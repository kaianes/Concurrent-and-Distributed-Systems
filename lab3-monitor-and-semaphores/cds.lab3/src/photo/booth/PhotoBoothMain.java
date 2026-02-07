package photo.booth;

public class PhotoBoothMain {

    public static void main(String args[]) {

        int noThreads = 5;
        int noBooths = 1;
        PhotoBooth tills = new PhotoBooth(noBooths, false);

        Thread clientsArray[] = new Thread[noThreads];

        for (int i = 0; i < clientsArray.length; i++) {
            clientsArray[i] = new Thread(new Client(tills));
            clientsArray[i].start();
        }
        
    }
}
