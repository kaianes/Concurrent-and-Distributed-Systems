package balls;

import java.awt.Color;
import javax.swing.*;

public class BallsMainFrame {

    public static void nap(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //
            //  Print out the name of the tread that caused this.
            //
            System.err.println("Thread " + Thread.currentThread().getName()
                    + " throwed exception " + e.getMessage());
        }
    }

    public static void main(String[] a) {

        final BallsPanel ballsPanel = new BallsPanel();
        final JFrame win = new JFrame("Bouncing Balls ");
        JPanel mainPanel = new JPanel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JButton pauseButton = new JButton("Pause");
                JButton restartButton = new JButton("Restart");
                JButton killButton = new JButton("Kill one");

                mainPanel.add(ballsPanel);
                mainPanel.add(pauseButton);
                mainPanel.add(restartButton);                
                mainPanel.add(killButton);

                win.getContentPane().add(mainPanel);

                win.pack();
                win.setVisible(true);
            }
        });

        Thread.currentThread().setName("MyMainThread");

        nap((int) (1000 * Math.random()));
        new Ball(ballsPanel, 50, 80, 5, 10, Color.red).start();
        nap((int) (3000 * Math.random()));
        new Ball(ballsPanel, 70, 100, 8, 6, Color.blue).start();
        nap((int) (3000 * Math.random()));
        new Ball(ballsPanel, 150, 100, 9, 7, Color.green).start();
        nap((int) (3000 * Math.random()));
        new Ball(ballsPanel, 200, 130, 3, 8, Color.black).start();
        nap((int) (3000 * Math.random()));
    }
}
