//-----------------------------------------
// NAME: Trevor Bekolay
// STUDENT NUMBER: 6796723
// COURSE: Comp 3370 , SECTION: A01
// INSTRUCTOR: Michael Zapp
// ASSIGNMENT: Assignment 2
// 
// REMARKS: Based on Michael Zapp's solution
// to assignment 1 (a simulator for a certain
// ISA), but with a fully associative cache
// added.
//
//-----------------------------------------

#include <stdio.h>
#include <math.h>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

////////////////////////////////////////////////////////////////////
// constants and structures

// constants for our processor definition (sizes are in words)
#define WORD_SIZE		2
#define DATA_SIZE		1024
#define CODE_SIZE		1024
#define REGISTERS		16

// number of bytes to print on a line
#define LINE_LENGTH		32

// our filled illegal instruction
#define MEM_FILLER		0xFF

// don't allow for more than 1000000 branches
#define BRANCH_LIMIT	1000000

//******************************************************************
//	Structure of an address:
//		1024 words in data array
//		10 bit addresses
//		since fully associative, address is: [tag][block offset]
//		where length(block offset) = log(BLOCKSIZE)
//		and tag is the rest
//******************************************************************

// define the associativity and block size for the cache
#define NUMBLOCKS		33
#define BLOCKSIZE		8
#define BL_OFFSET		(int)log2( BLOCKSIZE ) //length of the block offset. This is N!

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

// this structure represents a block in the cache.
struct BLOCK
{
	// to account for compulsory misses
	bool valid;
	// since we are using write-back
	bool dirty;

	// addresses are only 10 bits, so tag can always fit in a short
	unsigned short tag;
	unsigned short ca_data[BLOCKSIZE];
};

typedef struct BLOCK Block;


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

unsigned short load( void );
void store( unsigned short st_data );


////////////////////////////////////////////////////////////////////
// local variables

// a count of branches so we can stop processing if we get an infinite loop
static int branch_count = 0;

// memory for our code and data, using our word size for a second dimension to make accessing bytes easier
static unsigned char code[CODE_SIZE][WORD_SIZE];
static unsigned char data[DATA_SIZE][WORD_SIZE];

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
  fetch_instr, decode_instr, calculate_ea, fetch_operands, execute_instr, write_back
};

// the cache
static Block cache[NUMBLOCKS];

// tracks cache hits and misses
static int hits;
static int misses;

// keeps track of cache usage for LRU replacement strategy
// least recently used elements are at the beginning of the vector
static vector<int> lru;


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
      {
        state.MDR = registers[reg];
      }

      // otherwise, fetch from memory
      else
      {
        // make sure it's in range
        if ( state.MAR <= DATA_SIZE )
        {
          // load data from cache
          state.MDR = load();
        }
        else
          rc = ILLEGAL_ADDRESS;
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
  Phase rc = FETCH_INSTR;
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

        // make sure it's in range
        if ( state.MAR <= DATA_SIZE )
        {
          // store the data to the cache
          store( state.MDR );
        }
        else
          rc = ILLEGAL_ADDRESS;
      }

      else
        // register
        registers[reg] = state.MDR;

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


////////////////////////////////////////////////////////////////////
// general routines


// builds our state machine "virtual function table" and initializes us to get going
void initialize_system()
{
  int i;

  state.PC = 0;
  state.MDR = 0;
  state.MAR = 0;
  state.ALU_x = 0;
  state.ALU_y = 0;
  state.ALU_z = 0;
  state.opcode = NUM_OPCODES;  // an illegal opcode...
  state.mode = 0;

  // intializes hit and miss tracking
  hits = 0;
  misses = 0;

  // fill all of our code and data space
  for ( i=0 ; i<CODE_SIZE ; i++ )
  {
    code[i][0] = MEM_FILLER;
    code[i][1] = MEM_FILLER;
  }
  for ( i=0 ; i<DATA_SIZE ; i++ )
  {
    data[i][0] = MEM_FILLER;
    data[i][1] = MEM_FILLER;
  }

  // initialize our registers
  for ( i=0 ; i<REGISTERS ; i++ )
    registers[i] = 0;

  // initialize the cache and LRU list
  for ( i=0 ; i<NUMBLOCKS ; i++ )
  {
	  cache[i].valid = false;
	  cache[i].dirty = false;
	  lru.push_back( i );
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
  int ix = 0;
  int text_index = 0;
  char the_text[LINE_LENGTH+1];

  // print each line 1 at a time
  for ( ix=0 ; ix<DATA_SIZE ; ix++ )
  {
      the_text[text_index++] = valid_ascii( (unsigned char)(data[ix][0]) );
      the_text[text_index++] = valid_ascii( (unsigned char)(data[ix][1]) );
      printf( "%02x%02x ", data[ix][0], data[ix][1] );

      // print out a line if we're at the end
      if ( text_index == LINE_LENGTH )
      {
        text_index = 0;
        the_text[LINE_LENGTH] = '\0';
        printf( "\t\'%s\'\n", the_text );
      }
  }
}


// converts the passed string into binary form and inserts it into our data area
// assumes and even number of words!!!
void insert_data( string line )
{
  static int   data_index = 0;
  unsigned int i;
  char         ascii_data[5];
  unsigned char byte1;
  unsigned char byte2;

  ascii_data[4] = '\0';

  for ( i=0 ; i<line.length() ; i+=4 )
  {
    ascii_data[0] = line[i];
    ascii_data[1] = line[i+1];
    ascii_data[2] = line[i+2];
    ascii_data[3] = line[i+3];
    if ( data_index < DATA_SIZE*WORD_SIZE )
    {
      sscanf( ascii_data, "%02hhx%02hhx", &byte1, &byte2 );
      data[data_index][0] = byte1;
      data[data_index++][1] = byte2;
    }
  }
}


// reads in the file data and returns true is our code and data areas are ready for processing
bool load_files( const char *code_filename, const char *data_filename )
{
  FILE           *code_file = NULL;
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

  return( rc );
}

//------------------------------------------------------
// lru_remove
//
// PURPOSE: C++'s vector class doesn't come with a search
//  or remove(element) functionality.  This deletes an
//  element from the lru vector<int>. 
// INPUT PARAMETERS:
//  element - the int we wish to delete
// OUTPUT PARAMETERS:
//  returns a boolean, true if an element was deleted
//  false if not
//------------------------------------------------------
bool lru_remove( int element )
{
  int i; //loop counter
  bool deleted = false; // ensures an element was actually deleted

  // looks at each element in the vector, deletes all elements
  // matching the passed element
  for( i=0 ; i<lru.size() ; i++ )
  {
    if( lru.at(i) == element )
    {
      lru.erase( lru.begin() + i );
      deleted = true;
    }
  }

  return deleted;
}

//------------------------------------------------------
// write_block
//
// PURPOSE: Copies a block from the cache to main
//  memory.
// INPUT PARAMETERS:
//  ca_index - the index of the block that we wish to
//  copy to memory.
//------------------------------------------------------
void write_block( int ca_index )
{
  unsigned short address; // an address in main memory
  unsigned short st_data; // data from the cache to be written
  int i; // loop counter

  // writes every element in a block to appropriate location
  // in main memory
  for( i=0 ; i<BLOCKSIZE ; i++ )
  {
    address = cache[ca_index].tag;
    address <<= BL_OFFSET;
    address |= i;
    st_data = cache[ca_index].ca_data[i];
    data[address][0] = st_data>>8;
    data[address][1] = st_data&0x00ff;
  }
}

//------------------------------------------------------
// read_block
//
// PURPOSE: Loads a block from main memory to the cache. 
// INPUT PARAMETERS:
//  address - the address in main memory that we need
//  data from
// OUTPUT PARAMETERS:
//  returns the cache index that the block was loaded
//  into, as an int
//------------------------------------------------------
int read_block( unsigned short address )
{

  int i;
  int ca_index; // cache index - the next member in the LRU queue
  unsigned short ld_address; // address in main memory

  // takes the least recently used cache index
  // adds that cache index to the end of the list, now most recently used
  ca_index = lru.front();
  lru.erase( lru.begin() );
  lru.push_back( ca_index );

  // extracts the tag from the passed address
  address >>= BL_OFFSET;

  // using write-back update method
  if( cache[ca_index].dirty )
  {
    write_block( ca_index );
    cache[ca_index].dirty = false;
  }

  // copies a block of memory and stores it in the cache
  for( i=0 ; i<BLOCKSIZE ; i++ )
  {
    ld_address = address;
    ld_address <<= BL_OFFSET;
    ld_address |= i;
    // map our data assuming that we have big endian coming in
    cache[ca_index].ca_data[i] = data[ld_address][0];
    cache[ca_index].ca_data[i] <<= 8;
    cache[ca_index].ca_data[i] |= data[ld_address][1];
  }

  cache[ca_index].valid = true; // this block is now valid!
  cache[ca_index].tag = address;
  
  return ca_index;
}

//------------------------------------------------------
// load
//
// PURPOSE: Checks the cache for requested data to load.
//  If not present, will load from memory to cache.
// OUTPUT PARAMETERS:
//  returns an unsigned short containing the data that
//  we wanted to load
//------------------------------------------------------
unsigned short load( void )
{
  unsigned short ld_data; // data to be loaded
  unsigned short ld_tag; // tag extracted from MAR
  int i; // loop counter
  int offset;  // offset extracted from MAR
  bool found; // while loop stop condition

  // extract tag and offset from MAR
  ld_tag = state.MAR >> BL_OFFSET;
  offset = state.MAR & (BLOCKSIZE - 1);

  i = 0;
  found = false;

  // search each cache entry for the correct tag
  while( i<NUMBLOCKS && !found )
  {
    if( cache[i].tag == ld_tag && cache[i].valid )
    {
      found = true;
      ld_data = cache[i].ca_data[offset];
      // set that cache index to most recently used
      if( lru_remove( i ) )
	    lru.push_back( i );
      // monitor cache activity
      hits++;
    }
    i++;
  }

  // if not in cache, load from memory
  if( !found )
  {
    ld_data = cache[read_block( state.MAR )].ca_data[offset];
    // monitor cache activity
    misses++;
  }

  return ld_data;
}

//------------------------------------------------------
// store
//
// PURPOSE: Stores data to the cache, if present.
//  Otherwise, loads the block from memory to cache. 
// INPUT PARAMETERS:
//  st_data is the data we wish to store
//------------------------------------------------------
void store( unsigned short st_data )
{

  unsigned short st_tag; // tag extracted from MAR
  int i; // loop counter
  int offset;  // offset extracted from MAR
  bool found; // while loop stop condition
  int ca_index; // cache index

  // extract tag and offset from MAR
  st_tag = state.MAR >> BL_OFFSET;
  offset = state.MAR & (BLOCKSIZE - 1);

  i = 0;
  found = false;

  // search each cache entry for the correct tag
  while( i<NUMBLOCKS && !found )
  {
    if( cache[i].tag == st_tag && cache[i].valid )
    {
      found = true;
      cache[i].ca_data[offset] = st_data;
      cache[i].dirty = true;
      // set that cache index to most recently used
      if( lru_remove( i ) )
	      lru.push_back( i );
      // monitor cache activity
      hits++;
    }
    i++;
  }

  // if not in cache, load from memory
  if( !found )
  {
    ca_index = read_block( state.MAR );
    cache[ca_index].ca_data[offset] = st_data;
    cache[ca_index].dirty = true;
    // monitor cache activity
    misses++;
  }
}

//------------------------------------------------------
// cache_flush
//
// PURPOSE: Flushes out dirty blocks in the cache to 
//  main memory after the program completes.
//------------------------------------------------------
void cache_flush( void )
{
  int i;

  // check each cache block for dirtiness
  for( i=0 ; i<NUMBLOCKS ; i++ )
  {
    if( cache[i].dirty )
    {
      // copy all dirty blocks in cache to memory
      write_block( i );
      cache[i].dirty = false;
    }
  }
}

//------------------------------------------------------
// print_hitrate
//
// PURPOSE: Prints out the data gathered by cache
//  monitoring.
//------------------------------------------------------
void print_hitrate( void )
{
  // avoids div by 0 error if program does no loads or stores
  if( hits + misses > 0 )
  {
    printf( "Cache details for fully associative cache with %d block(s) of %d word(s) each:\n", NUMBLOCKS, BLOCKSIZE );
    printf( "Hits: %d\tMisses: %d\n", hits, misses );
    printf( "Overall hit rate: %.2f%%\n\n", 100 * (float)hits / (float)(hits + misses) );
  }
}

// runs our simulation after initializing our memory
int main (int argc, const char * argv[])
{
  Phase current_phase = FETCH_INSTR;  // we always start if an instruction fetch

  initialize_system();

  // read in our code and data
  if ( load_files( argv[1], argv[2] ) )
  {
    // run our simulator
    while ( current_phase < NUM_PHASES )
      current_phase = control_unit[current_phase]();

//    for( int i=0 ; i<NUMBLOCKS ; i++)
//    {
//	  state.MAR = 1 * BLOCKSIZE;
//      read_block( state.MAR );
//	  write_block( 0 );
//	  printf("MAR: %04x\tIndex: %d\n",state.MAR,i);
//    }

      
    cache_flush();
    print_hitrate();

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

    // print out the data area
    print_memory();
  }
}
