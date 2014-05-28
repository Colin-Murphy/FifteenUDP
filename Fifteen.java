import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.DatagramSocket;


/**
	Create the socket the user wants to communicate on
	then create the proper objects, set their listeners and let
	the game begin.

	
	@author Colin L Murphy <clm3888@rit.edu>
	@version 4/10/14
*/
public class Fifteen {
	
	/**
		Setup the datagram socket and create the view and model proxy
		@param args command line argument
		@throws IOException on connection error
	*/
	public static void main(String[] args) throws Exception {
	
		//Verify arguments
		if (args.length != 5) {
			usage();
		}
		
		DatagramSocket mailbox = null;
		String serverHost = null;
		int serverPort = 0;
		String clientHost = null;
		int clientPort = 0;
		String name = null;
		
		try {
		
			//Create the socket
			name = args[0];
			clientHost = args[1];
			clientPort = Integer.parseInt(args[2]);
			serverHost = args[3];
			serverPort = Integer.parseInt(args[4]);
		
			mailbox = new DatagramSocket
				(new InetSocketAddress (clientHost, clientPort));
				
		}
		
		catch (Exception e) {
			usage();
		}
		
		//Create the objects and set the appropriate listeners
		final ModelProxy proxy = new ModelProxy(mailbox, 
			new InetSocketAddress (serverHost, serverPort));
		FifteenView view = new FifteenView(name);
		view.setViewListener(proxy);
		proxy.setModelListener(view);
		
		//Handle timeouts
		Runtime.getRuntime().addShutdownHook (new Thread() {
			public void run() {
				proxy.quit();
			}
		});

	

	}
	
	/**
		Print the usage to standard error
	*/
	private static void usage() {
		System.err.println("Usage: java fifteen <name> <clienthost> <clientport> <serverhost> <serverport>");
		System.exit(0);
	}
}
