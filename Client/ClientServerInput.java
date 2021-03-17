package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * This Thread class, is used to read the input from the Clients input
 * stream and print it out to the Clients console.
 *
 */
public class ClientServerInput extends Thread {

	/*Integer which is the number of milliseconds to sleep the thread for*/
	private static final int DELAY = 10;

	/*BufferedReader object used to read what the server is writing to clients
	 * input streams*/
	private BufferedReader serverIn;

	/**
	 * Constructor which starts the thread and sets the BufferedReader object
	 *
	 * @param server : Socket object which related to the server.
	 */
	public ClientServerInput(Socket server) {
		try {
			serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method which is called from starting the thread, and runs the getServerIn method.
	 */
	public void run() {
		getServerIn();
	}

	/**
	 * Reads the input stream of the Client Socket and outputs it to the console of the Client,
	 * then sleeps the thread as well as dealing with any exceptions.
	 */
	private void getServerIn() {
		try {
			String serverRes = "";
			while ((serverRes = serverIn.readLine()) != null) {
				System.out.println(serverRes);
				sleep(DELAY);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NullPointerException n) {
			System.out.println("The server you are trying to connect to does not exist or is closed");
		} catch (SocketException s) {
			System.out.println("Server terminated");
		} catch (IOException i) {
			System.out.println("Problems with IO");
			i.printStackTrace();
		}
	}

}