import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;



/**
	ModelProxy fills in for the Model in this netorked version of fifteen.
	The model proxy communicates to the server over a TCP connection.
	
	@author Colin L Murphy<clm3888@rit.edu>
	@version 4/12/14
*/
public class ModelProxy implements ViewListener {

	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private ModelListener modelListener;


	/**
		Create a new ModelProxy
		@param socket The socket used for communications
	*/
	public ModelProxy(Socket socket) {
	
		
		//Create the input and output streams
		try {
	
			this.socket = socket;
			output = new DataOutputStream(this.socket.getOutputStream());
			input = new DataInputStream(this.socket.getInputStream());
		}
		
		catch(IOException e){}
	
	}
	
	/**
		Set the model listener to listen to
		@param modelListener The model listener to listen to
	*/
	public void setModelListener(ModelListener modelListener) {
		this.modelListener = modelListener;
		new ServerCom() .start();
	}
	
	
	
	/**
		Player joined the server
		@param name players name
	*/
	public void joined(ViewProxy proxy, String name) {
	
		try {
			output.writeBytes("join ");
			output.writeBytes(name + "\n");
			output.flush();
		}
		
		catch(IOException e){}
	}
	

	/**
		Player clicked a digtt
		@param digit the digit clicked
	*/
	public void digit(int digit) {
	
		try {
			output.writeBytes("digit ");
			output.writeBytes(digit + "\n");
			output.flush();
	
		}
		
		catch(IOException e){}
	}


	/**
		Player has restarted the game
	*/
	public void newGame() {
	
		try {
			output.writeBytes("newgame\n");
			output.flush();
		
		}
		
		catch(IOException e){}
	}
	
	
	
	/**
		Player quit the game
	*/
	public void quit() {
	
		try {
			output.writeBytes("quit\n");
			output.flush();
		
		}
		
		catch(IOException e){}
	}
	
	
	
	/**
		Hidden class to communicate directly with the server in its
		own class
	*/
	private class ServerCom extends Thread {
		public void run() {
		
			try {
				//Go forever
				while(true) {
					
					//Get the next command		

					String message = input.readLine();
					
					
					String[] tokens = message.split(" ");
					
					
					
					//Define id and name outside of the switch so it wont throw errors from multiple definitions
					int id;
					String name;
					
					
					//Handle the command appropriately
					switch(tokens[0]) {
						case "id":
							id = Integer.parseInt(tokens[1]);
							modelListener.joined(id);
							break;
						
						case "name":
							id = Integer.parseInt(tokens[1]);
							name = tokens[2];
							
							modelListener.playerInfo(id, name);
							break;
						
						case "digits":
							String digits = tokens[1];

							Boolean[] available = new Boolean[digits.length()];
							
							for (int i=0;i<available.length;i++) {
							
								String s = digits.charAt(i) + "";
								if (s.equals("1")) {

									available[i] = true;
								}
								
								else {

									available[i] = false;
								}
								
								
							}
							modelListener.availableDigits(available);
							break;
						
						case "score":
							id = Integer.parseInt(tokens[1]);
							int score = Integer.parseInt(tokens[2]);
							modelListener.playerScore(id,score);
							break;
						
						case "turn": 
							id = Integer.parseInt(tokens[1]);
							modelListener.playerTurn(id);
							break;
						
						case "win":
							id = Integer.parseInt(tokens[1]);
							modelListener.winner(id);
							break;
						
						case "quit":
							modelListener.quit();
							break;
					}
				
				}
				
			}
			
			catch (IOException exc) {}
			
			finally {
				try {
					socket.close();
				}
				catch (IOException exc){}
			}
			
		}
		
	}
}
