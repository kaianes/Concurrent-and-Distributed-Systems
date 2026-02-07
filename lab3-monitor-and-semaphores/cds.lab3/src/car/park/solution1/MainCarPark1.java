package car.park.solution1;

public class MainCarPark1 {

    public static void main(String args[]) {

        int spaces = 5;
        CarParkControl cpc = new CarParkControl(spaces);
        Thread arrayArrivals[] = new Thread[2 * spaces];
        Thread arrayDepartures[] = new Thread[2 * spaces];
        
        for (int i = 0; i < arrayArrivals.length; i++) {
            arrayArrivals[i] = new Thread(new Arrivals(cpc, "Arrival Car " + (i + 1)));
            arrayArrivals[i].start();
        }
        for (int i = 0; i < arrayDepartures.length; i++) {
            arrayDepartures[i] = new Thread(new Departures(cpc, "Departure Car " + (i + 1)));
            arrayDepartures[i].start();
        }
        try {
            for (int i = 0; i < arrayDepartures.length; i++) {
                arrayArrivals[i].join();
                arrayDepartures[i].join();
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }

    }

}
