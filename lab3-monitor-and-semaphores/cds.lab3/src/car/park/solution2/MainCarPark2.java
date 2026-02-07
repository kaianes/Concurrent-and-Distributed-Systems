package car.park.solution2;

public class MainCarPark2 {

    public static void main(String args[]) {

        int spaces = 5;
        CarParkControl cpc = new CarParkControl(spaces);

        Thread carArray[] = new Thread[2 * spaces];
        for (int i = 0; i < carArray.length; i++) {
            carArray[i] = new Thread(new MyCar(cpc, "Car " + (i + 1)));
            carArray[i].start();
        }
        for (int i = 0; i < carArray.length; i++) {
            try {
                carArray[i].join();
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }

    }

}
