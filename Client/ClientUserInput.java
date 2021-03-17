package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * This Thread class gets the input from the Users input to the console
 * and sends it to the server.
 *
 */
public class ClientUserInput extends Thread {

	/*Socket object which connects the server and the clients I/O streams*/
	private Socket server;

	/*Integer value of the number of milliseconds to sleep the thread for*/
	private static final int DELAY = 100;

	/**
	 * Instantiates the server Socket and starts the thread.
	 *
	 * @param server : Socket object of the servers I/O streams
	 */
	public ClientUserInput(Socket server) {
		this.server = server;
		start();
	}

	/**
	 * Method that is called by starting the thread, also asks the user for their
	 * username and gets their input.
	 */
	public void run() {
		System.out.println("Enter your username: ");
		getUserIn();
	}

	/**
	 * Reads the console, of the Clients input and sends their input to the server
	 * Then sleeps the Thread and deals with the relevant exceptions.
	 */
	private void getUserIn() {
		try {
			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			String userInput = "";
			while ((userInput = userIn.readLine()) != null) {
				sendToServer(userInput);
				sleep(DELAY);
			}
		} catch (IOException e) {
			System.out.println("Exception occurred while getting input ");
			e.printStackTrace();
		} catch (InterruptedException q) {
			System.out.println("Exception occurred when interrupting");
			q.printStackTrace();
		}
	}

	/**
	 * Synchronised method which accesses the servers Output Stream and writes a
	 * message to it.
	 *
	 * @param message : A String which is to be sent to the server by the Client
	 */
	private synchronized void sendToServer(String message) {
		try {
			PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
			serverOut.println(message);
		} catch (IOException e) {
			System.out.println("Issues ");
			e.printStackTrace();
		}
	}

}