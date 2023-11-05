package warehouse;

public class Restock {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);


        Warehouse warehouse = new Warehouse();

	    int numberOfQueries = StdIn.readInt();

        for(int i = 0; i < numberOfQueries; i++)
        {
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
            else 
            {
                //System.out.println("RESTOCK");
                
                int id = StdIn.readInt(); 
                int amount = StdIn.readInt();

                warehouse.restockProduct(id, amount);
            }
            
        }

        StdOut.println(warehouse);
    }
}
