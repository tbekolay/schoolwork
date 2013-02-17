#include <stdio.h>
#include <fcntl.h>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

#define LABEL_SIZE		28

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
  BRANCH_OPCODE
};

// used to store label/address pairs so we can calculate branch addresses
struct BRANCH_POINT
{
  // no need for 16 bits since we can't go more than 6 bits for an offset
  unsigned char  address;
  char           label[LABEL_SIZE]; 
};

typedef struct BRANCH_POINT BranchPoint;

// constants for our processor definition
#define WORD_SIZE     2
#define DATA_SIZE     1024*WORD_SIZE
#define CODE_SIZE     1024*WORD_SIZE
#define REGISTERS			16

// number of bytes to print on a line
#define LINE_LENGTH		8


// checks the hex value to ensure it a printable ASCII character. If
// it isn't, '.' is returned instead of itself
char valid_ascii( unsigned char hex_value )
{
  if ( hex_value < 0x21 || hex_value > 0x7e )
    hex_value = '.';

  return( (char)hex_value );
}




// takes the data and puts it into a file
void create_object_file( char *filename, unsigned char *data, int length )
{
  FILE *object_file = NULL;
  char object_filename[strlen(filename)];

  // assumes that we have .asm at the end of each file name
  strncpy( object_filename, filename, strlen(filename)-3 );
  object_filename[strlen(filename)-3] = 'o';
  object_filename[strlen(filename)-2] = '\0';

  // create the new file
  // doing RAW C here so I can do a straight binary write to the file
  object_file = fopen( object_filename, "w+" );

  // spit it out if we have a file to write to
  if ( object_file )
  {
    fwrite( data, 1, length, object_file );

    fclose( object_file );
  }
}


// takes the data and prints it out in hexadecimal and ASCII form
void print_formatted_data( unsigned char *data, int length )
{
  int i, j, k;
  char the_text[LINE_LENGTH+1];

  // print each line 1 at a time
  for ( i=0 ; i<length ; i+=LINE_LENGTH )
  {
    // add 1 word at a time, but don't go beyond the and of the data
    for ( j=0 ; j<LINE_LENGTH && (i+j)<length ; j+=2 )
    {
      the_text[j] =  valid_ascii( data[i+j] );
      the_text[j+1] = valid_ascii( data[i+j+1] );
      printf( "%02x%02x ", data[i+j], data[i+j+1] );
    }

    // add in FFFF (invalid operation) to fill out the line
    if ( (i+j) >= length )
    {
      for ( k=j ; k<LINE_LENGTH ; k+=2 )
      {
        the_text[k] =  valid_ascii( 0xff );
        the_text[k+1] = valid_ascii( 0xff );
        printf( "ffff " );
      }
    }

    the_text[LINE_LENGTH] = '\0';
    printf( "\t\'%s\'\n", the_text );
  }
}


// returns the first 3 bits of the opcode corresponding to the given
// operation
unsigned char get_opcode( char *operation )
{
  unsigned char opcode;
  
  if ( strcmp( operation, "ADD" ) == 0 )
    opcode = ADD_OPCODE;

  else if ( strcmp( operation, "SUB" ) == 0 )
    opcode = SUB_OPCODE;

  else if ( strcmp( operation, "AND" ) == 0 )
    opcode = AND_OPCODE;

  else if ( strcmp( operation, "OR" ) == 0 )
    opcode = OR_OPCODE;

  else if ( strcmp( operation, "XOR" ) == 0 )
    opcode = XOR_OPCODE;

  else if ( strcmp( operation, "MOVE" ) == 0 )
    opcode = MOVE_OPCODE;

  else if ( strcmp( operation, "SRL" ) == 0 ||
            strcmp( operation, "SRR" ) == 0 )
    opcode = SHIFT_OPCODE;

  else if ( strcmp( operation, "JR" ) == 0  ||
            strcmp( operation, "BEQ" ) == 0 ||
            strcmp( operation, "BNE" ) == 0 ||
            strcmp( operation, "BLT" ) == 0 ||
            strcmp( operation, "BGT" ) == 0 ||
            strcmp( operation, "BLE" ) == 0 ||
            strcmp( operation, "BGE" ) == 0 )
    opcode = BRANCH_OPCODE;

  return( opcode );
}  


// returns the type specifier for the given opcode and it's operands
// specifies the specific addressing mode or operation sub-type
unsigned char get_opcode_type( unsigned char opcode, char * operation,
                                char *operand1, char *operand2 )
{
  unsigned char type = 0x00;  // default/base settings

  // arithmetic if on or before the XOR opcode
  if ( opcode <= XOR_OPCODE )
  {
    // need to indicate if the 2nd operand is a register
    if ( operand2[0] == 'R' )
      type = 0x01;
  }

  else if ( opcode == MOVE_OPCODE )
  {
    // need to specify addressing mode

    // handle destination being a memory locatoin
    if ( operand1[0] == '[' )
    {
      type = 0x04;

      // a register for a source is fine
      if ( operand2[0] == 'R' )
        type |= 0x01;

      // but another memory location isn't
      else if ( operand2[0] == '[' )
        type |= 0x02;
    }

    // otherwise it's a register
    else
    {
      // memory for a source is fine
      if ( operand2[0] == '[' )
        type |= 0x01;

      // but another register isn't
      else if ( operand2[0] == 'R' )
        type |= 0x02;
    }
}

  else if ( opcode == SHIFT_OPCODE )
  {
    // indicate left or right shifting
    if ( operation[2] == 'L' )
      type = 0x01;
  }

  else if ( opcode == BRANCH_OPCODE )
  {
    // identify the branch based on the instruction
    if ( strcmp( operation, "BEQ" ) == 0 )
      type = 0x01;
    else if ( strcmp( operation, "BNE" ) == 0 )
      type = 0x02;
    else if ( strcmp( operation, "BLT" ) == 0 )
      type = 0x03;
    else if ( strcmp( operation, "BGT" ) == 0 )
      type = 0x04;
    else if ( strcmp( operation, "BLE" ) == 0 )
      type = 0x05;
    else if ( strcmp( operation, "BGE" ) == 0 )
      type = 0x06;
    else if ( strcmp( operation, "BGE" ) == 0 )
      type = 0x06;
    else if ( strcmp( operation, "BGE" ) == 0 )
      type = 0x06;
  }

  return( type );
}


// extracts the register value from an operand
unsigned char get_register( char *operand )
{
  int reg;
  
  if ( operand[0] == 'R' )
    sscanf( operand, "R%d", &reg );
  else
    sscanf( operand, "[R%d]", &reg);

  return( (unsigned char)reg );
}


// Goes through each branch and finds the appropriate label.
// Adjusts the last 6 bits of the instruction at the branch location
// with the address of the found label.
void fix_branches( unsigned char *machine_code,
                   vector<BranchPoint*> &labels,
                   vector<BranchPoint*> &branches )
{
  int i,j;
  bool found = false;
  char offset;
  unsigned char *instr;
  
  // do each branch 1 at a time
  for ( i=0 ; i<(int)branches.size() ; i++ )
  {
    // simple linear search for the label
    found = false;
    for ( j=0 ; j<(int)labels.size() && !found ; j++ )
    {
      if ( strcmp( branches[i]->label, labels[j]->label ) == 0 )
        found = true;
    }

    // put in the branch offset
    // if not found you'll end up branching to yourself!!!
    if ( found )
    {
      // this subtraction makes labels prior to the branch have a
      // negative offset...
      // it also assumes the PC is incremented after the instruction is
      // executed, otherwise you'd have to add/subtract 1
      // we divide by 2 since we access by word but the assembler is working by byte
      offset = labels[j-1]->address/2 - branches[i]->address/2;
      instr = &machine_code[branches[i]->address + 1];

      // clear off the first 2 bits of the offset...
      offset &= 0x3F;

      // put in the new offset
      *instr |= offset;
    }
  }
}


// Takes each source line and converts it to the equivalent machine code.
// If there is a branch, it does a second pass to find the jump points.
// Returns the number of bytes of actual machine code.
int generate_machine_code( unsigned char *machine_code, vector<string> &source_text )
{
  int length = 0;
  int i;
  const char *line = NULL;
  int result;
  char label[LABEL_SIZE];     // arbitrary lengths -- not the best coding...
  char operation[6];
  char operands[36];
  char *operand1;
  char *operand2;
  bool labelled = false;
  // note that this could be done with a union or bit field over a short
  unsigned char instr_high;
  unsigned char instr_low;
  unsigned char opcode;
  // a list of labels that can be branched to
  vector<BranchPoint*> labels;
  // a list of branch operations that we have to jump from
  vector<BranchPoint*> branches;

  for ( i=0 ; i<(int)source_text.size() && length<CODE_SIZE ; i++ )
  {
    line = source_text[i].c_str();

    // try a labelled statement first
    labelled = true;
    result = sscanf( line, "%s %s %s", label, operation, operands );

    // if we don't have the correct number of variables, try non-labelled
    if ( result != 3 )
    {
      labelled = false;
      result = sscanf( line, "%s %s", operation, operands );
    }

    // don't process if we couldn't get a valid line
    if ( result != EOF )
    {
      // split off the operands
      operand1 = strtok( operands, "," );
      operand2 = strtok( NULL, "," );

      // start with the first 3 bits of the opcode
      opcode = get_opcode( operation );
      instr_high = opcode;
      
      // determine the next 3 bits based on our current opcode
      instr_high <<= 3;
      instr_high |= get_opcode_type( opcode, operation, operand1, operand2 );

      // put in the operand 1 code -- always a register value
      instr_high <<= 2;
      instr_high |= (get_register( operand1 ) >> 2);
      instr_low = get_register( operand1 ) & 0x03;

      // only process operand 2 if it's present
      if ( operand2 )
      {
        // put in the operand 2 code -- not always a register...
        if ( operand2[0] == 'R' || operand2[0] == '[' )
        {
          instr_low <<= 4;
          instr_low |= get_register( operand2 );

          // pad the last 2 bits
          instr_low <<= 2;
        }

        // must be a literal (unless a branch...)
        else
        {
          // for an opcode, we'll put and address in later
          if ( opcode == BRANCH_OPCODE )
          {
            BranchPoint *the_branch = new BranchPoint;

            the_branch->address = length;
            strcpy( the_branch->label, operand2 );
            branches.push_back( the_branch );

            // make space for adding the address
            instr_low <<= 6;
          }

          // definitely a literal
          else
          {
            unsigned char literal = (unsigned char)atoi( operand2 );

            // mask out the top 2 bits (only want numbers we can handle)
            // note that for negatives we would have to sign extend when
            // extracting in order to get the correct value
            literal &= 0x3F;

            instr_low <<= 6;
            instr_low |= literal;
          }
        }
      }

      // no operand 2, shift the instruction to fill the unused bits
      else
        instr_low <<= 6;
      
      // store a label's address so we can fix the branches later
      if ( labelled )
      {
        BranchPoint *the_label = new BranchPoint;

        the_label->address = length;
        strcpy( the_label->label, label );
        // get rid of the ":"
        the_label->label[strlen(the_label->label)-1] = '\0';

        labels.push_back( the_label );
      }

      // put the instruction into our code space
      machine_code[length++] = instr_high;
      machine_code[length++] = instr_low;
    }
  }

  // finally, put in the branch addresses
  fix_branches( machine_code, labels, branches );

  // free the memory used by the branch lists
  for ( i=0 ; i<(int)labels.size() ; i++ )
    delete labels[i];
  for ( i=0 ; i<(int)branches.size() ; i++ )
    delete branches[i];
  
  return( length );
}


int main (int argc, const char * argv[]) {
  std::ifstream  source_file( argv[1] );
  vector<string> source_text;
  string         line;           // used to read in a line of text
  unsigned char  machine_code[CODE_SIZE];
  int            byte_count = 0; // the number of bytes in the code
  
  // since we're allowing anything to be specified, make sure it's a file that ends in .asm...
  if ( source_file.is_open() && strstr( argv[1], ".asm") != NULL )
  {
    // read in the file
    while ( !source_file.eof() )
    {
      getline( source_file, line );
      source_text.push_back( line );
    }
    source_file.close();
    
    // process the file
    byte_count = generate_machine_code( machine_code, source_text );

    // create the executable
    create_object_file( (char *)argv[1], machine_code, byte_count );
    
    // output the machine code version
    print_formatted_data( machine_code, byte_count );
  }
  
  // if the file isn't open, tell the user...
  else
    printf( "%s isn't a valid filename\n", argv[1] );
}
