import java.net.InetSocketAddress;
import java.net.Socket;


/**
	Create the socket the user wants to communicate on
	then create the proper objects, set their listeners and let
	the game begin.

	
	@author Colin L Murphy <clm3888@rit.edu>
	@version 4/10/14
*/
public class Fifteen {
	
	/**
		Setup the socket and create the view and model proxy
		@param args command line argument
		@throws IOException on connection error
	*/
	public static void main(String[] args) throws Exception {
	
		//Verify arguments
		if (args.length != 3) {
			System.err.println("Usage: java fifteen <name> <host> <port>");
			System.exit(1);
		}
		
		//Create the socket
		String name = args[0];
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		
		Socket s = new Socket();
		s.connect(new InetSocketAddress(host,port));
		
		//Create the objects and set the appropriate listeners
		ModelProxy proxy = new ModelProxy(s);
		FifteenView view = new FifteenView(name);
		view.setViewListener(proxy);
		proxy.setModelListener(view);
	

	}
}
