//--------------------------
// File Structure:
// int n(# of frequency objects)
// n frequency objects
// int n(# of characters)
// block of unsigned chars representing the body of the text
//--------------------------

#include <iostream>
#include <fstream>
#include <string>
#include "FileHandler.h"
#include "PriorityQueue.h"
#define RIGHT_ONE 0x01
#define LEFT_ONE 0x80

//------------------------------------------------------
// getFreq
//
// PURPOSE: Builds the FreqList that is part of FileHandler by
//			parsing the ASCII text associated with this FileHandler,
//			determining the frequency of each character in the text file.
// EXTERNAL REFERENCES:  Requires FreqList class.
//------------------------------------------------------

void FileHandler::getFreq()
{

	freq = new FreqList; //Set aside space for freq
	string input = ""; //The word to be added
	ifstream fin(filename.c_str()); //Open file for input

	//This looop gets every character in the file and inserts
	//it into the FreqList.
	while(input[0] != EOF)
	{
		input = fin.get();
		freq->insert(input);
	}

	//Close the file
	fin.close();

}

//------------------------------------------------------
// parseFreq
//
// PURPOSE: Builds the FreqList that is part of FileHandler by
//			extracting the Frequency data in the Binary encoded .taz file
//			based on the previously explained file structure.
// EXTERNAL REFERENCES:  Requires FreqList, Frequency class
//------------------------------------------------------

void FileHandler::parseFreq()
{

	freq = new FreqList; //Set aside space for freq
	ifstream fin(filename.c_str(), ios::binary); //Open binary file for input
	Frequency *temp; //Temporary
	int tempFreq; //Temporary
	char tempWord; //Temporary
	string newWord; //Temporary
	int length; //The number of Frequency objects to read in
	int i; //Counter

	//Get the number of Frequency objects from the file
	fin.read((char*)(&length), sizeof(length));

	//Read in and build a FreqList of size 'length'
	for(i = 0; i < length; i++)
	{
		fin.read((char*)(&tempFreq), sizeof(tempFreq));
		fin.read((char*)(&tempWord), sizeof(tempWord));
		newWord = tempWord;
		temp = new Frequency(tempFreq, newWord);
		freq->insert(temp);
	}

	//Close the file
	fin.close();

}

//------------------------------------------------------
// buildHuff
//
// PURPOSE: Builds the HuffmanTree that is part of FileHandler by
//			first placing each Frequency data in a PriorityQueue then
//			merging until there's only one HuffmanTree left.
// EXTERNAL REFERENCES:  Requires PriorityQueue, FreqList, HuffmanTree classes.
//------------------------------------------------------

void FileHandler::buildHuff()
{

	PriorityQueue *queue = new PriorityQueue; //The Queue of HuffmanTrees
	FreqList *temp = new FreqList(freq); //Copy the FreqList as we need it later
	HuffmanTree *temp1; //Temporary
	HuffmanTree *temp2; //Temporary

	//Fill the queue with HuffmanTrees with only one element
	while( !temp->isEmpty() )
		queue->enter( new HuffmanTree(temp->leave()) );

	//Merge the two lowest HuffmanTrees, then add that merged tree back into
	//the priority queue; continue until there is only one HuffmanTree left.
	while( !queue->isOneLeft() )
	{
		temp1 = queue->leave();
		temp2 = queue->leave();
		queue->enter( temp1->merge( temp2 ) );
	}

	//That tree becomes associated with the FileHandler object.
	tree = queue->leave();

}

//------------------------------------------------------
// encode
//
// PURPOSE: Writes a compressed binary .taz file to (filename + ".taz"), based on
//			the FreqList and HuffmanTree in the FileHandler object.
// EXTERNAL REFERENCES:  Requires ListFNode, Frequency, HuffNode, HuffmanTree classes.
//------------------------------------------------------

void FileHandler::encode()
{

	ifstream fin(filename.c_str()); //Input ASCII file
	ofstream fout( (filename + ".taz").c_str(), ios::binary); //Output Binary File
	ListFNode *frequency = freq->top->link; //The current frequency node
	Frequency *temp; //Temporary
	int tempFreq; //Temporary

	char tempWord; //Temporary
	HuffNode *curr = tree->top; //The current HuffNode

	string input; //The character to be encoded
	int count = 0; //A counter
	int length; //Various lengths
	int i; //Counter
	unsigned char output; //The char to be written to the binary file

	length = freq->length(); //Find how many elements in freq
	fout.write((char*)(&length), sizeof(length)); //Write to file

	//Writes out all of the frequency objects in freq.
	for(i = 0; i < length; i++)
	{
		temp = frequency->data;
		tempFreq = temp->freq;
		tempWord = temp->word[0];
		fout.write((char*)(&tempFreq), sizeof(tempFreq));
		fout.write((char*)(&tempWord), sizeof(tempWord));
		frequency = frequency->link;
	}

	//Find the number of characters in the input file
	fin.seekg(0, ios::end);
	length = fin.tellg();
	fin.seekg(0, ios::beg);

	//Output that to the compressed file
	fout.write((char*)(&length), sizeof(int));

	input = fin.get();

	//The main loop: outputs the body of the text to the compressed file
	//Runs until the EOF
	while(input[0] != EOF)
	{
		//Determine whether you wish to go right or left based on the split
		i = curr->data.find(input);

		//If we go left, output nothing (0)
		if( i < curr->split )
		{
			curr = curr->left;
		}
		//If we go right, output 0x01
		else
		{
			curr = curr->right;
			output |= RIGHT_ONE;
		}
		count++;

		//If we have filled the output character, output it to the file
		if(count == 8)
		{
			fout.write((char*)(&output), sizeof(output));
			count = 0; //Then reset the count
		}

		//Shift the output character
		output <<= 1;

		//If we are now at a leaf node, get the next character from the file
		if( curr->isLeafNode() )
		{
			input = fin.get();
			curr = tree->top;
		}

	}


	//Shift the output so that we do not repeat bits
	output <<= (7 - count);
	//Write the final output
	fout.write((char*)(&output), sizeof(output));

	//Close the files.
	fout.close();
	fin.close();

}

//------------------------------------------------------
// decode
//
// PURPOSE: Writes an ASCII text file based on the binary .taz file
//			associated with the FileHandler object.
// EXTERNAL REFERENCES:  Requires HuffNode, HuffmanTree, FreqList classes
//------------------------------------------------------

void FileHandler::decode()
{

	ifstream fin(filename.c_str(), ios::binary); //Input Binary file
	ofstream fout; //Output ASCII file
	HuffNode *curr = tree->top; //Current HuffNode (for traversing the HuffmanTree)
	int position; //A certain position in the input file
	char *buffer; //Will contain the body of the binary file
	string newFile; //The filename of the output file
	int length; //Various lengths
	int binlength; //The number of characters in the binary file

	int i; //Counter
	int j = 0; //Counter
	int count = 0; //Counter
	int charcount = 0; //Counter
	unsigned char current; //Output

	//Get the number of frequency objects in freq
	length = freq->length();

	position = sizeof(int) + ( (sizeof(int) + sizeof(char)) * length );


	//Skip past the Frequency objects we've already read in
	fin.seekg(position);
	//Get the number of characters that should be in the output file
	fin.read((char*)(&length), sizeof(length));
	position += sizeof(int);

	//Get the number of binary characters in the input file
	fin.seekg(0, ios::end);
	binlength = fin.tellg();
	binlength -= position;
	//Return to the correct position
	fin.seekg(position);

	//Read in the body of the binary file
	buffer = new char[binlength];
	fin.read(buffer, binlength);

	//Close the binary input file
	fin.close();

	//Determine the new filename and open it for output
	i = filename.find(".");
	newFile = filename.substr(0,i);
	newFile += "U.txt";
	fout.open(newFile.c_str());

	current = (unsigned char)buffer[j++];

	//This loop iterates until we have outputted the same number of
	//characters that were in the original inputted ASCII text file
	while(charcount < length)
	{

		//Determine the top bit
		i = (current & LEFT_ONE);

		//Go right or left depending on it
		if( i == 0 )
		{
			curr = curr->left;
		}
		else
		{
			curr = curr->right;
		}

		//Shift the current char
		current <<= 1;
		count++;

		//If we have read in all the bits in the char,
		//go to the next one
		if(count == 8)
		{
			current = (unsigned char)buffer[j++];
			count = 0;
		}

		//If we're currently at a leaf node, output the
		//ASCII value at that node to the ASCII output file
		if( curr->isLeafNode() )
		{
			fout.put(curr->data[0]);
			charcount++;
			curr = tree->top;
		}

	}

	//Close the output file.
	fout.close();

}

//------------------------------------------------------
// FileHandler
//
// PURPOSE: FileHandler constructor; self-explanatory
//------------------------------------------------------


FileHandler::FileHandler(const string newFile) : filename(newFile)
{}

//------------------------------------------------------
// ~FileHandler
//
// PURPOSE: FileHandler destructor
//------------------------------------------------------

FileHandler::~FileHandler()
{
	delete tree;
	delete freq;
}
