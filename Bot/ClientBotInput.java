package Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 *
 * This class is a thread which sets up the Bot by giving it the
 * username BOT, as well as printing a greeting statement.
 * Once the Bot has been set up it then monitors the inputs of the user
 * and produces a relevant output, which could be random or purposeful
 * depending on what the other clients have said.
 *
 */
public class ClientBotInput extends Thread {

	/*Socket of the server that has been passed by the ChatBot constructor method*/
	private Socket server;

	/*Integer value of the number of milliseconds the thread sleeps for*/
	private static final int DELAY = 50;

	/**
	 * Constructor which starts the thread and sets the Socket to the server
	 *
	 * @param server : The Socket that was passed when connecting to the
	 * server through the ChatBot method
	 */
	public ClientBotInput(Socket server) {
		this.server = server;
		start();
	}

	/**
	 * This method sets up the Bot by giving it an appropriate
	 * username and producing a greeting.
	 * It then runs the getServerIn method, and sleeps the thread for the DELAY
	 * number of seconds.
	 * It also catches the relevant exceptions that may be thrown when attempting to
	 * sleep the thread.
	 */
	public void run() {
		sendToServer("BOT"); //sets the username to BOT
		while (true) {
			try {
				getServerIn();
				sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Synchronised method which accesses the servers Output Stream and writes a
	 * message to it.
	 *
	 * @param message : A String which is to be sent to the server by the bot
	 */
	private synchronized void sendToServer(String message) {
		try {
			PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
			serverOut.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the most recent line the server has output and sends the generated response back to the server,
	 * for the other clients to read.
	 * It does this by creating a Buffered Reader object and accessing the input stream of the socket.
	 *
	 * It also interrupts the thread if the Socket closes or becomes unreadable.
	 *
	 */
	private void getServerIn() {
		try {
			BufferedReader chatMessages = new BufferedReader(new InputStreamReader(server.getInputStream()));
			String chatMessage = "";
			//if there is a line sent by the server then a response is generated
			while ((chatMessage = chatMessages.readLine()) != null) {
				sendToServer(generateResponse(chatMessage));
			}
		} catch (IOException e) {
			System.out.println("Server closed");
			interrupt();
		}
	}

	/**
	 * This method creates the response by cheching what the other Clients have said and responding, with eitehr a
	 * pre-determined response or a random one.
	 *
	 * @param userInput : A String of the most recent input from the Client.
	 * @return : A String response to the userInput string.
	 */
	private String generateResponse(String userInput) {
		if (userInput.contains("?")) {
			return ("I am too dumb to reply to questions :/");
		} else if (userInput.toLowerCase().contains("hey") || userInput.toLowerCase().contains("hello") || userInput.toLowerCase().contains("hi")) {
			return ("Hello!");
		} else {
			switch (generateRandom()) { //Switch/case used to send random responses back to user.
				case 0:
					return ("Oh ok...");

				case 1:
					return ("Yeah, I agree");

				case 2:
					return ("lmao");

				case 3:
					return ("No I don't agree");

				case 4:
					return ("Very interesting point you are making!");

				case 5:
					return ("I see why someone may think that");

				case 6:
					return ("Sounds plausable to me");

				case 7:
					return ("Some people I know have said similar");
			}
		}
		return ("Yes"); //defaults to this response if an error occurs with generating a random number
	}

	/**
	 * Generates a random number between 0 and 7, using the Random object.
	 *
	 * @return : The random integer between 0 and 7.
	 */
	private static int generateRandom() {
		Random rand = new Random();
		int randInt = rand.nextInt(7);
		return (randInt);
	}

}