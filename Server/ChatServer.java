package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * The class which is responsible for starting the server
 * and creating it's relevant threads to generate responses.
 *
 * This class is also responsible for "listening" to any new
 * clients and organise them.
 *
 */
public class ChatServer {

	/*ServerSocket object which gets the input from clients*/
	private ServerSocket in ;

	/*List of the clients output streams*/
	private List <ClientList> clients;

	/*AtomicBoolean that determines whether the server is on or not
	 * made to be atomic so only one method can change it at once, hence
	 * not leading to any deadlocks etc. */
	public static AtomicBoolean serverOn;

	/**
	 * Default constructor creating a server socket on port 14001.
	 * Creates a new synchronised list object called clients.
	 * Also initialises the serverOn variable, and deals with
	 * relevant exceptions.
	 */
	public ChatServer() {
		try {
			in = new ServerSocket(14001);
			clients = Collections.synchronizedList(new ArrayList <ClientList> ());
			serverOn = new AtomicBoolean(true);
			go();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor which creates a server socket on the provided port csp.
	 * Creates a new synchronised list object called clients.
	 * Also initialises the serverOn variable, and deals with
	 * relevant exceptions.
	 *
	 *  @param csp : Integer value of a port that the server will attach to.
	 */
	public ChatServer(int csp) {
		try {
			in = new ServerSocket(csp);
			clients = Collections.synchronizedList(new ArrayList <ClientList> ());
			serverOn = new AtomicBoolean(true);
			go();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method "listens" for new clients and stores them
	 * in the clientList object.
	 * It also creates the ServerResponse thread which monitors
	 * the clients input and outputs them to all of the other Clients
	 */
	public void go() {
		try {
			while (serverOn.get() == true) {
				System.out.println("Server listening...");
				if (serverOn.get() == true) {
					Socket server = in.accept();
					ClientList newClient = new ClientList(server);
					//Prints the relevant ports the Client has connected to, onto the console
					System.out.println("Server accepted connection on " + in.getLocalPort() + " ; " + server.getPort());
					clients.add(newClient); //adds to list of clients output streams
					new ServerResponse(clients, server);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in .close(); //closes the ServerSocket once the serverOn attribute turns to false
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method finds the port number, by converting the string into an integer
	 *
	 * @param args : Array of Strings passed in by the console as arguments
	 * @return port : The integer value of a port
	 */
	private static int calculatePort(String[] args) {
		try {
			if(args[0].equals("-csp")) {
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
	 * Main function which determines the port of the server by searching through the args
	 * parameter passed into the main by the command line
	 *
	 * @param args : Array of Strings passed in by the console as arguments
	 */
	public static void main(String[] args) {
		if(args.length>0) {
			int port = calculatePort(args);
			new ChatServer(port);
		}else {
			new ChatServer();
		}
	}

}