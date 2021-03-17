package DOD;

import Client.ClientServerInput;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * This class connects the DOD client to the server and creates the thread
 * objects required to run the game.
 *
 */
public class DungeonsOfDoom {

	/*Socket object of the server*/
	private Socket server;

	/**
	 * Constructor of the DOD client which connects it to the server
	 * and deals with the appropriate exceptions.
	 *
	 * This constructor runs off the default localhost and the default port 14001
	 */
	public DungeonsOfDoom() {
		try {
			server = new Socket("localhost", 14001);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor of the DOD client which connects it to the server
	 * and deals with the appropriate exceptions.
	 *
	 * This constructor uses the passed cca and ccp arguments
	 */
	public DungeonsOfDoom(String cca, int ccp) {
		try {
			server = new Socket(cca, ccp);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor of the DOD client which connects it to the server
	 * and deals with the appropriate exceptions.
	 *
	 * This constructor uses the passed cca and default port
	 */
	public DungeonsOfDoom(String cca) {
		try {
			server = new Socket(cca, 14001);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor of the DOD client which connects it to the server
	 * and deals with the appropriate exceptions.
	 *
	 * This constructor uses the passed ccp and default localhost address
	 */
	public DungeonsOfDoom(int ccp) {
		try {
			server = new Socket("localhost", ccp);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates the threads which start the game and obtain input from the
	 * server.
	 *
	 * @param server : Socket object which connects the server and client
	 */
	public void go(Socket server) {
		new ClientServerInput(server);
		new GameStart(server);
	}

	/**
	 * This method finds the port number, by converting the string into an integer
	 *
	 * @param args : Array of Strings passed in by the console as arguments
	 * @return port : The integer value of a port
	 */
	private static int calculatePort(String[] args) {
		try {
			if(args[0].equals("-ccp")) {
				String portString = args[1];
				int port = Integer.parseInt(portString.trim());
				return(port);
			}else {
				return 14001; //returns default port if cannot find the correct port
			}
		}catch(NumberFormatException n) {
			System.out.println("You have been given the default constructor as you did not enter an integer number for a port");
			return 14001;
		}
	}

	/**
	 * This method finds the address number, by converting the string into an integer
	 *
	 * @param args : Array of Strings passed in by the console as arguments
	 * @return address : The String value of an address
	 */
	private static String calculateAddress(String[] args) {
		if(args[0].equals("-cca")) {
			String address = args[1];
			return(address);
		}else {
			return null; //returns default port if cannot find the correct port
		}
	}

	/**
	 * Main function which determines the port and addresses of the server by searching through the args
	 * parameter passed into the main by the command line
	 *
	 * @param args : Array of Strings passed in by the console as arguments
	 */
	public static void main(String[] args) {
		if(args.length == 2) {
			if(args[0].contentEquals("-ccp")){
				int port = calculatePort(args);
				new DungeonsOfDoom(port);
			}else if(args[0].contentEquals("-cca")) {
				String address = calculateAddress(args);
				new DungeonsOfDoom(address);
			}else {
				new DungeonsOfDoom();
				System.out.println("You have been resorted to the default constructo as your arguments were invalid");
			}
		}else if(args.length == 4) { //checks both ways
			try {
				String address = args[1];
				String portString = args[3];
				int port = Integer.parseInt(portString.trim());
				new DungeonsOfDoom(address, port);
			}catch(NumberFormatException e) {
				String address = args[3];
				String portString = args[1];
				int port = Integer.parseInt(portString.trim());
				new DungeonsOfDoom(address, port);
			}
		}else {
			new DungeonsOfDoom();
		}
	}

}