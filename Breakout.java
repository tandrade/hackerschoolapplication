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

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		buildRows(getWidth()/2 + BRICK_SEP/2, BRICK_Y_OFFSET);  
		//buildRows(getWidth()/2 + BRICK_SEP, BRICK_Y_OFFSET, Color.RED); 
	} 
	
	
	private void buildRows(int x, int y) { 
		double brickDif = BRICK_HEIGHT + BRICK_SEP; 
		for (int i=0; i < 2; i++) { 
			drawRowEven(x, y + brickDif*i, Color.RED);
		} 
		for (int i=1; i < 3; i++) { 
			drawRowEven(x, y+brickDif+(brickDif*i), Color.ORANGE); 
		}
		for (int i=1; i < 3; i++) { 
			drawRowEven(x, y+(3*brickDif)+(brickDif*i), Color.YELLOW); 
		}
		for (int i=1; i < 3; i++) { 
			drawRowEven(x, y+(3*brickDif)+(brickDif*i), Color.GREEN); 
		}
		for (int i=1; i < 3; i++) { 
			drawRowEven(x, y+(4*brickDif)+(brickDif*i), Color.CYAN);
		}
	}

	
	/**private void drawRow(double x, double y, Color color) { 
		if (NBRICKS_PER_ROW %2 == 0) { 
			drawRowEven(x, y, color); 
		} else {
			drawRowOdd(x, y, color);
		} 
	}*/
	
	private void drawRowEven(double x, double y, Color color){
		for (int i=0; i < NBRICKS_PER_ROW ; i++) { 
			double n = i/2;
			double x_right = x + n*BRICK_WIDTH + n*BRICK_SEP; 
			double x_left = x - (n +1)*BRICK_WIDTH - (n + 1)*BRICK_SEP; 
			fillRow(x_right, x_left, y, color);
		}
	}
	
	/*private void drawRowOdd(double x, double y, double count){
		double first_x = x - (BRICK_WIDTH * 0.5);  
		GRect brick = new GRect(first_x, y, BRICK_WIDTH, BRICK_HEIGHT);
		add(brick); 
		for (int i=1; i < count; i++) { 
			double n = (i - 1)/2;
			double x_right = first_x + (n + 1)*BRICK_WIDTH; 
			double x_left = first_x - (n + 1)*BRICK_WIDTH;
			fillRow(x_right, x_left, y);
		}
	} */
	
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
