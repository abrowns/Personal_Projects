/*
	File: Simple_Graphics.java
		This file contains all the graphics handling for the animation

	Original Author: 	Ken Whitener
	Edited by: 			Andrew Brown

	Date: 3/14/2024
*/






import java.awt.Color;					// import the color class
import java.awt.EventQueue;				// import the Graphics class
import java.awt.Graphics;				// import the Graphics2D class
import java.awt.Graphics2D;		// Timer kicks out ActionEvents
import java.awt.event.ActionEvent;	//// The class needs to listen for ActionEvents
import java.awt.event.ActionListener;				// A JPanel is a simple container for graphics
import javax.swing.JFrame;				// A Timer object kicks out ActionEvents at regular intervals
import javax.swing.JPanel;
import javax.swing.Timer;


public class Simple_Graphics extends JPanel implements ActionListener{

	private final int DELAY = 100;	// delay for ActionListener Timer. Change this to change the speed of the simulation
	
	private Timer timer;			// Timer object to control repainting.

	private JFrame window = new JFrame();

	private static World grid = new World(); 			// needs a reference to the class that contains the matrix
	Cell[][] world = grid.setWorld(70,70);
	int rows = world.length;
	int columns = world[0].length;

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				Simple_Graphics simpG = new Simple_Graphics(grid);

			}
		});
	}

	/**
	 * Constructor
	 * @param grid - the World object to be rendered
	 */
	public Simple_Graphics(World grid) {
		window.add(this);
		window.setTitle("Fire Animation");
		window.setSize(rows*11, columns*11);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		this.grid = grid;			// store the reference to the grid
		initTimer();				// initialize the timer

	}

	/**
	 * Method to initialize and start the Timer object
	 */
	private void initTimer() {
		timer = new Timer(DELAY, this);	// create a new Timer object with delay and reference to ActionListener class
		timer.start();					// start the timer
	}

	// method to render the world in Java 2D Graphics
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // type cast Graphics object to 2D. 
		
		final int height	= 10;	// height for the rectangle
		final int width	= 10;		// width for the rectangle
		int x = 1, y = 1;			// coordinates for each rectangle

		for (int i = 0; i < world.length; i++)
		{
			for (int j = 0; j < world[i].length; j++)
			{
				Cell currCell = world[i][j];	// creates a variable to store the current cell object
				Color cellColor = currCell.getColor();	// gets the color of the current cell
				g2d.setColor(cellColor);	// sets the color for the rectangle
				g2d.fillRect(x,y,width,height);	// draws the rectangle at the specified point with the height and width
				x += width;	// moves the rectangle for the next drawing
			}
			y += height;	// moves the rectangle down after the previous row has been drawn
			x = 1;			// resets the x position to start over for the new row
		}

	}

	@Override // method will be called each time timer "ticks"
	public void actionPerformed(ActionEvent arg0) {


		Cell[][] newWorld = grid.applySpread(world);	// creates a new altered world based on the last one
		this.world = newWorld;	// changes the world into the newly modified world

		// This method represents a time step
		// TO DO: update the grid by iterating through the cells and updating their state
		// with the transition rules
		// then repaint the grid
		this.repaint();					// call the repaint method, which will call paintComponent
	}

	
	@Override // automatically called by "repaint"
	public void paintComponent(Graphics g){
		super.paintComponent(g);		// call the superclass method
		doDrawing(g);					// call the doDrawing method
	}
}