package Bot;

import Client.ClientServerInput;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * The class that contains the constructors to create the implementations of a Bot
 * This class contains the overriding feature of OOP. Where each different contructor
 * takes diffrent parameters.
 *
 */
public class ChatBot {

	/*Socket connection that links the server to the Client */
	private Socket server;

	/**
	 * Chat Bot constructor which connects the Bot client to the server
	 * and produces response to the relevant exceptions.
	 * Connects the Bot Client to the default localhost and port: 14001
	 */
	public ChatBot() {
		try {
			server = new Socket("localhost", 14001);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
	}

	/**
	 * Overriding method for constructing a bot, if a specific IP address
	 * needs to be connected to.
	 *
	 * @param cca : Allows input of a different IP address
	 */
	public ChatBot(String cca) {
		try {
			server = new Socket(cca, 14001);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}

	/**
	 * Overriding method for constructing a bot, if a specific port number
	 * needs to be connected to.
	 *
	 * @param ccp : Allows input of a different port number if required
	 */
	public ChatBot(int ccp) {
		try {
			server = new Socket("localhost", ccp);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}

	/**
	 * Overriding method for constructing a bot, if a specific port number
	 * and IP address needs to be connected to.
	 *
	 * @param cca : Allows input of a different IP address if required
	 * @param ccp : Allows input of a different port number if required
	 */
	public ChatBot(String cca, int ccp) {
		try {
			server = new Socket(cca, ccp);
			go(server);
		} catch (ConnectException c) {
			System.out.println("No server associated with the socket!");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host, try a different IP address");
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}

	/**
	 * Method that creates threads that generate responses to clients inputs
	 * and receive inputs from the server.
	 *
	 * @param server : The socket that is passed into the threads, giving them
	 * access to the input/output streams of the server.
	 */
	protected void go(Socket server) {
		new ClientBotInput(server);
		new ClientServerInput(server);
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
				new ChatBot(port);
			}else if(args[0].contentEquals("-cca")) {
				String address = calculateAddress(args);
				new ChatBot(address);
			}else {
				new ChatBot(); //default constructor
				System.out.println("You have been resorted to the default constructo as your arguments were invalid");
			}
		}else if(args.length == 4) { //checks both ways
			try {
				String address = args[1];
				String portString = args[3];
				int port = Integer.parseInt(portString.trim());
				new ChatBot(address, port);
			}catch(NumberFormatException e) {
				String address = args[3];
				String portString = args[1];
				int port = Integer.parseInt(portString.trim());
				new ChatBot(address, port);
			}
		}else {
			new ChatBot();
		}
	}
}