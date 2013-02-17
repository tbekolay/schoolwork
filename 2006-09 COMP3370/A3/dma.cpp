#include <stdio.h>
#include <fstream>
#include <string>
#include <vector>
#include <math.h>

using namespace std;

////////////////////////////////////////////////////////////////////
// constants and structures

// we have an x block cache with y words each
#define CACHE_BLOCKS  8
#define BLOCK_SIZE    1

// constants for our processor definition
#define WORD_SIZE     2
#define CODE_SIZE     1024
#define REGISTERS			16
// our data area is now based on blocks
#define DATA_SIZE     (1024/BLOCK_SIZE)  

// number of bytes to print on a line
#define LINE_LENGTH		32

// our filled illegal instruction
#define MEM_FILLER    0xFF

// don't allow for more than 1000000 branches
#define BRANCH_LIMIT  1000000

// macros to convert between tags and addresses
#define addr2tag( addr ) (addr/BLOCK_SIZE)
#define addr2offset( addr ) (addr%BLOCK_SIZE)

// DMA memory mapped I/O locations
#define HD_ADDR       1
#define MEM_ADDR      2
#define STATUS        0

#define HD_SIZE       4096

// our opcodes are nicely incremental
enum OPCODES
{
  ADD_OPCODE,
  SUB_OPCODE,
  AND_OPCODE,
  OR_OPCODE,
  XOR_OPCODE,
  MOVE_OPCODE,
  SHIFT_OPCODE,
  BRANCH_OPCODE,
  NUM_OPCODES
};

typedef enum OPCODES Opcode;

// We have specific phases that we use to execute each instruction.
// We use this to run through a simple state machine that always advances to the
// next state and then cycles back to the beginning.
enum PHASES
{
  FETCH_INSTR,
  DECODE_INSTR,
  CALCULATE_EA,
  FETCH_OPERANDS,
  EXECUTE_INSTR,
  WRITE_BACK,
  INTERRUPTS,
  NUM_PHASES,
  // the following are error return codes that the state machine may return
  ILLEGAL_OPCODE,    // indicates that we can't execute anymore instructions
  INFINITE_LOOP,     // indicates that we think we have an infinite loop
  ILLEGAL_ADDRESS,   // inidates that we have an memory location that's out of range
};

typedef enum PHASES Phase;

// We use a structure to maintain our current state. This allows for the information
// to be easily passed around.
struct STATE
{
  // internal registers used for system operation
  // note that these are all 16-bit meaning that all memory accesses will by 16-bit
  unsigned short PC;
  unsigned short MDR;
  unsigned short MAR;
  // note that this could be done with a union or bit fields on a short
  unsigned char IR[2];
  
  // DMA registers -- this would really be outside the CPU state but then so should MAR/MDR
  unsigned short HDAddr;
  unsigned short MemAddr;
  unsigned short HDStatus;
  
  // the current instruction and it's operand's EA, would be encoded in the CU's control logic
  Opcode				 opcode;
  unsigned char  mode;          // the "addressing mode" for the current opcode
                                // stores the actual operand data that we will manipulate
                                // x and y are the ALU inputs and z is the output
  unsigned short ALU_x;
  unsigned short ALU_y;
  unsigned short ALU_z;
};

typedef struct STATE State;

// we need to store cache data and the current state of the cache entries
struct CACHE_ENTRY
{
  bool           valid;
//  bool           dirty;
  unsigned long  ref_count;     // the smallest value gets replaced
  unsigned short tag;           // how many bits?
};

typedef struct CACHE_ENTRY CacheEntry;

// standard function pointer to run our control unit state machine
typedef Phase (*process_phase)(void);


////////////////////////////////////////////////////////////////////
// prototypes
Phase fetch_instr( void );
Phase decode_instr( void );
Phase calculate_ea( void );
Phase fetch_operands( void );
Phase execute_instr( void );
Phase write_back( void );
Phase interrupts( void );


////////////////////////////////////////////////////////////////////
// local variables

// a count of branches so we can stop processing if we get an infinite loop
static int branch_count = 0;

// memory for our code and data
static unsigned char code[CODE_SIZE][WORD_SIZE];
// must reorganize memory to match our cache layout
static unsigned char data[DATA_SIZE][BLOCK_SIZE][WORD_SIZE];

// our "hard drive" for storing data long term
static unsigned char hd[HD_SIZE][WORD_SIZE];

// our cache area
static unsigned char data_cache[CACHE_BLOCKS][BLOCK_SIZE][WORD_SIZE];
// the cache dictionary
static CacheEntry dictionary[CACHE_BLOCKS];

// cache statistics
// note that we can use the ref_count (below) - 1 as our total number of memory references
static unsigned long cache_hits = 0;

// we have a reference count that monotonically increases to manage the LRU policy
// we change the entry's count every time it's accessed
// this value stores the next value to use
static unsigned long current_ref_count = 1;

// our general purpose registers
// NOTE: we let the registers match the host endianness so that the operations are easier -- all mapping occurs at the MDR
static unsigned short registers[REGISTERS];

// tracks what we're currently doing
static State state;

// A list of handlers to process each state. Provides for a nice simple
// state machine loop and is easily extended without using a huge
// switch statement.
static process_phase control_unit[NUM_PHASES] =
{
  fetch_instr, decode_instr, calculate_ea, fetch_operands, execute_instr, write_back, interrupts
};


//////////////////////////////////////////////////////////////////////////
// cache processing routines 


// writes a given block to memory and makes the cache block available for use
void write_block( int block_id )
{
  int i = dictionary[block_id].tag;
  
  // make sure it's valid first...
  if ( dictionary[block_id].valid )
  {
    // if it's dirty write the data
//    if ( dictionary[block_id].dirty )
    {
      // write the block (with each element being a 2 byte word)
      // note that the tag is our memory block identifier!
      for ( i=0 ; i<BLOCK_SIZE ; i++ )
      {
        data[dictionary[block_id].tag][i][0] = data_cache[block_id][i][0];
        data[dictionary[block_id].tag][i][1] = data_cache[block_id][i][1];
      }
    }

    // clear the dictionary
//    dictionary[block_id].valid = false;
//    dictionary[block_id].dirty = false;
//    dictionary[block_id].ref_count = 0;
  }
}


// find the least recently used block and writes it back to main memory
int removeLRU( void )
{
  unsigned long LRU = 0xFFFFFFFF;
  int i;
  int block_id = 0;

  // find the LRU block
  for ( i=0 ; i<CACHE_BLOCKS ; i++ )
  {
    // make sure it's a valid block (should be redundant here...)
    if ( dictionary[i].valid )
    {
      if ( dictionary[i].ref_count < LRU )
      {
        LRU = dictionary[i].ref_count;
        block_id = i;
      }
    }
  }
  
  // write it back to memory
//  write_block( block_id );

  // must reset the cache table entry for the next block to go in
  dictionary[block_id].valid = false;
  dictionary[block_id].ref_count = 0;
  
  return( block_id );
}


// pulls the given block from memory and places it into an available cache block
int fetch_block( unsigned short tag )
{
  int i;
  int block_id = 0;
  bool found = false;

  // find the first free block -- keeping a pointer would be more efficient
  for ( i=0 ; i<CACHE_BLOCKS && !found ; i++ )
  {
    if ( dictionary[i].valid == false )
    {
      block_id = i;
      found = true;
    }
  }

  // if we didn't find a block, kill one
  if ( !found )
    block_id = removeLRU();

  // load the required data
  // note that the tag is our memory block identifier!
  for ( i=0 ; i<BLOCK_SIZE ; i++ )
  {
    data_cache[block_id][i][0] = data[tag][i][0];
    data_cache[block_id][i][1] = data[tag][i][1];
  }

  // indicate that it's available
  dictionary[block_id].valid = true;
//  dictionary[block_id].dirty = false;
  dictionary[block_id].tag = tag;

  return( block_id );
}


// looks for the tag in the dictionary and sets the block id if found
bool find_block( unsigned long tag, int &block_id )
{
  bool found = false;
  int i;

  // simple linear search...
  for ( i=0 ; i<CACHE_BLOCKS && !found ; i++ )
  {
    // make sure it's valid first!
    if ( dictionary[i].valid )
    {
      if (dictionary[i].tag == tag )
      {
        block_id = i;
        found = true;
      }
    }
  }

  return( found );
}

// a DMA transfer has changed a word in memory -- must invalidate cache entry if the word's block is in cache
void snoop_cache()
{
  unsigned long  tag = addr2tag( state.MemAddr );
  int block_id;
  
  // is the tag in the cache? If so, mark it as invalid...
  if ( find_block( tag, block_id ) )
  {
    dictionary[block_id].valid = false;
  }
}

// write the data (wrt the MAR/MDR) into the cache, fetching the block if required
Phase cache_write( void )
{
  Phase rc = FETCH_INSTR;
  unsigned long  tag = addr2tag( state.MAR );
  unsigned short offset = addr2offset( state.MAR );
  int block_id;
  
  // make sure it's in range
  // must adjust the data size back up to a byte based value -- it's now in blocks...
  if ( state.MAR <= (DATA_SIZE*BLOCK_SIZE*2) )
  {
    // if the block isn't in the cache, put it in
    if ( !find_block( tag, block_id ) )
      block_id = fetch_block( tag );
    
    // we have a cache hit!
    else
      cache_hits++;
    
    // write the word
    data_cache[block_id][offset][0] = state.MDR>>8;
    data_cache[block_id][offset][1] = state.MDR&0x00ff;
    
    // up the block's reference count
    dictionary[block_id].ref_count = current_ref_count++;
    
    // write the block through to memory
    write_block( block_id );
  }
  
  else
    rc = ILLEGAL_ADDRESS;
  
  return( rc );
}


// read in the data from the cache and fetch from memory as required
Phase cache_read( void )
{
  Phase rc = WRITE_BACK;
  unsigned long  tag = addr2tag( state.MAR );
  unsigned short offset = addr2offset( state.MAR );
  int block_id;

  // make sure it's in range
  // must adjust the data size back up to a byte based value -- it's now in blocks...
  if ( state.MAR <= (DATA_SIZE*BLOCK_SIZE*2) )
  {
    // if the block isn't in the cache, put it in
    if ( !find_block( tag, block_id ) )
      block_id = fetch_block( tag );
    
    // we have a cache hit!
    else
      cache_hits++;
    
    // read the word
    // map our data assuming that we have big endian coming in
    state.MDR = data_cache[block_id][offset][0];
    state.MDR <<= 8;
    state.MDR |= data_cache[block_id][offset][1];
    
    // up the block's reference count
    dictionary[block_id].ref_count = current_ref_count++;
  }
  
  else
    rc = ILLEGAL_ADDRESS;
  
  return( rc );
}


//////////////////////////////////////////////////////////////////////////
// state processing routines -- note that they all have the same prototype


// pulls a literal value from the 2nd operand of the current instruction
char extract_literal()
{
  char value = (state.IR[1] & 0x3F);
  
  // sign extend if negative
  if ( value & 0x20 )
    value |= 0xC0;
  
  return( value );
}


// simply pulls the instruction from code memory, verifying the address first
Phase fetch_instr( void )
{
  Phase rc = DECODE_INSTR;
  
  // make sure it's in range
  if ( state.PC <= CODE_SIZE )
  {
    state.IR[0] = code[state.PC][0];
    state.IR[1] = code[state.PC][1];
  }
  else
    rc = ILLEGAL_ADDRESS;
  
  return( rc );
}


// pulls the opcode and addressing mode and verifies that the instruction is valid.
// uses the instruction characterization to decide where to go next.
Phase decode_instr( void )
{
  Phase rc = FETCH_OPERANDS;
  
  state.opcode = (Opcode)(state.IR[0] >> 5);
  
  // get the mode and mask it so we don't have to worry about higher bits in our tests
  state.mode = state.IR[0] >> 2;
  state.mode &= 0x07;
  
  // validate the instruction before continuing
  switch( state.opcode )
  {
    // valid modes are 000b and 001b
    case ADD_OPCODE:
    case SUB_OPCODE:
    case AND_OPCODE:
    case OR_OPCODE:
    case XOR_OPCODE:
    case SHIFT_OPCODE:
      if ( state.mode > 1 )
        rc = ILLEGAL_OPCODE;
      break;
      
      // invalid mode if the second bit is set
    case MOVE_OPCODE:
      if ( state.mode & 0x02 )
        rc = ILLEGAL_OPCODE;
      else
        rc = CALCULATE_EA;
      break;
      
      // invalid mode if all 3 bits are set
    case BRANCH_OPCODE:
      if ( state.mode == 0x07 )
        rc = ILLEGAL_OPCODE;
      break;
      
      // all other opcode values are invalid  
    default:
      rc = ILLEGAL_OPCODE;
      break;
  }
  
  return( rc );
}

static unsigned char get_reg1()
{
  unsigned char reg1 = 0xFF;
  reg1 = ((state.IR[0]&0x03)<<2) | (state.IR[1]>>6);
  reg1 &= 0x0F;
  
  return reg1;
  
}
// effective address calculations are only required if we have to access memory
// with the address placed in the MAR
Phase calculate_ea( void )
{
  Phase rc = FETCH_OPERANDS;
  unsigned char reg = 0xFF;
  
  // the first operand has our memory address
  if ( state.mode & 0x04 )
  {
    reg = get_reg1();
  }
  
  // the second operand has our memory address
  else if ( state.mode & 0x01 )
  {
    reg = state.IR[1] >> 2;
    reg &= 0x0F;
  }
  
  // load the address if we have a valid register
  if ( reg != 0xFF )
  {
    state.MAR = registers[reg];
  }
  
  return( rc );
}


// uses the instruction and addressing mode to decide how to get the data
Phase fetch_operands( void )
{
  Phase rc = EXECUTE_INSTR;
  unsigned char reg;

  // operand 1 is always register contents...
  // unless it's a MOVE, then it's a destination and doesn't need fetching
  if ( state.opcode != MOVE_OPCODE )
  {
    reg = get_reg1();
    state.ALU_x = registers[reg];
  }

  // operand 2 is more complicated...
  
  // calculate a register value in case we need it
  reg = state.IR[1] >> 2;
  reg &= 0x0F;
  switch( state.opcode )
  {
    // depending on the mode, put register contents or the literal into the "register"
    case ADD_OPCODE:
    case SUB_OPCODE:
    case AND_OPCODE:
    case OR_OPCODE:
    case XOR_OPCODE:
      if ( state.mode == 0 )
        state.ALU_y = extract_literal();
      else
        state.ALU_y = registers[reg];
      break;

    // to simplify things we always put the data into the MDR, even for a literal to
    // register transfer. 
    case MOVE_OPCODE:
      // no need to execute an instruction here
      rc = WRITE_BACK;
      
      // copy in the literal or register contents
      if ( (state.mode & 0x01) == 0 )
        state.MDR = extract_literal();
      else if ( state.mode & 0x04 )
        state.MDR = registers[reg];
        
        // otherwise, fetch from memory
      else
      {
        // catch DMA register access
        if ( state.MAR <= MEM_ADDR )
        {
          switch ( state.MAR )
          {
            case HD_ADDR:
              state.MDR = state.HDAddr;
              break;
              
            case MEM_ADDR:
              state.MDR = state.MemAddr;
              break;
              
            case STATUS:
              state.MDR = state.HDStatus;
              break;
          }
        }
        else
          rc = cache_read();
      }
        
      break;

    // branches always have a literal, ignored for jumps...  
    case BRANCH_OPCODE:
      state.ALU_y = extract_literal();
      break;

    default:
      break;
  }
  
  return( rc );
}


// based on the opcode, performs the operation on the ALU inputs
Phase execute_instr( void )
{
  Phase rc = WRITE_BACK;
  
  switch( state.opcode )
  {
    case ADD_OPCODE:
      state.ALU_z = (short)state.ALU_x + (short)state.ALU_y;
      break;

    case SUB_OPCODE:
      state.ALU_z = (short)state.ALU_x - (short)state.ALU_y;
      break;

    case AND_OPCODE:
      state.ALU_z = state.ALU_x & state.ALU_y;
      break;

    case OR_OPCODE:
      state.ALU_z = state.ALU_x | state.ALU_y;
      break;

    case XOR_OPCODE:
      state.ALU_z = state.ALU_x ^ state.ALU_y;
      break;

    case SHIFT_OPCODE:
      if ( state.mode == 0 )
        state.ALU_z = state.ALU_x >> 1;
      else
        state.ALU_z = state.ALU_x << 1;
      break;

    // note that this is where my design is incomplete since I should do a branch
    // over a couple of iterations or I need more registers going to the ALU
    // Oh well, I consider this to be good enough :)  
    case BRANCH_OPCODE:
      // handle the jump separately since it's special
      if ( state.mode == 0 )
      {
        state.ALU_z = state.ALU_x;

        // check for infinite loops
        branch_count++;
        if (branch_count > BRANCH_LIMIT )
          rc = INFINITE_LOOP;
      }

      else
      {
        bool branch = false;

        switch( state.mode )
        {
            // BEQ
          case 1:
            if ( (short)state.ALU_x == (short)registers[0] )
              branch = true;
            break;

            // BNE
          case 2:
            if ( (short)state.ALU_x != (short)registers[0] )
              branch = true;
            break;

            // BLT
          case 3:
            if ( (short)state.ALU_x < (short)registers[0] )
              branch = true;
            break;

            // BGT
          case 4:
            if ( (short)state.ALU_x > (short)registers[0] )
              branch = true;
            break;

            // BLE
          case 5:
            if ( (short)state.ALU_x <= (short)registers[0] )
              branch = true;
            break;

            // BGE
          case 6:
            if ( (short)state.ALU_x >= (short)registers[0] )
              branch = true;
            break;
        }

        // we always update the PC, but it only changes if required
        if ( branch )
        {
          state.ALU_z = state.PC + state.ALU_y;

          // check for infinite loops
          branch_count++;
          if (branch_count > BRANCH_LIMIT )
            rc = INFINITE_LOOP;
        }

        else
          // don't forget to account for the increment...
          state.ALU_z = state.PC + 1;
      }
      break;

    default:
      break;
  }

  return( rc );
}


// we will either write to a register, the PC or memory
Phase write_back( void )
{
  Phase rc = INTERRUPTS;
  unsigned char reg;

  // determine the register we may have to write into
  reg = get_reg1();
  
  switch( state.opcode )
  {
    // always writing to a register
    case ADD_OPCODE:
    case SUB_OPCODE:
    case AND_OPCODE:
    case OR_OPCODE:
    case XOR_OPCODE:
    case SHIFT_OPCODE:
      registers[reg] = state.ALU_z;
      break;

    // update the PC, if no branch it will simply re-write itself  
    case BRANCH_OPCODE:
      state.PC = state.ALU_z;
      break;

    case MOVE_OPCODE:
      // do we put the contents of MDR into a register or memory?
      if ( state.mode & 0x04 )
      {
        // memory

        // catch DMA register access
        if ( state.MAR <= MEM_ADDR )
        {
          switch ( state.MAR )
          {
            case HD_ADDR:
              state.HDAddr = state.MDR;
              break;
              
            case MEM_ADDR:
              state.MemAddr = state.MDR;
              break;
              
            case STATUS:
              state.HDStatus = state.MDR;
              break;
          }
        }
        else
          rc = cache_write();
      }
      
      else
      {
        // register
        registers[reg] = state.MDR;
      }
      break;
      
    default:
      break;
  }

  // don't forget to increment the program counter if haven't done a branch
  // do it here since we know we'll always end an instruction here...
  if ( state.opcode != BRANCH_OPCODE )
    state.PC++;

  return( rc );
}

// see if we need to perform and DMA transfers
Phase interrupts( void )
{
  Phase rc = FETCH_INSTR;
  unsigned short wordCount = state.HDStatus >> 1;
  unsigned char direction = state.HDStatus & 0x0001;
  int countDown = 4;
  unsigned char *currMem;
  
  
  if ( wordCount > 0 )
  {
    // transfer until we have no more data to move or we've done enough for this round
    while ( wordCount > 0 && countDown > 0 )
    {
      // must map from memory organized for caching to a flat model for transfers
      currMem = &(data[0][0][0]) + state.MemAddr;
      
      if ( direction )
      {
        *currMem = hd[state.HDAddr][0];
        *(currMem+1) = hd[state.HDAddr][1];
        
        // make sure we check the cache since memory has been updated
        snoop_cache();
      }

      else
      {
        hd[state.HDAddr][0] = *currMem;
        hd[state.HDAddr][1] = *(currMem+1);
      }
      
      state.MemAddr++;
      state.HDAddr++;
      countDown--;
      wordCount--;
    }
  }

  // set the new wordCount
  state.HDStatus = (wordCount<<1) | direction;
  
  return( rc );
}


////////////////////////////////////////////////////////////////////
// general routines


// initializes us to get going
void initialize_system()
{
  int i, j;
  
  state.PC = 0;
  state.MDR = 0;
  state.MAR = 0;
  state.HDStatus = 0;
  state.HDAddr = 0;
  state.MemAddr = 0;
  state.ALU_x = 0;
  state.ALU_y = 0;
  state.ALU_z = 0;
  state.opcode = NUM_OPCODES;  // an illegal opcode...
  state.mode = 0;

  // fill all of our code and data space
  for ( i=0 ; i<CODE_SIZE ; i++ )
  {
    code[i][0] = MEM_FILLER;
    code[i][1] = MEM_FILLER;
  }
  for ( i=0 ; i<DATA_SIZE ; i++ )
  {
    for ( j=0; j<BLOCK_SIZE ; j++ )
    {
      data[i][j][0] = MEM_FILLER;
      data[i][j][1] = MEM_FILLER;
    }
  }

  for ( i=0 ; i<HD_SIZE ; i++ )
  {
    hd[i][0] = MEM_FILLER;
    hd[i][1] = MEM_FILLER;
  }
  
  // initialize our registers
  for ( i=0 ; i<REGISTERS ; i++ )
    registers[i] = 0;

  // initialize our cache to be empty
  for ( i=0 ; i<CACHE_BLOCKS ; i++ )
  {
    dictionary[i].valid = false;
//    dictionary[i].dirty = false;
    dictionary[i].tag = 0;
    dictionary[i].ref_count = 0;

    // initialize all cache data -- not required but we'll see any bad references this way...
    for ( j=0; j<BLOCK_SIZE ; j++ )
    {
      data_cache[i][j][0] = MEM_FILLER;
      data_cache[i][j][1] = MEM_FILLER;
    }
  }
}


// checks the hex value to ensure it a printable ASCII character. If
// it isn't, '.' is returned instead of itself
char valid_ascii( unsigned char hex_value )
{
  if ( hex_value < 0x21 || hex_value > 0x7e )
    hex_value = '.';

  return( (char)hex_value );
}


// takes the data and prints it out in hexadecimal and ASCII form
void print_memory( void )
{
  int block_index = 0;
  int word_index = 0;
  int text_index = 0;
  char the_text[LINE_LENGTH+1];

  // print each line 1 at a time
  for ( block_index=0 ; block_index<DATA_SIZE ; block_index++ )
  {
    // add 1 word at a time, but don't go beyond the and of the data
    for ( word_index=0 ; word_index<BLOCK_SIZE ; word_index++ )
    {
      the_text[text_index++] = valid_ascii( (unsigned char)(data[block_index][word_index][0]) );
      the_text[text_index++] = valid_ascii( (unsigned char)(data[block_index][word_index][1]) );
      printf( "%02x%02x ", data[block_index][word_index][0], data[block_index][word_index][1] );

      // print out a line if we're at the end
      if ( text_index == LINE_LENGTH )
      {
        text_index = 0;
        the_text[LINE_LENGTH] = '\0';
        printf( "\t\'%s\'\n", the_text );
      }
    }
  }
}


// converts the passed string into binary form and inserts it into our data area
// assumes and even number of characters!!!
void insert_data( string line )
{
  static int    data_index = 0;
  static int    block_index = 0;
  unsigned int  i;
  char          ascii_data[5];    // I know it's hard coded....
  unsigned char byte1;
  unsigned char byte2;
  
  ascii_data[4] = '\0';
  
  for ( i=0 ; i<line.length() ; i+=4 )
  {
    // extract the word...
    ascii_data[0] = line[i];
    ascii_data[1] = line[i+1];
    ascii_data[2] = line[i+2];
    ascii_data[3] = line[i+3];
    
    // put it into the data area if we have space
    if ( data_index < DATA_SIZE )
    {
      sscanf( ascii_data, "%02hhx%02hhx", &byte1, &byte2 );
      data[data_index][block_index][0] = byte1;
      data[data_index][block_index++][1] = byte2;
      
      // max out the block count
      if ( block_index == BLOCK_SIZE )
      {
        data_index++;
        block_index = 0;
      }
    }
  }
}


// reads in the file data and returns true is our code and data areas are ready for processing
bool load_files( const char *code_filename, const char *data_filename )
{
  FILE           *code_file = NULL;
  FILE           *hd_file = NULL;
  std::ifstream  data_file( data_filename );
  string         line;           // used to read in a line of text
  bool           rc = false;
  
  // using RAW C here since I want to have straight binary access to the data
  code_file = fopen( code_filename, "r" );
  
  // since we're allowing anything to be specified, make sure it's a file...
  if ( code_file )
  {
    // put the code into the code area
    fread( code, 1, CODE_SIZE*WORD_SIZE, code_file );
    
    fclose( code_file );
    
    // since we're allowing anything to be specified, make sure it's a file...
    if ( data_file.is_open() )
    {
      // read the data into our data area
      getline( data_file, line );
      while ( !data_file.eof() )
      {
        // put the data into the data area
        insert_data( line );
        
        getline( data_file, line );
      }
      data_file.close();
      
      // both files were read so we can continue processing
      rc = true;
    }
  }
  
  // load up our current HD file
  hd_file = fopen( "hd", "r" );
  
  // make sure the file is present before reading...
  if ( hd_file )
  {
    // put the hd data into the hd area
    fread( hd, 1, HD_SIZE*WORD_SIZE, hd_file );
    
    fclose( hd_file );
  }
  
  return( rc );
}

// save the current HD contents to our file
void save_hd()
{
  FILE *hd_file = NULL;
  
  // re-create the hd file each time...
  // doing RAW C here so I can do a straight binary write to the file
  hd_file = fopen( "hd", "w" );
  
  // spit it out if we have a file to write to
  if ( hd_file )
  {
    fwrite( hd, 1, HD_SIZE*WORD_SIZE, hd_file );
    
    fclose( hd_file );
  }
}

// runs our simulation after initializing our memory
int main (int argc, const char * argv[])
{
  Phase current_phase = FETCH_INSTR;  // we always start if an instruction fetch
//  int i;
  
  initialize_system();

  // read in our code and data
  if ( load_files( argv[1], argv[2] ) )
  {
    // run our simulator
    while ( current_phase < NUM_PHASES )
      current_phase = control_unit[current_phase]();

    // write back the contents of the cache
//    for ( i=0 ; i<CACHE_BLOCKS ; i++ )
//      write_block( i );
    
    // output what stopped the simulator
    switch( current_phase )
    {
      case ILLEGAL_OPCODE:
        printf( "Illegal instruction %02x%02x detected at address %04x\n\n",
                state.IR[0], state.IR[1], state.PC );
        break;
        
      case INFINITE_LOOP:
        printf( "Possible infinite loop detected with instruction %02x%02x at address %04x\n\n",
                state.IR[0], state.IR[1], state.PC );
        break;
        
      case ILLEGAL_ADDRESS:
        printf( "Illegal address %04x detected with instruction %02x%02x at address %04x\n\n",
                state.MAR, state.IR[0], state.IR[1], state.PC );
        break;
        
      default:
        break;
    }

    // print our cache statistics
    printf( "There were a total of %ld cache hits and %ld cache misses, for a hit rate of %4.3f.\n\n",
            cache_hits, current_ref_count-cache_hits-1,
            (double)cache_hits/(double)(current_ref_count-1) );
    
    // print out the data area
    print_memory();
    
    // make sure we have a persistent state...
    save_hd();
  }
}
