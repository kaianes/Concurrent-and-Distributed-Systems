package bike.rental;

public class Client implements Runnable {

    private final BikeRental bikeRental;

    public Client(BikeRental bikeRental) {
        this.bikeRental = bikeRental;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " going rent a bike");
        bikeRental.getBike();
        System.out.println(Thread.currentThread().getName() + " finished! ");
    }

}
