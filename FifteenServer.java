
//Required for udp networking
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
		Run the main program
		@param args Command line arguments
	*/
	public static void main(String[] args) throws Exception {
	
	
	
		if (args.length != 2) {
			System.err.println("Usage: java FifteenServer <serverhost> <serverport>");
			System.exit(0);
		}
		
		DatagramSocket mailbox = null;
		
		try {
		
			String host = args[0];
			int port = Integer.parseInt (args[1]);

		
			mailbox = new DatagramSocket
				(new InetSocketAddress (host, port));
		}
		
		catch (Exception e) {
			System.err.println("Could not listen on requested address");
			System.exit(0);
		}


		MailboxManager manager = new MailboxManager (mailbox);

		for (;;) {
			manager.receiveMessage();
		}

	
	}
	
}
	
	
	
