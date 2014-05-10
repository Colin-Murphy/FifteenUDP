

//ONLY NEEDED IN TCP VERSION, REMOVE IN UDP VERSION
import java.net.InetSocketAddress;
import java.net.DatagramSocket;
/**
	Runs the Fifteen Server to the specifications defined by COPADS
	project 4.
	
	@author Colin L Murphy <clm3888@rit.edu>
	@version 5/2/14
	
*/


public class FifteenServer {
	
	/**
		Main program
	*/
	public static void main(String[] args) throws Exception {
	
		System.out.println(args.length);
	
		if (args.length != 2) {
			System.err.println("Usage: java FifteenServer <serverhost> <serverport>");
			System.exit(0);
		}
		
		String host = args[0];
		int port = Integer.parseInt (args[1]);

		DatagramSocket mailbox = new DatagramSocket
			(new InetSocketAddress (host, port));


		MailboxManager manager = new MailboxManager (mailbox);

		for (;;) {
			manager.receiveMessage();
		}
		/*
		ServerSocket serversocket = new ServerSocket();
		serversocket.bind (new InetSocketAddress (host, port));
		
		SessionManager manager = new SessionManager();
		
		while (true) {
			Socket socket = serversocket.accept();
			ViewProxy proxy = new ViewProxy (socket);
			proxy.setViewListener (manager);
		}
		
		*/

	
	}
	
}
	
	
	
