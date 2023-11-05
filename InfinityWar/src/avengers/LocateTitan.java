package avengers;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
    	
        if (args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

    	// WRITE YOUR CODE HERE

        String locateTitanInputFile = args[0];
        String locateTitanOutputFile = args[1];

        StdIn.setFile(locateTitanInputFile);
        int arraySize = StdIn.readInt();

        double[] functionalityArray = new double[arraySize];
        for(int i = 0; i < functionalityArray.length; i++)
        {
            
            functionalityArray[StdIn.readInt()] = StdIn.readDouble();
        }


        int[][] adjacencyMatrix = new int[arraySize][arraySize];
        for(int i = 0; i < arraySize; i++)
        {
            for(int j = 0; j < arraySize; j++)
            {
                adjacencyMatrix[i][j] = StdIn.readInt();
            }
        }
        
        for(int i = 0; i < adjacencyMatrix.length; i ++)
        {
            for(int j = 0; j < adjacencyMatrix[i].length; j++)
            {
                adjacencyMatrix[i][j] = (int) (adjacencyMatrix[i][j]/(functionalityArray[i] * functionalityArray[j]));
            }
        }

        /*
        Just checking to make sure I completed step two correctly

        for(int i = 0; i < adjacencyMatrix.length; i ++)
        {
            for(int j = 0; j < adjacencyMatrix[i].length; j++)
            {
                System.out.print(adjacencyMatrix[i][j] + "\t");
            }
            System.out.println();
        }
         */
        
        //Step Three
        int[] minCost = new int[arraySize];

        boolean[] dijkstraSet = new boolean[arraySize];
        
        for(int i = 0; i < minCost.length; i ++)
        {
            if(i == 0)
                minCost[i] = 0;
            else
                minCost[i] = Integer.MAX_VALUE;

        }

        

        for(int i = 0; i < arraySize - 1; i++)
        {
            int currentSource = getMinCostNode(minCost, dijkstraSet); 
            //System.out.println(currentSource);
            
            dijkstraSet[currentSource] = true; 

            for(int w = 0; w < arraySize; w++)
            {
                if(adjacencyMatrix[currentSource][w] != 0)
                {
                    if(dijkstraSet[w] == false && minCost[currentSource] != Integer.MAX_VALUE && minCost[w] > minCost[currentSource] + adjacencyMatrix[currentSource][w])
                    {
                        minCost[w] = minCost[currentSource] + adjacencyMatrix[currentSource][w];
                        

                    }

                }

                for(int j = 0; j < minCost.length; j ++)
                {
                    System.out.print(minCost[j] + " ");
                }
                System.out.println();
            }
        }

        
        StdOut.setFile(locateTitanOutputFile);
        StdOut.print(minCost[arraySize - 1]);



    }

    public static int getMinCostNode(int[] minCost, boolean[] dijkstraSet)
    {
        int currentMin = Integer.MAX_VALUE;
        int minCostNode = 0;

        for(int i = 0; i < minCost.length; i ++)
        {
            if(dijkstraSet[i] == false)
            {
                if(minCost[i] < currentMin)
                {
                    currentMin = minCost[i]; 
                    minCostNode = i; 
                }
            }
        }


        return minCostNode;
    }
}
