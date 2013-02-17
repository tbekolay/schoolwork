//-----------------------------------------
// CLASS: FileHandler
//
// REMARKS: A class that handles all file-related operations;
//			most notably encode and decode.
//
// INPUT: A string filename
//
// OUTPUT: Outputs encoded or decoded text (or taz) files.
//
//-----------------------------------------
#ifndef FHANDLER_H
#define FHANDLER_H

#include <string>
#include "FreqList.h"
#include "HuffmanTree.h"

using namespace std;

class FileHandler
{

	private:
		string filename;
		HuffmanTree *tree;
		FreqList *freq;

	public:

		void getFreq();
		void parseFreq();
		void buildHuff();
		void encode();
		void decode();
		FileHandler(const string newFile);
		~FileHandler();

};

#endif
