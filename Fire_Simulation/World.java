/*
 * File:    World.java
 * Author:  Andrew Brown
 * Date:    3/14/2024
 */
import java.util.Random;

public class World
{
    final double rageProb = 0.1;           // the probability to advance from a burning to raging state
    final double regBurnProb = 0.28;        // the regular probability to advance from a tree to burning state
    final double ragingBurnProb = 0.8;     // the probability to advance from a tree to burning state if a neighbor is a raging state
    // I played around with these probabilities and thought this was the coolest

    /**
     * Returns a matrix of cells with positions set based on the number of columns and rows.
     * Also initializes all states.
     * @param columns number of columns
     * @param rows    number of rows
     * @return the matrix of cells set to initial states and positions
     */
    public Cell[][] setWorld(int rows, int columns)
    {
        rows += 2;
        columns += 2;   
        // (n+2)x(n+2)
        Cell[][] grid = new Cell[rows][columns];
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < columns; j++)
            {
                grid[i][j] = new Cell();    // instantiates a new cell object for every matrix cell
                if (i == rows - 1 || j == columns - 1 || i == 0 || j == 0)
                {
                    grid[i][j].setState("empty");   // sets all outer edges to empty states
                }
                if ((double)i - (double)rows/2 == 0 && (double)j - (double)columns/2 == 0)
                {
                    grid[i][j].setState("burning"); // sets the middle cell to burning
                }
            }
        }
        return grid;
    }

    /**
     * Returns an Altered version of the given matrix's cells based on their surroundings.
     * @param grid the matrix that came before
     * @return the new altered matrix
     */
    public Cell[][] applySpread(Cell[][] grid)
    {
        Cell[][] newGrid = setWorld(grid.length - 2, grid[0].length - 2);   // creates a new cell matrix to store the altered cells in
        // subtract the length by 2 to account for (n+2)x(n+2)

        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                newGrid[i][j] = new Cell();
                String This = grid[i][j].getState();    // gets the current cell's state

                if (This.equals("empty"))
                {
                    newGrid[i][j].setState("empty");
                    continue; // skips empty cells after inserting them into the new grid
                }   
                Random randNumGen = new Random();   // random number generator
                float probNum = randNumGen.nextFloat();  // gets a float from the random number generator (1.0 > x > 0.0)

                boolean[] surroundings = checkSurroundings(grid, i, j);      // gets an array for the surrounding cell states
                boolean burning = surroundings[0];  // true if a neighbor is a burning state
                boolean raging = surroundings[1];   // true if a neighbor is a raging state

                if (This.equals("burning"))  // if the current cell is a burning state
                {
                    if (probNum <= rageProb)
                    {
                        newGrid[i][j].setState("raging");   // the probability of advancing to a raging state
                    }
                    else if (raging && probNum <= rageProb*3) // if a neighbor cell is a raging state the probability to advance to a raging state triples
                    {
                        newGrid[i][j].setState("raging");
                    }
                    else if (burning && probNum <= rageProb*1.5) // if a neighbor cell is also a burning state the probability to advance to a raging state is higher
                    {
                        newGrid[i][j].setState("raging");
                    }
                    else
                    {
                        newGrid[i][j].setState("empty");    // otherwise the fire goes out
                    }
                }
        
                else if (This.equals("tree"))    // if the current cell is a tree state
                {
                    if (burning && !raging && probNum <= regBurnProb)   // if a neighbor is burning and not raging
                    {
                        newGrid[i][j].setState("burning");  // advances to burning state depending on probability
                    }
                    else if (raging && probNum <= ragingBurnProb)      // if a neighbor is raging
                    {
                        newGrid[i][j].setState("burning");  // advances to burning state depending on a higher probability
                    }
                    else newGrid[i][j].setState("tree");    // otherwise the state remains a tree
                }

                else newGrid[i][j].setState("empty");  // raging automatically turns to empty during the next time step
            }
        }
        return newGrid;
    }

    /** Checks a grid's cell's surroundings from given the grid location of the cell, and returns a boolean array
     * 
     * @param grid the grid of cells
     * @param i the row number integer
     * @param j the column number integer
     * 
     * @return boolean array: THE FIRST value is true if any of the surrounding cells are burning;
     *         THE SECOND is true if any of the surrounding cells are raging
     */
    public boolean[] checkSurroundings(Cell[][] grid, int i, int j)
    {
        String N, S, E, W, NE, NW, SE, SW;
        S = grid[i+1][j].getState();
        N = grid[i-1][j].getState();
        W = grid[i][j-1].getState();
        E = grid[i][j+1].getState();
        SW = grid[i+1][j-1].getState();
        SE = grid[i+1][j+1].getState();
        NW = grid[i-1][j-1].getState();
        NE = grid[i-1][j+1].getState();     // gets the state of every cell around the current one

        boolean burning = N.equals("burning") ||
                            S.equals("burning") ||
                            E.equals("burning") ||
                            W.equals("burning") ||
                            NE.equals("burning") ||
                            NW.equals("burning") ||
                            SW.equals("burning") ||
                            SE.equals("burning");     // checks if any of the states around the current cell are burning

        boolean raging = N.equals("raging") ||
                            S.equals("raging") ||
                            E.equals("raging") ||
                            W.equals("raging") ||
                            NE.equals("raging") ||
                            NW.equals("raging") ||
                            SW.equals("raging") ||
                            SE.equals("raging");   // checks for any raging states around the cell

        boolean[] surroundings = { burning, raging };
        return surroundings;
    }


}