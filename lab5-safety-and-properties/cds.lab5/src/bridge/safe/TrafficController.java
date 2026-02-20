package bridge.safe;

public class TrafficController {

    private final static int MIN_TURN = 5;
    private final static int LEFT = 0;
    private final static int RIGHT = 1;

    private int onBridge = 0;
    private boolean redWaitingLeft = false;
    private boolean blueWaitingRight = false;
    private int direction = LEFT;
    private int turnCount = 0;
    private boolean changingDirection = false;


    public synchronized void redEnterLeft() {
	if ((!blueWaitingRight) && onBridge == 0) {
	    direction = RIGHT;
	    turnCount = 0;
	} else {
	    while ((turnCount >= MIN_TURN && blueWaitingRight) || 
		   direction == LEFT || changingDirection) {
		changingDirection = turnCount >= MIN_TURN;
		redWaitingLeft = true;
		try {wait();} catch (InterruptedException e) {}
		redWaitingLeft = false;
	    }
	}
	onBridge++;
	turnCount++;
    }

    public synchronized void blueEnterRight() {
	if ((!redWaitingLeft) && onBridge == 0) {
	    direction = LEFT;
	    turnCount = 0;
	} else {
	    while ((turnCount >= MIN_TURN && redWaitingLeft) || 
		   direction == RIGHT || changingDirection) {
		changingDirection = turnCount >= MIN_TURN;
		blueWaitingRight = true;
		try {wait();} catch (InterruptedException e) {}
		blueWaitingRight = false;
	    }
	}
	onBridge++;
	turnCount++;
    }

     public synchronized void blueLeaveLeft() {
	 onBridge--;
	 if (onBridge==0 && redWaitingLeft) {
	     turnCount = 0;
	     direction = RIGHT;
	     changingDirection = false;
	     notifyAll();
	 }
    }

    public synchronized void redLeaveRight() {
	 onBridge--;
	 if (onBridge==0 && blueWaitingRight) {
	     turnCount = 0;
	     direction = LEFT;
	     changingDirection = false;
	     notifyAll();
	 }

    }

}