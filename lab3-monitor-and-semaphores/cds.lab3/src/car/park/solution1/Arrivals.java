package car.park.solution1;

public class Arrivals extends Thread {

    CarParkControl cpc;

    public Arrivals(CarParkControl cpc, String name) {
        this.cpc = cpc;
        this.setName(name);
    }

    @Override
    public void run() {

        Long duration = (long) (Math.random() * 1000);
        try {
            System.out.println("Thread " + this.getName() + " will sleep for " + duration + ", then will simulate arrival ");
            this.sleep(duration);
            System.out.println("Thread " + this.getName() + " arriving ");
            cpc.arrive();
            System.out.println("Thread " + this.getName() + " parked ");
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }

    }

}
