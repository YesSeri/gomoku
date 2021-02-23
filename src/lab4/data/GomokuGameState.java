package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game
 */

@SuppressWarnings("deprecation")
public class GomokuGameState extends Observable implements Observer{

   // Game variables
	private final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;
	
    //Possible game states
	private final int NOT_STARTED = 0;
	private final int MY_TURN = 1; 
	private final int OTHER_TURN = 2;
	private final int FINISHED =3;
	
	private int currentState;
	
	private GomokuClient client;
	
	private String message;
	
	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc){
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
		setChanged();
		notifyObservers();
	}
	

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString(){
		return message;
	}
	private void setMessage(String message) {
		setChanged();
		notifyObservers();
		this.message = message;
	}
	
	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid(){
		return gameGrid;
	}

	/**
	 * This player makes a move at a specified location
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y){
		if (currentState == MY_TURN) {
			boolean isSuccessful = gameGrid.move(x, y, Square.ME.getValue());
			if(isSuccessful) {
				client.sendMoveMessage(x, y);
				if(gameGrid.isWinner(Square.ME.getValue())) {
					setMessage("Move could not be made. Click an empty square.");
					currentState = OTHER_TURN;
				} else {
					setMessage("CONGRATULATIONS, YOU ARE THE WINNER!!!");
					currentState = FINISHED;
				}
			} else {
				setMessage("Move could not be made. Click an empty square.");
			}
			setChanged();
			notifyObservers();
			
		} 
		
	}
	
	/**
	 * Starts a new game with the current client
	 */
	public void newGame(){
		currentState = MY_TURN;
		setMessage("Game started, it is your turn");
		resetBoard();
	}
	
	/**
	 * Other player has requested a new game, so the 
	 * game state is changed accordingly
	 */
	public void receivedNewGame(){
		currentState = OTHER_TURN;
		setMessage("Game started, waiting for opponents move. ");
		resetBoard();
	}
	
	/**
	 * The connection to the other player is lost, 
	 * so the game is interrupted
	 */
	public void otherGuyLeft(){
		setMessage("Your opponent has disconnected from the game.");
		resetBoard();
	}
	
	/**
	 * The player disconnects from the client
	 */
	public void disconnect(){
		client.disconnect();
		setMessage("You have disconnected from the game.");
		resetBoard();
		
	}
	
	private void resetBoard() {
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y){
		gameGrid.move(x, y, Square.OTHER.getValue());
		if(gameGrid.isWinner(Square.OTHER.getValue())) {
			setMessage("YOU LOST, BETTER LUCK NEXT TIME!!!");
		} else {
			setMessage("Opponent has moved, your turn.");	
		}
		setChanged();
		notifyObservers();
	}
	
	public void update(Observable o, Object arg) {
		
		switch(client.getConnectionStatus()){
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHER_TURN;
			break;
		}
		setChanged();
		notifyObservers();
		
		
	}
	
}