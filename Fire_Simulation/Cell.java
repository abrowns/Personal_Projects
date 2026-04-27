/*
 * File:    Cell.java
 * Author:  Andrew Brown
 * Date:    3/14/2024
 */

import java.awt.Color;

public class Cell
{
    static enum States
    { empty, tree, burning, raging };  // names of each state representation
    // I wanted to add a raging state, which represents a raging fire that has a much higher probability to spread fire
    // only the burning state has a chance to get to a raging state
    // I thought it was a cool idea for a stronger fire to exist
    static Color[] colors = 
    { Color.YELLOW , Color.GREEN , new Color(255,0,0), new Color(90,0,0) }; // colors to represent each state
    // bright red represents regular burning state, and darker red represents raging state
    private Enum state = States.tree;       // initializes the state as a tree
    private Color cellColor = colors[1];    // initializes the color of the cell to match the state
    
    /**
     * Returns the state of the cell (empty, tree, burning, raging) as a string
     *
     * @return the state of the cell
     */
    public String getState()
    {
        return this.state.toString();   // returns a string representation of the current state of the cell
    }

    /**
     * Sets the enum state of the specified cell.
     * Does not return anything
     * @param stateOrd  the numbered location of the state enum
     */
    public void setState(String state)
    {
        this.state = States.valueOf(state);     // changes the state to the specified string
        int stateOrd = this.state.ordinal();    // gets the number of the state in the enumerations
        this.cellColor = colors[stateOrd];      // sets the cell color based on the state
    }

    /**
     * Returns the specified cell's color
     *
     * @return the color of the cell
     */
    public Color getColor()
    {
        return this.cellColor;
    }


}