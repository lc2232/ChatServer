package Server;

import Bot.ChatBot;
import DOD.DungeonsOfDoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerResponse extends Thread {

	/*Socket object for the server */
	protected Socket server;

	/*List object containing the object ClientList, named clients*/
	protected List <ClientList> clients;

	/*Integer value for the number of milliseconds for the thread to sleep for*/
	protected static final int DELAY = 100;

	/*InputStreamReader object which used to read the servers input streams*/
	protected InputStreamReader r;

	public AtomicBoolean DODActive;

	/**
	 * Thread constructor which initialises all of the attributes, as well as starting the thread.
	 *
	 * @param clients : List of clients input Streams
	 * @param server : socket object which joins the client and server
	 */
	public ServerResponse(List<ClientList> clients, Socket server) {
		this.server = server;
		this.clients = clients;
		DODActive = new AtomicBoolean(false);;
		start();
	}

	/**
	 * Method that is called after starting the thread, which gets the username from
	 * the client and sends the responses to the other clients
	 */
	public void run() {
		String username = getInput();
		sendResponse(username);
	}

	/**
	 * This method reads the users input and does the relevant operations to either
	 * quit the server, or to simply output their message to the rest
	 * of the Clients.
	 *
	 * @param username : this is the username that has been picked by the client, and that
	 * will be associated with their message
	 */
	protected void sendResponse(String username) {
		try {
			while(ChatServer.serverOn.get() == true) {
				String userInput = getInput();
				if (userInput.equals("EXIT")){
					sendToClients("I have closed the server!", username);
					serverClose();
				}else if (userInput.equals("BOT") && countClients()==1 && DODActive.get() == false) {
					serverMessageSender("Chat Bot Online!");
					new ChatBot();
				}else if (userInput.equals("JOIN") && countClients()==1) {
					DODActive.set(true);
	                new DungeonsOfDoom();
				}else if ((userInput.equals("JOIN") && countClients()>1)){
					serverMessageSender("DOD can only be played when one client is in the chat room");
				}else if (userInput != null & userInput.length() > 0) {
	                sendToClients(userInput,username);
	                sleep(DELAY);
				}
			}
		} catch (Exception e) {
			System.out.println("A client has disconnected");
        }
	}


	/**
	 * This method read the input stream for the server and returns it as a String to the
	 * method that has called it.
	 *
	 * @return : String message obtained from the Clients.
	 */
	protected String getInput() {
		try {
			r = new InputStreamReader(server.getInputStream());
			BufferedReader clientIn = new BufferedReader(r);
			String userInput = clientIn.readLine();
			return(userInput);
		}catch(SocketException s) {
			return("Server is Closed");
		} catch (IOException e) {
			e.printStackTrace();
			return("Error with IO");
		}
	}

	/**
	 * Send to clients method, that loops through the clientList and writes to each
	 * of their input streams, with the clients that sent the message's username and the message
	 *
	 * @param message : The String of the clients message
	 * @param username : The String username of the Client that sent the messsage into the server
	 */
	protected synchronized void sendToClients(String message, String username) {
		for (ClientList client : clients) {
       	 	client.out.print(username +": ");
            client.out.println(message);
            client.out.flush();

       }
	}

	/**
	 * Send to clients method, that loops through the clientList and writes to each
	 * of their input streams, with the server as a username
	 *
	 * @param message : The String of the clients message
	 */
	protected synchronized void serverMessageSender(String message) {
		for (ClientList client : clients) {
       	 	client.out.print("Server: ");
            client.out.println(message);
            client.out.flush();
       }
	}


	/**
	 * This method is responsible for closing the server and sends to the clients appropriate
	 * messages which indicate to them that the server is closed.
	 * It also changes the serverOn attribute from the ChatServer which flags down all other server
	 * loops.
	 */
	protected void serverClose() {
		try {
			serverMessageSender("The server is shutting down...");
			serverMessageSender("Your messages will no longer be able to send/recieve messages");
			ChatServer.serverOn.set(false);
	        server.close();
	        System.out.println("The server has been shut down");
	        System.exit(0);
	    } catch (IOException e) {
	    	serverMessageSender("Failed to shut down");
	    }
	}

	/**
	 * This method iterates through the client list and increments a counter each time,
	 * which counts the number of clients the server currently has connected.
	 *
	 * @return numOfClients : An integer value of the number of connected clients.
	 */
	protected synchronized int countClients() {
		int numOfClients = 0;
		for (ClientList client : clients) {
       	 	numOfClients++;
       }
		return(numOfClients);
	}


}

