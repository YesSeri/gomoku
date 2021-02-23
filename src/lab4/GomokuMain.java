package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

public class GomokuMain {

	private static int portNum = 4000; // Default value
	
	public static void main(String[] args) {
		// Input Check
		if (args.length > 1) {throw new IllegalArgumentException("Too many arguments are given"); } // Too many arguments
		
		try { if (args.length == 1) { portNum = Integer.parseInt(args[0]); } } // Set value
		catch (NumberFormatException e) {throw new IllegalArgumentException("Expected argument of type int");} // Not integer
		
		// Initiating app
		GomokuClient me = new GomokuClient(portNum);
		GomokuGameState gameState = new GomokuGameState(me);
		GomokuGUI gui = new GomokuGUI(gameState, me);
		
	}

}
