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

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		buildRows(getWidth()/2 + BRICK_SEP/2, BRICK_Y_OFFSET);
		buildBar((getWidth() - PADDLE_WIDTH)/2, BOX_HEIGHT);
		buildBall(BALL_X_START, BALL_Y_START);
		waitForClick();
		while (true) { 
			checkForPaddle(); 
			checkForXCollision();
			checkForYCollision();
			moveBall(); 
		}
	} 
	
	/*
	 * Making the ball  
	 */
	
	private GOval ball; 
	
	private void buildBall(double x, double y) { 
		ball = new GOval (x, y, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true); 
		add(ball); 
	}
	
	private static final int X_BARRIER_LEFT = 2;
	private static final int X_BARRIER_RIGHT = WIDTH - 2*BALL_RADIUS; 
	private static final int Y_BARRIER_UP = 2*BALL_RADIUS; 
	private static final int Y_BARRIER_DOWN = HEIGHT - 4*BALL_RADIUS; 
	private static final int VEL = 15;
	
	private double dx = VEL;
	private double dy = VEL; 
	
	private void checkForXCollision() { 
		if (ball.getX() < X_BARRIER_LEFT) { 
			dx = VEL; 
		}
		if (ball.getX() > X_BARRIER_RIGHT) { 
			dx = -VEL; 
		}
	}
	
	private void checkForYCollision() { 
		if (ball.getY() < Y_BARRIER_UP) { 
			dy = VEL; 
		}
		if (ball.getY() > Y_BARRIER_DOWN) { 
			dy = -VEL; 
		}
	}
	
	private void moveBall() { 
		ball.move(dx, dy); 
		pause(60);
	}
	

	private void checkForPaddle() { 
		if (ball.getY() > BOX_HEIGHT - 2*BALL_RADIUS && ball.getX() > box.getX() && ball.getX() < (box.getX() + PADDLE_WIDTH)) { 
			dy = VEL;
			dx = -dx;
			pause(60); 
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
	private static final int N_COLOR_ROWS = NBRICK_ROWS/5; 
	
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

	private void drawRow(double x, double y, Color color){
		for (int i=0; i < NBRICKS_PER_ROW ; i++) { 
			double n = i/2;
			double x_right = x + n*BRICK_WIDTH + n*BRICK_SEP; 
			double x_left = x - (n +1)*BRICK_WIDTH - (n + 1)*BRICK_SEP; 
			fillRow(x_right, x_left, y, color);
		}
	}
	
	private void fillRow(double x_right, double x_left, double y, Color color){ 
		GRect brick_right = new GRect(x_right, y, BRICK_WIDTH, BRICK_HEIGHT);
		GRect brick_left = new GRect(x_left, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick_right.setFilled(true); 
		brick_right.setColor(color); 
		add(brick_right);
		brick_left.setFilled(true); 
		brick_left.setColor(color); 
		add(brick_left); 
	}

}
