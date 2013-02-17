 //-----------------------------------------
 // NAME		: Trevor Bekolay
 // STUDENT NUMBER	: 6796723
 // COURSE		: 074.215
 // SECTION		: L02
 // INSTRUCTOR	: Ryan Wegner
 // ASSIGNMENT	: 3
 //
 // REMARKS: This is a file compression utility, for text files.
 //			 It will compress text files (better compression ratios
 //			 with larger files) and uncompress the results.
 //
 // INPUT: Uncompressed ASCII text files and compressed .taz files.
 //
 // OUTPUT: Compressed .taz files and uncompressed ASCII text files (respectively).
 //
 //-----------------------------------------

#include <iostream>
#include "FileHandler.h"

int main(int argc, char* argv[])
{

	bool uncompress = false; // Is -u argument passed?
	string filename; // Filename to be opened
	FileHandler *file; // FileHandler object, handles input/output
	int ind; // Index for command line scanning

	// Scan command line
	for (ind = 1; ind < argc; ind++)
	{
	// Check for a -u
		if (argv[ind][0] == '-')
		{
			switch (argv[ind][1])
			{
				// If -u, set uncompress flag
				case 'u': uncompress = true;
                	break;
       			default : cout << "Unknown switch " << argv[ind][1] << endl;

      		}//end switch
    	}//end if

		else
		{
	        // This should be the filename
	        filename = argv[ind];
        }//end else

	}//end for

	// Make sure a valid filename was read in
	if(filename == "")
	{
		cout << "Error!  Please pass the filename as an argument." << endl;
		return 0;
	}//end if

	// Open the file for input/output
	file = new FileHandler(filename);

	// If -u flag not set, compress the file
	if(!uncompress)
	{
		cout << "Compressing " << filename << " . . . " << endl;
		file->getFreq();
		file->buildHuff();
		file->encode();
		cout << "Compression completed!" << endl;
	}//end if
	// If -u flag set, uncompress the file.
	else
	{
		cout << "Uncompressing " << filename << " . . . " << endl;
		file->parseFreq();
		file->buildHuff();
		file->decode();
		cout << "Uncompression completed!" << endl;
	}//end else

	return 0;

}
