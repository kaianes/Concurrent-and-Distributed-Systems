package grocery.shop;

public class Client extends Thread {

    private final Tills tills;

    public Client(Tills tills) {
        this.tills = tills;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " going to the tills ");
        tills.goToTill();
        System.out.println(Thread.currentThread().getName() + " finished! ");
    }
}
