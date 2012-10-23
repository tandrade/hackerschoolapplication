/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** Y-coordinate for the box */ 	
	private static final int BOX_HEIGHT = HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
	
	private static final int BALL_X_START = WIDTH/2 - BALL_RADIUS;
	private static final int BALL_Y_START = BOX_HEIGHT - 150; 
	private static final int X_BARRIER_LEFT = 2;
	private static final int X_BARRIER_RIGHT = WIDTH - 2*BALL_RADIUS; 
	private static final int Y_BARRIER_UP = 2*BALL_RADIUS; 
	private static final int Y_BARRIER_DOWN = HEIGHT - 4*BALL_RADIUS; 
	private static final int VEL = 1;
	private static final int N_COLOR_ROWS = NBRICK_ROWS/5;
	private static final int TIME = 5; 
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		i = 1; 
		addMouseListeners();
		buildRows(getWidth()/2 + BRICK_SEP/2, BRICK_Y_OFFSET);
		buildBar((getWidth() - PADDLE_WIDTH)/2, BOX_HEIGHT);
		buildBall(BALL_X_START, BALL_Y_START);
		waitForClick();
		while (i < 4) {   
			checkForPaddle(); 
			checkBrickCollision(); 
			checkForXCollision();
			checkForYCollision();
			moveBall(); 
		} 
	} 
	
	/*
	 * Ending the game 
	 */
	
	private void stopProgram() {
		remove(ball); 
		pause(200); 
		endScreen(); 
		makeLabel(); 
	}
	
	/* Make the screen go black*/  
	private void endScreen() { 
		GRect end = new GRect(0, 0, WIDTH, HEIGHT);
		end.setFilled(true); 
		end.setColor(Color.black);
		add(end); 
	}
	
	/* Print "GAME OVER" to the screen */ 
	private void makeLabel() { 
		GLabel gameover = new GLabel("GAME OVER"); 
		gameover.setFont(new Font("Courier", Font.BOLD, 30));
		gameover.setLocation((getWidth() - gameover.getWidth())/2, HEIGHT/2);
		gameover.setColor(Color.WHITE);
		add(gameover); 
	}
	
	
	/*
	 * Making and controlling the ball  
	 */
	
	/* The ball itself */ 
	private GOval ball; 

	/* used to keep track of the ball in play */ 
	private int i; 
	
	/* x and y movement of the ball */ 
	private double dx = VEL;
	private double dy = VEL; 
	
	/* start off by building the ball */ 
	private void buildBall(double x, double y) { 
		ball = new GOval (x, y, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true); 
		add(ball); 
	}

	/* check if going to the right or left of screen*/ 
	private void checkForXCollision() { 
		if (ball.getX() < X_BARRIER_LEFT) { 
			dx = VEL; 
		}
		if (ball.getX() > X_BARRIER_RIGHT) { 
			dx = -VEL; 
		}
	}
	
	/* check if going too high or too low */ 
	private void checkForYCollision() { 
		if (ball.getY() < Y_BARRIER_UP) { 
			dy = VEL; 
		}
		if (ball.getY() > Y_BARRIER_DOWN) {
			i ++ ; 
			if (i < 4) { 
				endGame(); //end one move of the game  
			}
		}
	}
	
	/* resetting the game after ball falls to the bottom */ 
	private void endGame() { 
		remove(ball); 
		buildBall(BALL_X_START, BALL_Y_START);
		waitForClick(); 
	}
	
	/* checking whether the ball hits the paddle */ 
	
	private void checkForPaddle() { 
		if (ball.getY() > BOX_HEIGHT - 2*BALL_RADIUS) { 
			if (ball.getX() > box.getX() - 1 && ball.getX() < (box.getX() + 1 + PADDLE_WIDTH)) { 
				dy = -dy;
				pause(TIME);
			} 
		}
	}
	
	/* moving the ball */ 
	private void moveBall() { 
		if (i < 4) { // keeping track of lives 
			ball.move(dx, dy); 
			pause(TIME);
		} else { 
			stopProgram(); //executes the script to end the program  
			}  
	}
	
	
	/* 
	 * Making the paddle 
	 */
	
	private GRect box; 
	
	private void buildBar(double x, double y) { 
		box = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT); //box starts 
		box.setFilled(true); 
		add(box); 
	}
	
	public void mouseMoved(MouseEvent e) {
		double x = e.getX(); 
		if (x > APPLICATION_WIDTH - PADDLE_WIDTH) {  //box doesn't go offscreen  
			x = APPLICATION_WIDTH - PADDLE_WIDTH; 
		}
		remove(box); //erase where box was before 
		box.setLocation(x, BOX_HEIGHT); //update box coordinates 
		add(box); //add box 
	}
	
	
/*
 * Build the bricks 	
 */
	/* making the brick */ 
	private GRect brick;
	
	/* building the rows */ 
	private void buildRows(int x, int y) { 
		double brickDif = BRICK_HEIGHT + BRICK_SEP; 
		for (int i=0; i < N_COLOR_ROWS; i++) { 
			drawRow(x, y + brickDif*i, Color.RED);
		} 
		for (int i=1; i < N_COLOR_ROWS + 1 ; i++) { 
			drawRow(x, y+brickDif+(brickDif*i), Color.ORANGE); 
		}
		for (int i=1; i < N_COLOR_ROWS + 1; i++) { 
			drawRow(x, y+(3*brickDif)+(brickDif*i), Color.YELLOW); 
		}
		for (int i=1; i < N_COLOR_ROWS + 1; i++) { 
			drawRow(x, y+(5*brickDif)+(brickDif*i), Color.GREEN); 
		}
		for (int i=1; i < N_COLOR_ROWS + 1; i++) { 
			drawRow(x, y+(7*brickDif)+(brickDif*i), Color.CYAN);
		}
	}

	/* filling in the rows - part 1 */ 
	private void drawRow(double x, double y, Color color){
		for (int i=0; i < NBRICKS_PER_ROW ; i++) { 
			double n = i/2;
			double x_right = x + n*BRICK_WIDTH + n*BRICK_SEP; 
			double x_left = x - (n +1)*BRICK_WIDTH - (n + 1)*BRICK_SEP; 
			fillRow(x_right, x_left, y, color);
		}
	}
	
	/* filling in the rows - part 2 */ 
	private void fillRow(double x_right, double x_left, double y, Color color){ 
		brick = new GRect(x_right, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true); 
		brick.setColor(color);
		add(brick);
		brick = new GRect(x_left, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true); 
		brick.setColor(color); 
		add(brick); 
	}
		
	
	/* checking if the ball has hit a brick */ 
	private void checkBrickCollision() { 
		//checkRight(); 
		//checkLeft(); 
		//checkTop(); 
		checkBottom(); 
	}
	
	
	/* check if hit the right part of the brick */ 
	/*private void checkRight() {
		GObject right = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS); 
		if (right != null) { 
			if (right != ball && right != box) { 
				remove(right); 
				dx = -dx; 
			}
		}
	} 
	
	/* check if hit the left part of the brick */ 
	/*private void checkLeft() { 
		GObject left = getElementAt(ball.getX() + 2*BALL_RADIUS, ball.getY() + BALL_RADIUS); 
		if (left != null) { 
			if (left != ball && left != box) {
				remove(left);
				dx = -dx;  
			}
		}
	}
	
	/* check if hit the top of the brick */ 
	/*private void checkTop() { 
		GObject top = getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + 2*BALL_RADIUS); 
		if (top != null) { 
			if (top != ball && top != box) { 
				dy = -VEL; 
				remove(top); 
			}
		}
	}*/
	
	/* check if hit the bottom of the brick */ 
	private void getCollidingObject() { 
		GObject objtop1 = getElementAt(ball.getX(), ball.getY());
		GObject objtop2 = getElementAt(ball.getX() + 2*BALL_RADIUS, ball.getY());
		GObject objbottom1 = getElementAt(ball.getX(), ball.getY() + 2*BALL_RADIUS); 
		GObject objbottom2 = getElementAt(ball.getX() + 2*BALL_RADIUS, ball.getY() + 2*BALL_RADIUS);  
	}
	
	/*private void checkBottom() {
		GObject bottom = getElementAt(ball.getX() + BALL_RADIUS, ball.getY()); 
		if (bottom != null &&   
				bottom != box){ 
					remove(bottom); 
					dy = VEL; 
		}
	}*/ 
	
}