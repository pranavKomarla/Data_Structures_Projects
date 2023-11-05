package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

	// Use this file to test all methods

        Warehouse warehouse = new Warehouse();

	    int numberOfQueries = StdIn.readInt();

        for(int i = 0; i < numberOfQueries; i++)
        {
            //System.out.println(i);
            String process = StdIn.readString();
            if(process.equals("add"))
            {
                //System.out.println("ADD");
                
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                String name = StdIn.readString();
                int stock = StdIn.readInt();
                int demand = StdIn.readInt();
 
                warehouse.addProduct(id, name, stock, day, demand);
            }
            else if(process.equals("restock"))
            {
                //System.out.println("RESTOCK");

                int id = StdIn.readInt(); 
                int amount = StdIn.readInt();

                warehouse.restockProduct(id, amount);
            }
            else if(process.equals("delete"))
            {
                //System.out.println("DELETE");
                int id = StdIn.readInt(); 
                
                warehouse.deleteProduct(id);
            }
            else  
            {
                //System.out.println("PURCHASE");
                
                int day = StdIn.readInt();
                int id = StdIn.readInt(); 
                int amount = StdIn.readInt(); 
                

                warehouse.purchaseProduct(id, day, amount);
            }
            
            
            
        }

        StdOut.println(warehouse); 
    }
}
