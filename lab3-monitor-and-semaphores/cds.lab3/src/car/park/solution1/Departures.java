package car.park.solution1;

public class Departures extends Thread {

    CarParkControl cpc;

    public Departures(CarParkControl cpc, String name) {
        this.cpc = cpc;
        this.setName(name);
    }

    @Override
    public void run() {

        Long duration = (long) (Math.random() * 1000);
        duration = 1000 + 3*duration;
        try {
            System.out.println("Thread " + this.getName() + " will sleep for " + duration + ", then will simulate departure ");
            this.sleep(3*duration);
            System.out.println("Thread " + this.getName() + " departing ");
            cpc.depart();
            System.out.println("Thread " + this.getName() + " left ");
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }

    }

}
