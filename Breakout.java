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

/** X and Y coordinate for the ball */ 	
	private static final int BALL_X_START = WIDTH/2 - BALL_RADIUS;
	private static final int BALL_Y_START = BOX_HEIGHT - 150;
	
/** X and Y coordinate for the barriers */ 	
	private static final int X_BARRIER_LEFT = 2;
	private static final int X_BARRIER_RIGHT = WIDTH - 2*BALL_RADIUS; 
	private static final int Y_BARRIER_UP = 2*BALL_RADIUS; 
	private static final int Y_BARRIER_DOWN = HEIGHT - 4*BALL_RADIUS;
	
/** Number of rows for each color */	
	private static final int N_COLOR_ROWS = NBRICK_ROWS/5;
	
/** Time to wait between movements */ 	
	private static final int TIME = 10;
	
/** Difference between bricks */ 	
	private static double brickDif = BRICK_HEIGHT + BRICK_SEP;
	
/** How high to make the scoreboard */	
	private static int SCORE_OFFSET = BRICK_Y_OFFSET / 3;
	private static int SCORE_SIZE = SCORE_OFFSET / 2;
	
/** How much each brick is worth */ 	
	private static int BASE_SCORE = 10; 
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() { 
		/** adding parameters */ 
		gameFinished = false; 
		brickcount = 0; 
		i = 1; 
		score = 0; 
		setBackground(Color.BLACK);
		
		/** the game */ 
		addMouseListeners();
		setup();
		getStart(); 
		waitForClick(); 
		playGame(); 
	} 
	
	
	/*
	 * Starting the program 
	 */
	
	/** run all the start up commands */ 
	private void setup() { 
		buildRows(getWidth()/2 + BRICK_SEP/2, BRICK_Y_OFFSET);
		buildBar((getWidth() - PADDLE_WIDTH)/2, BOX_HEIGHT);
		buildBall(BALL_X_START, BALL_Y_START);
		showScore();
		showLives();
	}
	
	/** build the paddle */ 
	private void buildBar(double x, double y) { 
		box = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT); //box starts 
		box.setFilled(true); 
		box.setColor(Color.WHITE); 
		add(box); 
	}
	
	/* control the paddle */ 
	public void mouseMoved(MouseEvent e) {
		if (i < NTURNS + 1) { 
			double x = e.getX(); 
			if (x > APPLICATION_WIDTH - PADDLE_WIDTH) { //box doesn't go offscreen  
				x = APPLICATION_WIDTH - PADDLE_WIDTH;
			}
			remove(box); //erase where paddle was before 
			box.setLocation(x, BOX_HEIGHT); //update paddle coordinates
			box.setColor(Color.WHITE); 
			add(box); //add the paddle 
		} 
	}
	
	/** build the ball */ 
	private void buildBall(double x, double y) { 
		ball = new GOval (x, y, 2*BALL_RADIUS, 2*BALL_RADIUS);
		ball.setFilled(true); 
		ball.setColor(Color.WHITE);
		add(ball); 
	}
	
	/** build the rows */ 
	private void buildRows(int x, int y) { 
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
		for (int i=1; i < NBRICKS_PER_ROW + 1 ; i++) { 
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
		
	/*
	 * Game play 
	 */
	
	/** move the ball initially */
	private void getStart() { 
		RandomGenerator rgen = RandomGenerator.getInstance(); 
		Y_VEL = rgen.nextDouble(2.0, 4.0);
		X_VEL = rgen.nextDouble(2.0, 4.0); 
		if (rgen.nextBoolean(0.5)) X_VEL = -X_VEL;
	}
	
	/** moving the ball while game is working */ 
	private void playGame() { 
		while (gameFinished == false) { 
			checkObjCollision(); 
			checkForXCollision();
			checkForYCollision();
			moveBall(); 
			checkIfDone(); 
		}
	}
	
	
	/*
	 * Controlling the ball  
	 */
		
	/** check if going to the right or left of screen*/ 
	private void checkForXCollision() { 
		if (ball.getX() < X_BARRIER_LEFT) { 
			X_VEL = -X_VEL; 
		}
		if (ball.getX() > X_BARRIER_RIGHT) { 
			X_VEL = -X_VEL; 
		}
	}
	
	/** check if going too high or too low */ 
	private void checkForYCollision() { 
		if (ball.getY() < Y_BARRIER_UP) { 
			Y_VEL = -Y_VEL; 
		}
		if (ball.getY() > Y_BARRIER_DOWN) {
			i ++ ; 
			if (i < 4) {
				showLives(); //update how many lives there are 
				endTurn(); //end one move of the game  
			}
		}
	}
	
	/* update sign showing how many lives the player has */ 
	private void showLives() {
		livesdisp = new GLabel("Lives: " + i);
		if (livesdisp != null) remove(livesdisp); 
		livesdisp.setColor(Color.WHITE);
		livesdisp.setFont(new Font("Courier", Font.BOLD, SCORE_SIZE));
		livesdisp.setLocation((3*getWidth()/4 - livesdisp.getWidth())/2, SCORE_OFFSET);
		add(livesdisp); 
	}
	
	/* resetting the game after ball falls to the bottom */ 
	private void endTurn() { 
		remove(ball); 
		buildBall(BALL_X_START, BALL_Y_START);
		getStart(); 
		waitForClick(); 
	}
	

	
	/** check if hitting an object */ 
	private GObject getCollidingObject() { 
		GObject objtop1 = getElementAt(ball.getX(), ball.getY());
		GObject objtop2 = getElementAt(ball.getX() + 2*BALL_RADIUS, ball.getY());
		GObject objbottom1 = getElementAt(ball.getX(), ball.getY() + 2*BALL_RADIUS);
		GObject objbottom2 = getElementAt(ball.getX() + 2*BALL_RADIUS, ball.getY() + 2*BALL_RADIUS);
		
		//handling cases where multiple objects are 
		if (objtop1 != null & objtop2 != null) { 
			if (objtop1 != objtop2) objtop2 = null; 
		}
		if (objtop1 != null & objbottom1 != null) { 
			if (objtop1 != objbottom1) objbottom1 = null; 
		}
		if (objbottom1 != null & objbottom2 != null) { 
			if (objbottom1 != objbottom2) objbottom2 = null; 
		}
		if (objtop2 != null & objbottom2 != null) { 
			if (objtop2 != objbottom2) objbottom2 = null; 
		}
		
		// returning the right object 
		if (objtop1 != null) return objtop1; 
		else {
			if (objtop2 != null) return objtop2; 
			else { 
				if (objbottom1 != null) return objbottom1;
				else { 
					if (objbottom2 != null) return objbottom2;
					else return null; 
				}
			}
		}
	}
	
	/* giving instructions about what to do if hit a brick */ 
	private void checkObjCollision() { 
		GObject collision = getCollidingObject();
		if (collision != null 
		&& collision != scorecard		//don't react to hitting the scoreboard
		&& collision != livesdisp) { 	//don't react to hitting the life display
			if (collision == box) { 	// what to do if it's the paddle 
				if (Y_VEL > 0) Y_VEL = -Y_VEL; 
			}
			else {						// anything else = brick 
				getScore(); 			// update the score 
				remove(collision);			// remove the brick 
				brickcount = brickcount + 1; 	//count the bricks (to end the game) 
				safeMove();
			}
		} 
	}
	
	/* making sure that if a brick is hit, it is really removed */
	/* additional layer of protection against errors */ 
	private void safeMove() {
		GObject check = getCollidingObject();
		if (check != null) { 
			remove(check); 
			Y_VEL = -Y_VEL; 
		}
		if (check == null) { 
			Y_VEL = -Y_VEL; 
		}
	}
	
	/* update the score and scoreboard */ 
	private void getScore() { 
		score = score + BASE_SCORE; 
		remove(scorecard); 
		showScore(); 
	}
	
	/* making the scoreboard */ 
	private void showScore() {  
		scorecard = new GLabel("Score: " + score);
		scorecard.setColor(Color.WHITE);
		scorecard.setFont(new Font("Courier", Font.BOLD, SCORE_SIZE));
		scorecard.setLocation((getWidth()/4 - scorecard.getWidth())/2, SCORE_OFFSET);
		add(scorecard); 
	}
	
	/* moving the ball */ 
	private void moveBall() { 
		if (i < NTURNS + 1) { // keeping track of lives 
			ball.move(X_VEL, Y_VEL); 
			pause(TIME);
		} else { 
			stopProgram(); //executes the script to end the program  
			}  
	}
	
	
	/*
	 * Ending the game 
	 */
	
	/** If the player loses */ 
	private void stopProgram() {
		gameFinished = true; 
		remove(ball); 
		pause(200); 
		endScreen(); 
		makeLoseLabel(); 
	}
	
	/* Make the screen go black*/  
	private void endScreen() { 
		GRect end = new GRect(0, 0, WIDTH, HEIGHT);
		end.setFilled(true); 
		end.setColor(Color.black);
		add(end);
		; 
	}
	
	/* Print "GAME OVER" to the screen */ 
	private void makeLoseLabel() { 
		GLabel gameover = new GLabel("GAME OVER"); 
		gameover.setFont(new Font("Courier", Font.BOLD, 30));
		gameover.setLocation((getWidth() - gameover.getWidth())/2, HEIGHT/2);
		gameover.setColor(Color.WHITE);
		add(gameover); 
	}
	
	/** If the player wins */ 
	private void checkIfDone() { 
		if (brickcount == NBRICKS_PER_ROW * NBRICK_ROWS) { //compare bricks hit v total number of bricks  
			remove(ball); 
			gameFinished = true; 
			makeWinLabel(); 
		}
	}
	
	/* Show congratulations */ 
	private void makeWinLabel() { 
		GLabel congrats = new GLabel("Congratulations"); 
		congrats.setFont(new Font("Courier", Font.BOLD, 30));
		congrats.setLocation((getWidth() - congrats.getWidth())/2, HEIGHT/2);
		congrats.setColor(Color.WHITE);
		add(congrats); 
	}
	
	
	/** Gobjects called by multiple methods */ 
	private GRect brick;	
	private GRect box; 		
	private GOval ball;
	private GLabel scorecard;
	private GLabel livesdisp;
	
	/** boolean commands called by multiple objects */ 
	private boolean gameFinished;  

	/** numbers changed by multiple programs */ 
	private int i; 			//number of lives 
	private int score; 
	private int brickcount; 
	private double X_VEL; 
	private double Y_VEL; 
	
	
	
}