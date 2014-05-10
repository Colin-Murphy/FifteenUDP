/**
	The Model for a game of Fifteen
	@author Colin L Murphy <clm3888@rit.edu>
	@version 5/1/14
*/

import java.util.ArrayList;
import java.util.Iterator;

public class FifteenModel implements ViewListener {

	//Total number of players
	private static final int NUM_PLAYERS = 2;
	
	//The winning score
	private static final int WINNING_SCORE = 15;
	
	//Number of digits in the game
	private static final int NUM_DIGITS = 9;
	
	//Number of available digits
	private int availableAmount;
	
	//Reset score (0)
	private static int RESET_SCORE = 0;
	
	//Session Manager to talk to on quit
	private SessionManager manager;
	
	//View proxies to talk to
	private ArrayList<ModelListener> modelListeners = new ArrayList<ModelListener>();
	
	//Availabel digits
	private Boolean[] available = new Boolean[NUM_DIGITS];
	
	
	
	//Scores of the players
	private int[] scores = new int[2];
	
	//ID's of players
	private static final int PLAYER_ONE = 1;
	private static final int PLAYER_TWO = 2;
	
	//Whos turn it is
	private int turn;
	
	
	
	private String[] names = new String[2];
	
	
	/**
		Create a new Fifteen Model
		@param manager The session manaager to alert on quit
	*/
	public FifteenModel(SessionManager manager) {
		this.manager = manager;
	}
	
	/**
		Player Joined
		@param Proxy (unused)
		@param name The players name
	*/
	
	public void joined(ViewProxy proxy, String name) {
	
		int id;
		
		if (names[PLAYER_ONE - 1] == null) {
			names[PLAYER_ONE - 1] = name;
			id = PLAYER_ONE;
		}
		
		else {
			names[PLAYER_TWO - 1] = name;
			id = PLAYER_TWO;
			//Tell player 2 about player 1
			modelListeners.get(id-1).playerInfo(PLAYER_ONE, names[PLAYER_ONE -1]);
		}
		
		
		//inform the player of their id
		modelListeners.get(id-1).joined(id);
		
		//Tell everyone about the new player
		Iterator<ModelListener> iter = modelListeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			listener.playerInfo(id, name);

		}
		
		//Tell everyone the new game has started if player two has arrived
		if (id == PLAYER_TWO) {
			newGame();
		}
		
			
	}
	
	
	/**
		Add a model listener
		@param listener The model listener to add
	*/
	public void addModelListener(ModelListener listener) {
		modelListeners.add(listener);
	}
	
	/**
		Player quit the game
	*/
	public void quit() {
	
	
		//Tell everyone the game quit
		Iterator<ModelListener> iter = modelListeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			listener.quit();
		}
		
		//Inform the sesssion manager that this session is done.
		manager.killSession(this);

	}
	
	/**
		Player clicked a digit, assumed to be from the player whos turn it is
		@param digit the digit clicked
	*/
	public void digit(int digit) {
	
		scores[turn-1] +=digit;
		
		boolean gameOver = false;
		
		//Handle winning
		if (scores[turn-1] == WINNING_SCORE) {
		
			//Tell everyone the game quit
			Iterator<ModelListener> iter = modelListeners.iterator();
			while (iter.hasNext()) {
				ModelListener listener = iter.next();
				listener.playerScore(turn, scores[turn-1]);
				listener.winner(turn);
				listener.playerTurn(0);

			}
			
			return;
			
		
			
		}
		
		int scoreBy = turn;
		
		available[digit-1] = false;
		
		availableAmount--;
		
		//Handle draw
		if (availableAmount == 0) {
			gameOver = true;
		}
		
		//Figure out new turn
		if (turn == PLAYER_ONE) {
			turn = PLAYER_TWO;
		}
		
		else {
			turn = PLAYER_ONE;
		}
		
		if (gameOver) {
			turn = 0;
		}
		
		//Tell everyone whos turn it is and whats available
		Iterator<ModelListener> iter = modelListeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			listener.playerScore(scoreBy, scores[scoreBy-1]);
			listener.availableDigits(available);
			
			if (!gameOver) {
				listener.playerTurn(turn);
			}
			
			else {
				listener.playerTurn(0);
				listener.winner(0);
			}
		}
		
		
		
		
	
	
	}
	
	/**
		Player has restarted the game, reset all game data and let them know
	*/
	public void newGame() {
	
		scores[0] = RESET_SCORE;
		scores[1] = RESET_SCORE;
		
		availableAmount = NUM_DIGITS;
		
		turn = PLAYER_ONE;
		
		for (int i=0;i<available.length;i++) {
			available[i] = true;
		}
		
		//Tell everyone the new information
		Iterator<ModelListener> iter = modelListeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			//New board
			listener.availableDigits(available);
			//Each player has 0 points
			listener.playerScore(PLAYER_ONE, 0);
			listener.playerScore(PLAYER_TWO, 0);
			
			//Player ones turn
			listener.playerTurn(turn);
		}
		
		
	
	
	}
		
		
		


}
