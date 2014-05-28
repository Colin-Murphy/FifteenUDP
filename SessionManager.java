/**
	Sets up sessions for the Fifteen Server
	@author Colin L Murphy <clm3888@rit.edu>
	@version 5/1/14
*/

public class SessionManager implements ViewListener {

	//The current open session, after a session is full the server doesn't need to track it
	FifteenModel openSession = null;
	
	/**
		Construct a new session manager
	*/
	public SessionManager() {
		
		//Nothing to do here...
	
	}
	
	/**
		Join a session or create a new session
		@param proxy The view proxy needed for communicating back to the user
	*/
	public synchronized void joined(ViewProxy proxy, String name) {
	

		
		FifteenModel model;
		
		//Session waiting for a second player
		if (openSession != null) {
			model = openSession;
			//Forget about this session so nobody else joins
			openSession = null;
			
		}
		
		else {
			model = new FifteenModel(this);
			openSession = model;
		}
		
		model.addModelListener(proxy);
		proxy.setViewListener(model);
		
		//model doesn't care about the proxy in its constructor, this just conforms with the interface
		model.joined(null, name);
		
		
	
	}
	
	/**
		The only time that a session ending matters is when it is the
		open session, all dropped sessions call this function, if they
		are the open session (meaning the player got tired of waiting)
		then dump the open session
		
		@param session The session being dropped
	*/
	public void killSession(FifteenModel session) {
		if (session == openSession) {
			openSession = null;
		}
	}
	
	
	//Doesnt use any of these, but they are required by the interface
	
	//These are all called on the model
	/**
		Player clicked a diget
		@param digit the digit clicked
	*/
	public void digit(int digit) {
	
	}


	/**
		Player has restarted the game
	*/
	public void newGame() {
	
	}
	
	
	/**
		Player quit the game
	*/
	public void quit() {
	
	}
}
	
	
	
