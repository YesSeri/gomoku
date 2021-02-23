package lab4.data;

import java.util.Arrays;
import java.util.Observable;


/**
 * Represents the 2-d game grid
 */

enum Square {
    EMPTY(0),
    ME(1),
    OTHER(2);

    private final int id;
    Square(int id) { this.id = id; }
    public int getValue() { return id; }
    public boolean isEqual(Square compare) { 
    	return this.getValue() == compare.getValue() ? true : false;
    }
    
    static public Square playerToSquare(int player) {
    	if (player == Square.ME.getValue()) {
    		return Square.ME;
    	}else if (player == Square.OTHER.getValue()) {
    		return Square.OTHER;
    	} else {
    		throw new IllegalArgumentException("Invalid value for player");
    	}
    }
}

@SuppressWarnings("deprecation")
public class GameGrid extends Observable{
	final static int INROW = 5;
	private Square[][] grid;
	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 */
	public GameGrid(int size){
		this.grid = new Square[size][size];
		this.clearGrid();
	}
	
	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y){
		return grid[x][y].getValue();
	}
	
	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize(){
		return grid.length * grid[0].length;
	}
	
	private void boardUpdated() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Enters a move in the game grid
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player){
		if(grid[x][y] != Square.EMPTY) {
			return false;
		}
		Square s = Square.playerToSquare(player);
		grid[x][y] = s;
		boardUpdated();
		return true;
	}
	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid(){
		for (int i = 0; i < grid.length; i++) {
			Arrays.fill(grid[i], Square.EMPTY);
		}
		boardUpdated();
	}
	
	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player){
		Square control = Square.playerToSquare(player);
		
		for (int row = 0; row < grid.length  ; row++) {
			for(int col = 0; col < grid[row].length; col++) {	
				if(grid[row][col] == control){
					if(controlSquare(row, col, control)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private boolean controlSquare(int row, int col, Square control) {
		if(controlVertical(row, col, control)) {
			return true;
		}
		if(controlHorizontal(row, col, control)) {
			return true;
		}
		if(controlNWSEDiagonal(row, col, control)) {
			return true;
		}
		if(controlNESWDiagonal(row, col, control)) {
			return true;
		}
		
		return false;
		
	}
	private boolean controlVertical(int row, int col, Square control) {
		if (row + INROW > grid.length) {
			return false;
		}
		for(int i = row + 1; i < row + INROW; i++) {
			if(!control.isEqual(grid[i][col])) {
				return false;
			}
		}
		return true;
		
	}
	private boolean controlHorizontal(int row, int col, Square control) {
		if (col + INROW > grid.length) {
			return false;
		}
		for(int i = col + 1; i < col + INROW; i++) {
			if(!control.isEqual(grid[row][i])) {
				return false;
			}
		}
		return true;
		
	}
	
	private boolean controlNWSEDiagonal(int row, int col, Square control) {
		if (col + INROW > grid.length || row + INROW > grid.length) {
			return false;
		}
		for(int i = col + 1; i < col + INROW; i++) {
			if(!control.isEqual(grid[i][i])) {
				return false;
			}
		}
		return true;
		
	}
	
	private boolean controlNESWDiagonal(int row, int col, Square control) {
		if (col + INROW > grid.length) {
			return false;
		}
		for(int i = col + 1; i < col + INROW; i++) {
			if(!control.isEqual(grid[i][INROW - i -1])) {
				return false;
			}
		}
		return true;	
	}	
}
