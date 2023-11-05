package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        sortedCharFreqList = new ArrayList<CharFreq>();

        int[] array = new int[128];

        int totalChars = 0;
        while(StdIn.hasNextChar())
        {  
            char c = StdIn.readChar();

            array[c] += 1;
            totalChars ++; 

        }

        int currentIndexValue = 0;
        int numberOfDistinctChars = 0;
        for(int i = 0; i < 128; i++)
        {
            if(array[i] != 0)
            {
                numberOfDistinctChars++;
                //System.out.println((char)i + " " + array[i]);
                CharFreq charFreq = new CharFreq((char) i, array[i]/(double)totalChars);
                currentIndexValue = i; 
                sortedCharFreqList.add(charFreq);
            }
        }
        
        if(numberOfDistinctChars == 1)
        {

            if(currentIndexValue == 127)
                currentIndexValue = -1;
                
            CharFreq charFreq = new CharFreq((char)(currentIndexValue + 1), 0);
            sortedCharFreqList.add(charFreq);
        }

        Collections.sort(sortedCharFreqList);
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

        Queue sourceQueue = new Queue<>();
        Queue targetQueue = new Queue<>();

        TreeNode targetTreeNode = new TreeNode(); //a temp node to replace the huffman root

        // this will fill the sourceQueue with the arrayList 
        for(int i = 0; i < sortedCharFreqList.size(); i++)
        {
            TreeNode node = new TreeNode(sortedCharFreqList.get(i), null, null);

            sourceQueue.enqueue(node);
        }
        

        //while sourceQueue is not empty or while targetQueue.size() != 1
        while(!sourceQueue.isEmpty() || targetQueue.size() != 1)
        {

            // stores the lowerNodes needed
            TreeNode firstLowerNode = new TreeNode();
            TreeNode secondLowerNode = new TreeNode();

            for(int i = 0; i < 2; i++)
            {
                
                if(targetQueue.isEmpty()) //if targetQueue is ever empty
                {
                    if(i == 0)
                        firstLowerNode = (TreeNode) sourceQueue.dequeue();
                    else
                        secondLowerNode = (TreeNode) sourceQueue.dequeue();
        
                }
                else if(sourceQueue.isEmpty()) //if sourceQueue is empty which it should be at the end
                {
                    if(i == 0)
                        firstLowerNode = (TreeNode) targetQueue.dequeue();
                    else
                        secondLowerNode = (TreeNode) targetQueue.dequeue();
    
                }
                
                else
                {
                    
                    TreeNode peekSourceNode = (TreeNode) sourceQueue.peek();
                    TreeNode peekTargetNode = (TreeNode) targetQueue.peek();

                    if(peekTargetNode.getData().getProbOcc() < peekSourceNode.getData().getProbOcc())
                    {
                        if(i == 0)
                            firstLowerNode = (TreeNode) targetQueue.dequeue(); 
                        else
                            secondLowerNode = (TreeNode) targetQueue.dequeue();
                    
                    }
    
                    else 
                    {
                        if(i == 0)
                            firstLowerNode = (TreeNode) sourceQueue.dequeue();
                        else
                            secondLowerNode = (TreeNode) sourceQueue.dequeue(); 
                    
                    }
                }
                
            }
            
            
            CharFreq parentCharFreq = new CharFreq(null, firstLowerNode.getData().getProbOcc() + secondLowerNode.getData().getProbOcc());
            targetTreeNode = new TreeNode(parentCharFreq, firstLowerNode, secondLowerNode);
            targetQueue.enqueue(targetTreeNode);
        }

        huffmanRoot = targetTreeNode;  


    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {

        encodings = new String[128];
        setEncodingsHelper(huffmanRoot, encodings, ""); 
    }

    
    private void setEncodingsHelper(TreeNode huffmanRoot, String[] encodings, String bitcode)
    {
        if(huffmanRoot == null)
            return;

        if(huffmanRoot.getLeft() == null && huffmanRoot.getRight() == null)
        {
            encodings[huffmanRoot.getData().getCharacter()] = bitcode;
        }

        setEncodingsHelper(huffmanRoot.getLeft(), encodings, bitcode + "0");
        setEncodingsHelper(huffmanRoot.getRight(), encodings, bitcode + "1");
    }
    

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

        String bitString = "";

        while(StdIn.hasNextChar())
        {
            char c = StdIn.readChar();
            bitString += encodings[c];
        }

        writeBitString(encodedFile, bitString);

	/* Your code goes here */
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        String string = readBitString(encodedFile);
        //System.out.println(string);

        TreeNode tempNode = huffmanRoot;

        for(int i = 0; i < string.length(); i++)
        {
            
            if(string.charAt(i) == '0')
            {
               tempNode = tempNode.getLeft(); 
            }
            else
            {
                tempNode = tempNode.getRight();
            }
            
            if(tempNode.getLeft() == null && tempNode.getRight() == null)
            {
                StdOut.print(tempNode.getData().getCharacter());
                tempNode = huffmanRoot;
                 

            }
            
        }





	/* Your code goes here */
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
