import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class ViewProxy implements ModelListener {

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ViewListener viewListener;
	private String message;

	/**
		Create a new view proxy
	*/
	public ViewProxy(Socket socket) {
		this.socket = socket;

		try {
			out = new DataOutputStream (socket.getOutputStream());
			in = new DataInputStream (socket.getInputStream());
		}
		
		catch (IOException exc){}
	}
	
	 /**
	* Set the view listener object for this view proxy.
	*
	* @param  viewListener  View listener.
	*/
	public void setViewListener (ViewListener viewListener) {
		if (this.viewListener == null) {
	 		this.viewListener = viewListener;
	 		new ReaderThread() .start();
	 	}
		else {
			 this.viewListener = viewListener;
	 	}
	}
	
	/**
		Inform the client it has joined the game, set their id
		@param id The clients id
	*/
	public void joined(int id) {
	
		try {
			message = "id " + id + "\n";
			out.writeBytes(message);
			out.flush();
		}
		
		catch (IOException exc){}
	
	
	}

	/**
		Report the name and id of a player
		@param id The players id
		@param name The players name
	*/
	public void playerInfo(int id, String name) {
		message = "name " + id + " " + name  + "\n";
		
		try {
			out.writeBytes(message);
			out.flush();
		}
		
		catch (IOException exc){}
	
	}
	
	
	/**
		Set the 
		@param digits An array of booleans telling if that digit is available
	*/
	public void availableDigits(Boolean[] digits) {
	
		message = "digits ";
		
		for (Boolean b:digits) {
			if (b) {
				message += "1";
			}
			
			else {
				message +="0";
			}
		}
		
		message +="\n";
		
		try {
			out.writeBytes(message);
			out.flush();
		}
		
		catch (IOException exc){}
	
	
	}
	
	/**
		Set the score of a player
		@param id the id of the player
		@param score the score of the player
	*/
	public void playerScore(int id, int score) {
	
		message = "score " + id + " " + score + "\n";
		
		try {
			out.writeBytes(message);
			out.flush();
		}
		
		catch (IOException exc){}
	}
	
	
	/**
		Inform the view of whos turn it is
		@param i The id of the players whos turn it is
	*/
	public void playerTurn(int id) {
		message = "turn " + id + "\n";
		
		try {
			out.writeBytes(message);
			out.flush();
		}
		
		catch (IOException exc){}
	
	
	}
	
	/**
		Display the winner of the game (or say its a draw)
		@param winner the id of the winner (0 for draw)
	*/
	public void winner(int winner) {
	
		message = "win " + winner + "\n";
		try {
			out.writeBytes(message);
			out.flush();
			
		}
		
		catch (IOException exc){}
		
	
	
	}
	
	/**
		Quit the game
	*/
	public void quit() {
		message = "quit\n";
		try {
			out.writeBytes(message);
			out.flush();
		}
		
		catch (IOException exc){}
	
	}
	
	private class ReaderThread extends Thread {
		public void run() {
		
			try {
				while (true) {
					String input = in.readLine();
				
					String[] tokens = input.split(" ");
					

					
					switch(tokens[0]) {
						case "join": 
							String name = tokens[1];
							viewListener.joined(ViewProxy.this, name);
							break;
							
						case "digit": 
							int digit = Integer.parseInt(tokens[1]);
							viewListener.digit(digit);
							break;
							
						case "newgame" :
							viewListener.newGame();
							break;
							
						case "quit": 
							viewListener.quit();
							break;
							
						default:
							System.err.println("Bad message");
							break;
					}
					
				}
		
			}
			
			catch (Exception exc) {}
			
			finally {
				try {
					socket.close();
				}
				catch (IOException exc){}
			}
			
		
		}
		
		
		
	}
	
	
}
	
