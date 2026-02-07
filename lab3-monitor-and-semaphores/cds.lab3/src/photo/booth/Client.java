package photo.booth;

public class Client extends Thread {

    private final PhotoBooth photoBooth;

    public Client(PhotoBooth photoBooth) {
        this.photoBooth = photoBooth;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started, going to the photo booth ");
        photoBooth.goToBooth();
        System.out.println(Thread.currentThread().getName() + " finished! ");
    }
}
