package grocery.shop;

public class GroceryShopMain {

    public static void main(String args[]) {

        int noThreads = 10;
        int noTills = 4;
        Tills tills = new Tills(noTills, true);

        Thread clientsArray[] = new Thread[noThreads];

        for (int i = 0; i < clientsArray.length; i++) {
            clientsArray[i] = new Thread(new Client(tills));
            clientsArray[i].start();
        }
        
    }
}
