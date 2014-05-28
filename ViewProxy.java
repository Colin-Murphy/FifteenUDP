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
	ViewProxy provides a communication interface for a client and a server
	in the Fifteen game. Communication is done using UDP Datagrams
	
	@author Colin L Murphy <clm3888@rit.edu>
	@version 5/2/14
*/
public class ViewProxy implements ModelListener {

	//Private variables
	private DatagramSocket mailbox;
	private ViewListener viewListener;
	private SocketAddress clientAddress;
	private String message;

	/**
		Create a new view proxy
		@param mailbox The mailbox to listen to for datagrams
		@param clientAddress the return address for the client
	*/
	public ViewProxy(DatagramSocket mailbox, SocketAddress clientAddress) {
		this.mailbox = mailbox;
		this.clientAddress = clientAddress;
	}
	
	 /**
	* Set the view listener object for this view proxy.
	*
	* @param  viewListener  View listener.
	*/
	public void setViewListener (ViewListener viewListener) {
		this.viewListener = viewListener;
	}
	
	/**
		Inform the client it has joined the game, set their id
		@param id The clients id
	*/
	public void joined(int id) {
	
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream (baos);
			message = "id " + id + "\n";
			out.writeBytes(message);
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream (baos);
			out.writeBytes(message);
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
		}
		
		catch (IOException exc){}
	
	}
	
	
	/**
		Tell the player what digits are available
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream (baos);
			out.writeBytes(message);
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream (baos);
			out.writeBytes(message);
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream (baos);
			out.writeBytes(message);
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream (baos);
			out.writeBytes(message);
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
			
		}
		
		catch (IOException exc){}
		
	
	
	}
	
	/**
		Quit the game
	*/
	public void quit() {
		message = "quit\n";
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream (baos);
			out.writeBytes(message);
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
		}
		
		catch (IOException exc){}
	
	}
	
	/**
		Handle the incoming datagram and close the connection if necessary
		@param datagram The incomming datagram
	*/
	public boolean process(DatagramPacket datagram) {
		//Let the mailbox know if this session is ending
		boolean discard = false;
		try {
			Scanner in = new Scanner(new DataInputStream
			(new ByteArrayInputStream(datagram.getData(), 0, datagram.getLength())));
			String input = in.nextLine();
			
		
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
					discard = true;
					break;
					
				default:
					System.err.println("Bad message");
					break;
			}
				

	
		}
		
		catch (Exception exc) {}
		
		return discard;

	}
	
	
}
	
