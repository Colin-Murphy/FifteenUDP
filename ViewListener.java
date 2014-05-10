import java.io.IOException;

/**
	The interface for listening to a FifteenView object
	@author Colin L Murphy <clm3888@rit.edu>
	@version 4/12/14
*/


public interface ViewListener {

	/**
		Player joined the server
		@param name players name
	*/
	public void joined(String name);
	
	/**
		Player clicked a diget
		@param digit the digit clicked
	*/
	public void digit(int digit);


	/**
		Player has restarted the game
	*/
	public void newGame();
	
	
	/**
		Player quit the game
	*/
	public void quit();
	
	/**
		Set the model listener to listen to
		@param modelListener The model listener to listen to
	*/
	public void setModelListener(ModelListener modelListener);
}
