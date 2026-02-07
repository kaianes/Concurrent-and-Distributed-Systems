package car.park.solution2;

public class MyCar extends Thread {

    CarParkControl cpc;

    public MyCar(CarParkControl cpc, String name) {
        this.setName(name);
        this.cpc = cpc;
    }

    @Override
    public void run() {
        for (int i = 0; i < cpc.getCapacity() * 2; i++) {
            Long duration = (long) (Math.random() * 1000);            
            try {
                System.out.println(this.getName() + " is arriving and trying to park, there are " + cpc.getSpaces() +  " free spaces");
                cpc.arrive();
                System.out.println(this.getName() + " parked, there are " + cpc.getSpaces() + " spaces, car remains parked for " + duration);
                this.sleep(duration);
                System.out.println(this.getName() + " is leaving now, there are " + cpc.getSpaces() +  " free spaces");
                cpc.depart();
                System.out.println(this.getName() + " left, there are " + cpc.getSpaces() +  " free spaces");
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }

        }

    }

}
