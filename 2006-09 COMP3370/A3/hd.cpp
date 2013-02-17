#include <stdio.h>
#include <fstream>
#include <string>
#include <vector>
#include <math.h>

using namespace std;

#define HD_SIZE       4096
#define WORD_SIZE	  2

// hard-code in the filename of the binary file we will use for the harddrive
#define HD_FILENAME   "hard_drive"

// hard drive instantiation
static unsigned char hd[HD_SIZE][WORD_SIZE];

int main(int argc, const char * argv[])
{
	FILE           *hd_file = NULL;

	hd_file = fopen( HD_FILENAME, "r");

    // since we're allowing anything to be specified, make sure it's a file...
    if ( hd_file )
    {
    	fread( hd, 1, HD_SIZE*WORD_SIZE, hd_file );

    	fclose( hd_file );
	}

	printf("Hard drive contents:\n%02x%02x",hd[0][0],hd[0][1]);

}
