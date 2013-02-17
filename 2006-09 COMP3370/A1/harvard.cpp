//-----------------------------------------
// NAME: 				Trevor Bekolay
// STUDENT NUMBER: 		6796723
// COURSE: Comp 3370	SECTION: A01
// INSTRUCTOR: 			Michael Zapp
// ASSIGNMENT: 1 		QUESTION: 1
// 
// REMARKS: This program is a simulator for
//			a harvard CPU.  It loads a .o
//			object file and a data file,
//			then exectutes the code.
//-----------------------------------------

#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <stdio.h>

using namespace std;

// Opcodes are incremental
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

// Exit codes are incremental
enum EXITCODES
{
	SUCCESSFUL,
	CODE_OVERFLOW,
	INFINITE_LOOP,
	ILLEGAL_OPCODE,
	OUT_OF_RANGE
};

// 3 Possible states related to branching
enum BRANCH
{
	NO_BRANCH,
	FALSE_BRANCH,
	TRUE_BRANCH
};

// Constants for our processor definition
#define WORD_SIZE		2
#define ASCII_SIZE		2*WORD_SIZE // Number of ascii hex characters representing a word.
#define HEX				16			// Hex is base 16.
#define DATA_SIZE		1024		// # of words in data memory.
#define CODE_SIZE		1024		// # of words in code memory.
#define REGISTERS		16			// # of registers.
#define MAX_LOOPCOUNT	1000000		// Used to detect for possible infinite looping.
#define LINE_LENGTH		8			// Number of bytes to print on a line.

//------------------------------------------------------
// valid_ascii
//
// NOTE: This method was taken from the assembler.cpp file
// provided by Michael Zapp
// PURPOSE: Returns a valid ASCII character ('.' for
// characters that are not displayable)
// INPUT PARAMETERS:
// hex_value - the hex value of the char to be made valid
// OUTPUT PARAMETERS:
// hex_value - the converted char
//------------------------------------------------------
char valid_ascii( unsigned char hex_value )
{
	if ( hex_value < 0x21 || hex_value > 0x7e )
		hex_value = '.';

	return( (char)hex_value );
}

//------------------------------------------------------
// print_formatted_data
//
// NOTE: Parts of this method were taken from the
// assembler.cpp file provided by Michael Zapp
// PURPOSE: Takes the information in the data area and
// outputs it in hexadecimal and ASCII form
// INPUT PARAMETERS:
// data[DATA_SIZE] - the contents of the data memory
// length - how much of the data memory to print out
//------------------------------------------------------
void print_formatted_data( short data[DATA_SIZE], int length )
{
	int i, j, k;					// Loop counters.
	char the_text[LINE_LENGTH+1];	// ASCII character string.
	unsigned char current;			// Used for separating short data.
	
	// Print each line 1 at a time.
	for ( i=0 ; i<length*2 ; i+=LINE_LENGTH )
	{
		// Add 1 word at a time, but don't go beyond the and of the data.
		for ( j=0 ; j<LINE_LENGTH && (i+j)<length*2 ; j+=2 )
		{
			// Need to separate the short data into two chars.
			current = (unsigned char)(data[(i+j)/2] & 0x00ff);
			the_text[j] =  valid_ascii( current );
			current = (unsigned char)(data[(i+j)/2] >> 8);
			the_text[j+1] = valid_ascii( current );
			printf( "%04X ", (unsigned short)data[(i+j)/2] );
		}

		// Add in FFFF (invalid operation) to fill out the line.
		if ( (i+j) >= length*2 )
		{
			for ( k=j ; k<LINE_LENGTH ; k+=2 )
			{
				the_text[k] =  valid_ascii( 0xff );
				the_text[k+1] = valid_ascii( 0xff );
				printf( "FFFF " );
			}
		}

		the_text[LINE_LENGTH] = '\0';
		printf( "\t\'%s\'\n", the_text );
	}
}

//------------------------------------------------------
// run
//
// PURPOSE: Excecutes the machine code.  Simulates how
// a real CPU would do the operations. 
// INPUT PARAMETERS:
// code[CODE_SIZE] - the contents of the code memory
// data[DATA_SIZE] - the contents of the data memory
// reg[REGISTERS] - the 16 registers in the CPU
// *data_length - how much of the data area is being used
// by the program (passed by reference so it can be changed)
// OUTPUT PARAMETERS:
// exit_code - a code that details why the program stopped
// running
//------------------------------------------------------
int run ( unsigned short code[CODE_SIZE], short data[DATA_SIZE], unsigned short reg[REGISTERS], int *data_length )
{
	unsigned short	program_counter = 0x0000;	// PC register.
	int				loop_count = 0;				// To check for possible infinite loops.
	int				exit_code = SUCCESSFUL;		// Explains why the program quits.
	unsigned short	instruction;				// 16-bit instruction register.
	short			branch = NO_BRANCH;			// To check if a branch occurred.
	bool			store = false;				// To check if a store operation occurred.
	short			result;						// The data coming from the ALU.
	unsigned char	instr_high;					// Bits 1-3 of an instruction.
	unsigned char	instr_low;					// Bits 4-6 of an instruction.
	unsigned char	operand1;					// Bits 7-10 of an instruction.
	char			operand2;					// Bits 11-16 of an instruction.

	// Executes instructions. Basically represents the finite
	// state machine of this particular CPU. Follows the
	// four main steps of a CPU: fetch, decode, execute, write-back.
	while( exit_code == SUCCESSFUL && loop_count < MAX_LOOPCOUNT )
	{
		// FETCH the next instruction.
		instruction = code[program_counter];

		// DECODE the instruction.
		operand2 = instruction & 0x003f;
		// To ensure sign extension
		operand2 <<= 2;
		operand2 >>= 2;
		instruction >>= 6;
		operand1 = instruction & 0x000f;
		instruction >>= 4;
		instr_low = instruction & 0x0007;
		instruction >>= 3;
		instr_high = instruction;

		// EXECUTE the instruction.
		// The switch determines what kind of operation will be executed
		switch( instr_high )
		{
			// Arithmetic operations are checked for validity.
			// Then the ALU outputs the result.
			case ADD_OPCODE:
				if( instr_low == 0x00 )
					result = reg[operand1] + operand2;
				else if( instr_low == 0x01 )
				{
					operand2 >>= 2;
					result = reg[operand1] + reg[operand2];
				}
				else
					exit_code = ILLEGAL_OPCODE;
				break;
			case SUB_OPCODE:
				if( instr_low == 0x00 )
					result = reg[operand1] - operand2;
				else if( instr_low == 0x01 )
				{
					operand2 >>= 2;
					result = reg[operand1] - reg[operand2];
				}
				else
					exit_code = ILLEGAL_OPCODE;
				break;
			case AND_OPCODE:
				if( instr_low == 0x00 )
					result = reg[operand1] & operand2;
				else if( instr_low == 0x01 )
				{
					operand2 >>= 2;
					result = reg[operand1] & reg[operand2];
				}
				else
					exit_code = ILLEGAL_OPCODE;
				break;
			case OR_OPCODE:
				if( instr_low == 0x00 )
					result = reg[operand1] | operand2;
				else if( instr_low == 0x01 )
				{
					operand2 >>= 2;
					result = reg[operand1] | reg[operand2];
				}
				else
					exit_code = ILLEGAL_OPCODE;
				break;
			case XOR_OPCODE:
				if( instr_low == 0x00 )
					result = reg[operand1] ^ operand2;
				else if( instr_low == 0x01 )
				{
					operand2 >>= 2;
					result = reg[operand1] ^ reg[operand2];
				}
				else
					exit_code = ILLEGAL_OPCODE;
				break;

			// Move operations are checked for validity.
			// Loads from memory are checked for range.
			// Data from memory are outputted by the MDR.
			// Other data are outputted by registers or are
			// part of the instruction.
			case MOVE_OPCODE:
				if( instr_low & 0x01 == 0x01 )
					operand2 >>= 2;
				if( instr_low == 0x00 )
					result = operand2;
				else if( instr_low == 0x01 )
				{
					if( reg[operand2] >= DATA_SIZE - 1 )
						exit_code = OUT_OF_RANGE;
					else
						result = data[reg[operand2]];
				}
				else if( instr_low == 0x04 )
				{
					store = true;
					result = operand2;
				}
				else if( instr_low == 0x05 )
				{
					store = true;
					result = reg[operand2];
				}
				else
					exit_code = ILLEGAL_OPCODE;
				break;

			// Shift operations are checked for validity.
			// Then the ALU outputs the result.
			case SHIFT_OPCODE:
				if( instr_low == 0x00 )
					result = reg[operand1] >> 1;
				else if( instr_low == 0x01 )
					result = reg[operand1] << 1;
				else
					exit_code = ILLEGAL_OPCODE;
				break;

			// Branch operations are checked for validity.
			// The branch code is set to determine if the PC
			// needs to change during the writeback stage.
			// The ALU determines the new value for the PC
			// and outputs it.
			case BRANCH_OPCODE:
				if( instr_low == 0x00 && operand2 == 0x00 )
				{
					result = reg[operand1];
					branch = TRUE_BRANCH;
				}
				else if( instr_low == 0x01 )
				{
					if( reg[operand1] == reg[0] )
					{
						result = program_counter + operand2;
						branch = TRUE_BRANCH;
					}
					else
						branch = FALSE_BRANCH;
				}
				else if( instr_low == 0x02 )
				{
					if( reg[operand1] != reg[0] )
					{
						result = program_counter + operand2;
						branch = TRUE_BRANCH;
					}
					else
						branch = FALSE_BRANCH;
				}
				else if( instr_low == 0x03 )
				{
					if( reg[operand1] < reg[0] )
					{
						result = program_counter + operand2;
						branch = TRUE_BRANCH;
					}
					else
						branch = FALSE_BRANCH;
				}
				else if( instr_low == 0x04 )
				{
					if( reg[operand1] > reg[0] )
					{
						result = program_counter + operand2;
						branch = TRUE_BRANCH;
					}
					else
						branch = FALSE_BRANCH;
				}
				else if( instr_low == 0x05 )
				{
					if( reg[operand1] <= reg[0] )
					{
						result = program_counter + operand2;
						branch = TRUE_BRANCH;
					}
					else
						branch = FALSE_BRANCH;
				}
				else if( instr_low == 0x06 )
				{
					if( reg[operand1] >= reg[0] )
					{
						result = program_counter + operand2;
						branch = TRUE_BRANCH;
					}
					else
						branch = FALSE_BRANCH;
				}
				else
					exit_code = ILLEGAL_OPCODE;
				break;
			default:
				exit_code = ILLEGAL_OPCODE;
		}

		program_counter++;

		// WRITEBACK the results of execution.
		// First determine if an error was reached
		if( exit_code == ILLEGAL_OPCODE )
		{
			program_counter--;
		}
		// If a branch was taken, put the output in
		// the PC (if it is valid).
		else if ( branch == TRUE_BRANCH )
		{
			if( result < 0 || result >= CODE_SIZE - 1 )
				exit_code = OUT_OF_RANGE;
			else
				program_counter = result;
		}
		// Store operations put the output in data
		// memory, if valid.
		else if( store )
		{
			if( reg[operand1] >= DATA_SIZE - 1 )
				exit_code = OUT_OF_RANGE;
			else
			{
				data[reg[operand1]] = result;
				// Ensures the whole data area is
				// outputted on an error
				if( (reg[operand1] + 1) > *data_length )
					*data_length = reg[operand1] + 1;
			}
		}
		// Otherwise output is put into a register
		else if (branch == NO_BRANCH )
		{
			reg[operand1] = result;
		}

		loop_count++;
		branch = NO_BRANCH;
		store = false;
	}

	// Set the exit code
	if( exit_code == ILLEGAL_OPCODE )
		reg[0] = program_counter;
	else if( exit_code != OUT_OF_RANGE )
		exit_code = INFINITE_LOOP;

	return exit_code;
}

//------------------------------------------------------
// main
//
// PURPOSE: Does bootstrapping - reading in passed files
// and populating memory - and prints out an error
// message based on the output of the run method.
// INPUT PARAMETERS:
// argc - # of command line arguments (Not used)
// argv[] - array of strings containing the passed args
//------------------------------------------------------
int main(int argc, const char* argv[])
{

	int				data_length = 0;			// Size of the data area.
	int				i;							// Loop counter.
	char			code_input[CODE_SIZE*2];	// Temporarily stores code.
	unsigned short	code[CODE_SIZE];			// Code memory area.
	short			data[DATA_SIZE];			// Data memory area.
	char			file_input[ASCII_SIZE + 1];	// Temporarily stores data.
	int				file_size;					// # of chars in .o file.
	int				exit_code = SUCCESSFUL;		// Keeps track of errors.
	unsigned short	reg[REGISTERS];				// CPU Registers
	ifstream		object_file( argv[1], ios::binary | ios::in | ios::ate ); // .o file - 2nd argument.
	ifstream		data_file;					// Data file - 3rd argument.

	// *** Bootstrapping ***
	// Load code into code memory.
	if ( object_file.is_open() && strstr( argv[1], ".o") != NULL )
	{
		file_size = object_file.tellg();
		if ( file_size > CODE_SIZE*2 )
		{
			exit_code = CODE_OVERFLOW;
		}
		else
		{
			// Put contents of file into char string
			object_file.seekg( 0, ios::beg );
			object_file.read( code_input, file_size );

			// Put the char string into an array of shorts
			for( i=0 ; i<file_size ; i++ )
			{
				code[i] |= code_input[i*2];
				code[i] <<= 8;
				code[i] |= ( 0x00ff & code_input[(i*2)+1] );
			}

			// Fill in the rest of code memory with
			// invalid instructions.
			for( i=file_size/2 ; i<CODE_SIZE ; i++ )
			{
				code[i] = 0xffff;
			}
		}

		object_file.close();
	}

	// Initialize data memory
	for( i=0; i<DATA_SIZE ; i++ )
	{
		data[i] = 0xffff;
	}

	// Load data into data memory if available.
	if( argv[2] != NULL)
	{
		data_file.open( argv[2] );
		
		if( data_file.is_open() )
		{
			i = 0;
			while( data_file.peek() >= '0' )
			{
				data_file.get( file_input, ASCII_SIZE + 1 );
				data[i++] = (short)strtol( file_input, NULL, HEX );
				data_length++;
			}
			data_file.close();
		}
		else
			printf( "Warning: Data file does not exist.\n" );
	}
	else
		printf( "Warning:  No data file specified.\n" );

	// Initialize registers to 0x0000.
	for( i=0 ; i<REGISTERS ; i++)
	{
		reg[i] = 0x0000;
	}

	// *** Bootstrapping complete ***

	// Run the code if an error hasn't occured.
	if( exit_code == SUCCESSFUL )
	{
		exit_code = run( code, data, reg, &data_length );
	}

	// Print an error message based on the exit code.
	if( exit_code == SUCCESSFUL )
		printf("Error: Program completed successfully.  Clearly something is wrong.\n\n");
	else if( exit_code == CODE_OVERFLOW )
		printf("Error: Program to be run is too large for the CPU.\nFile size is %i bytes; maximum size is %d.\n\n", file_size, DATA_SIZE * WORD_SIZE);
	else if( exit_code == INFINITE_LOOP )
		printf("Possible infinite loop detected.  Ending program.\n\n");
	else if( exit_code == ILLEGAL_OPCODE )
		printf("Illegal instruction %04X detected at address %04X.\n\n",code[reg[0]],reg[0]);
	else if( exit_code == OUT_OF_RANGE )
		printf("Attempt to access invalid memory detected at address %04X.\n\n",reg[0]);

	print_formatted_data( data, data_length );
	
	printf( "\n*** End of processing ***\n" );

}
