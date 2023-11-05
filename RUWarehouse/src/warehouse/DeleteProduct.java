package warehouse;

/*
 * Use this class to test the deleteProduct method.
 */ 
public class DeleteProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

	
        Warehouse warehouse = new Warehouse();

	    int numberOfQueries = StdIn.readInt();

        for(int i = 0; i < numberOfQueries; i++)
        {
            if(StdIn.readString().equals("add"))
            {
                //System.out.println("ADD");
                
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                String name = StdIn.readString();
                int stock = StdIn.readInt();
                int demand = StdIn.readInt();
 
                warehouse.addProduct(id, name, stock, day, demand);
            }
            else 
            {
                //System.out.println("DELETE");
                
                int id = StdIn.readInt(); 
                

                warehouse.deleteProduct(id);
            }
            
        }

        StdOut.println(warehouse);
    }
}
