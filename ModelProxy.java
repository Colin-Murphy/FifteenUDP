import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Scanner;


/**
	ModelProxy fills in for the Model in this netorked version of fifteen.
	The model proxy communicates to the server over a UDP connection.
	
	@author Colin L Murphy<clm3888@rit.edu>
	@version 5/3/14
*/
public class ModelProxy implements ViewListener {

	private DatagramSocket mailbox;
	private SocketAddress destination;
	private DataOutputStream output;
	private ModelListener modelListener;


	/**
		Create a new ModelProxy
		@param mailbox The mailbox to use
		@param destination The destination address
	*/
	public ModelProxy (DatagramSocket mailbox, SocketAddress destination) {
	
	
		this.mailbox = mailbox;
		this.destination = destination;

	
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream (baos);
			output.writeBytes("join ");
			output.writeBytes(name + "\n");
			output.close();
			byte[] payload = baos.toByteArray();
			mailbox.send (
				new DatagramPacket (payload, payload.length, destination));

		}
		
		catch(IOException e){}
	}
	

	/**
		Player clicked a digtt
		@param digit the digit clicked
	*/
	public void digit(int digit) {
	
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream (baos);
			output.writeBytes("digit ");
			output.writeBytes(digit + "\n");
			output.close();
			byte[] payload = baos.toByteArray();
			mailbox.send (
				new DatagramPacket (payload, payload.length, destination));
	
		}
		
		catch(IOException e){}
	}


	/**
		Player has restarted the game
	*/
	public void newGame() {
	
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream (baos);
			output.writeBytes("newgame\n");
			output.close();
			byte[] payload = baos.toByteArray();
			mailbox.send (
				new DatagramPacket (payload, payload.length, destination));
		
		}
		
		catch(IOException e){}
	}
	
	
	
	/**
		Player quit the game
	*/
	public void quit() {
	
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream (baos);
			output.writeBytes("quit\n");
			output.close();
			byte[] payload = baos.toByteArray();
			mailbox.send (
				new DatagramPacket (payload, payload.length, destination));
		
		}
		
		catch(IOException e){}
	}
	
	
	
	/**
		Hidden class to communicate directly with the server in its
		own class
	*/
	private class ServerCom extends Thread {
		public void run() {
		
			byte[] payload = new byte [128];
		
			try {
			
				while (true) {

					
					//Get the next command
					DatagramPacket packet = new DatagramPacket (payload, payload.length);
					mailbox.receive (packet);
					Scanner input = new Scanner(new DataInputStream 
						(new ByteArrayInputStream (payload, 0, packet.getLength())));	

					String message = input.nextLine();

				
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
				mailbox.close();
			}
			
		}
		
	}
}
