package car.park.solution1;

public class CarParkControl {

    private int spaces;
    private int capacity;

    CarParkControl(int n) {
        capacity = spaces = n;
    }

    synchronized void arrive() throws InterruptedException {
        while (spaces == 0) {
            wait();
        }
        --spaces;
        notifyAll();
    }

    synchronized void depart() throws InterruptedException {
        while (spaces == capacity) {
            wait();
        }
        ++spaces;
        notifyAll();
    }

    public int getCapacity(){
        return capacity;
    }
}
