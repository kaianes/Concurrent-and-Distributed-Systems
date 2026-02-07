package bike.rental;

public class BikesRentalMain {

    public static void main(String args[]) {
        
        int noThreads = 10;
        int noBikes = 4;
        BikeRental bikeRental = new BikeRental(noBikes);

        Thread thread[] = new Thread[noThreads];
        for (int i = 0; i < noThreads; i++) {
            thread[i] = new Thread(new Client(bikeRental), "Thread " + i);
        }

        for (int i = 0; i < noThreads; i++) {
            thread[i].start();
        }
    }

}
