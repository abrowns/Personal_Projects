/**
 * File: WorldTest.java
 *      This file contains all the tests for "World.java"
 * Author: Andrew Brown
 * 
 */


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.awt.Color;  // import the color class

public class WorldTest
{

    Cell newCell;       // Cell object
    World newWorld;     // World object reference
    Cell[][] grid;      // Cell array


    @Test   // test the getState method
    void getState()
    {
       newCell = new Cell();    // creates a new cell object
       String state = newCell.getState();   // stores the state of the cell as a string
       assertEquals("tree", state); // all default cell states are the "tree" state
    }

    @Test   // test the setState method
    void setState()
    {
        newCell = new Cell(); // create a new cell object
        assertEquals("tree", newCell.getState());   // default state is "tree"
        newCell.setState("empty");  // sets the cell's state to "empty"
        assertEquals("empty", newCell.getState());
    }

    @Test   // test the setWorld method
    void setWorld()
    {
        newWorld = new World();             // instantiates a new world reference
        grid = newWorld.setWorld(10,10);    // creates an array of cell objects from the world reference
        assertEquals(12, grid.length);      // checks if the length of the array is 10+2 (n+2)
        assertEquals(12, grid[0].length);   // checks if the height of the array is 10+2 (n+2)
        for (int i = 0; i < grid.length; i++)
        {              
            for (int j = 0; j < grid[i].length; j++)
            {   if (i == grid.length - 1 || j == grid[i].length - 1 || i == 0 || j == 0)
                {
                    assertEquals("empty", grid[j][i].getState());   // checks all outer edges for the empty state
                }
                else if ((double)i - (double)grid.length/2 == 0 && (double)j - (double)grid[i].length/2 == 0)
                {
                    assertEquals("burning", grid[i][j].getState()); // checks the middle cell for the burning state
                }
                else
                {
                    assertEquals("tree", grid[i][j].getState());    // checks every other cell for a tree state
                }
            }
        }
    }
   
    @Test   // test the applySpred method
    void applySpread()
    {
        newWorld = new World();
        grid = newWorld.setWorld(10,10);    // create a new world grid
        Cell[][] altered = newWorld.applySpread(grid);  // spread fire for a single time step

        boolean[] burningCells = newWorld.checkSurroundings(altered, grid.length/2, grid[0].length/2);  // checks the surroundings of the middle cell
        boolean burning = burningCells[0];  // true if any of the surrounding cells are a burning state
        boolean raging = burningCells[1];   // true if any of the surrounding cells are a raging state
        assertTrue(burning || raging);    // true if a burning state is around the middle cell
        assertEquals(grid.length, altered.length);  // makes sure the altered grid's length stays the same
    }

    @Test   // test the checkSurroundings method
    void checkSurroundings()
    {
        newWorld = new World();
        grid = newWorld.setWorld(10,10);    // create a new world grid
        boolean[] cellNeighbors = newWorld.checkSurroundings(grid, grid.length/2 - 1 , grid[0].length/2);  // checks the surrounding cell's states of the cell below the middle cell
        boolean burningTrue = cellNeighbors[0]; // this should return true because the middle cell starts out with a burning state
        boolean ragingFalse = cellNeighbors[1]; // this should return false because no cells are able to have the raging state
        assertTrue(burningTrue);
        assertEquals(false, ragingFalse);
        grid[0][0].setState("raging");  // sets the first cell in the grid to a raging state
        boolean[] firstCellCheck = newWorld.checkSurroundings(grid, 1, 1);
        boolean ragingTrue = firstCellCheck[1]; // this should return true now because the first cell was changed to a raging state
        boolean burningFalse = firstCellCheck[0]; // this should return false because no cells around the first cell are in the burning state
        assertTrue(ragingTrue);
        assertEquals(false, burningFalse);

    }

    @Test   // test the getColor method
    void getColor()
    {
        newCell = new Cell();   // create a new cell object
        Color green = newCell.getColor();   // stores the color of the cell in a color object
        assertEquals(Color.GREEN, green);   // the default state is "tree", and the default color is green
        newCell.setState("empty");  // sets the state of the cell to "empty"
        Color yellow = newCell.getColor();  // the changed state of the cell should also change the color
        assertEquals(Color.YELLOW, yellow); // the "empty" state has the yellow color
    }

}