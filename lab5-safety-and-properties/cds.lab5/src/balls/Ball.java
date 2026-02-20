package balls;

import java.awt.*;

public class Ball extends Thread {

    BallsPanel panel;
    
    private int xpos, ypos, xinc, yinc;

    private final Color col;

    private final static int ballw = 20;
    private final static int ballh = 20;
    
    public Ball(BallsPanel panel, int xpos, int ypos, 
		int xinc, int yinc, Color col) {
	//
	// Assign a name to this thread for easier debugging
	//
	super("Ball :"+col);

	this.panel = panel;
      	this.xpos = xpos; this.ypos = ypos;
	this.xinc = xinc; this.yinc = yinc;
        this.col = col;

        panel.addBall(this);
    }
    
    public void run() {
	while (true)
	    move();
    }
   
    public void move() {
	if (xpos >= panel.getWidth() - ballw || xpos <= 0 ) xinc = -xinc;
	
	if (ypos >= panel.getHeight() - ballh || ypos <= 0 ) yinc = -yinc;

	BallsMainFrame.nap(30);
	doMove();
	panel.repaint();
    }
     
    //
    // SYNC: This modifies xpos and ypos.
    //
    public synchronized void doMove() {
	xpos += xinc;
	ypos += yinc;
    }

    //
    // SYNC: This is accessed from the GUI thread, and
    //       it reads xpos and ypos.
    //
    public synchronized void draw(Graphics g) {
	g.setColor(col);
	g.fillOval(xpos,ypos,ballw,ballh);
    }
}
