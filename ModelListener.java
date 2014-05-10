/**
	An interface that specifies functions necessary to listen to the 
	Fifteen model
	
	@author Colin L Murphy <clm3888@rit.edu>
	@version 4/11/14
*/

public interface ModelListener {

	/**
		Inform the client it has joined the game, set their id
		@param id The clients id
	*/
	public void joined(int id);

	/**
		Report the name and id of a player
		@param id The players id
		@param name The players name
	*/
	public void playerInfo(int id, String name);
	
	
	/**
		Set the 
		@param digits An array of booleans telling if that digit is available
	*/
	public void availableDigits(Boolean[] digits);
	
	/**
		Set the score of a player
		@param id the id of the player
		@param score the score of the player
	*/
	public void playerScore(int id, int score);
	
	/**
		Inform the view of whos turn it is
		@param i The id of the players whos turn it is
	*/
	public void playerTurn(int id);
	
	/**
		Display the winner of the game (or say its a draw)
		@param winner the id of the winner (0 for draw)
	*/
	public void winner(int winner);
	
	/**
		Quit the game
	*/
	public void quit();
	
	/**
		Set the view listener
		@param viewListener the view listener
	*/
	public void setViewListener(ViewListener viewListener);

}
