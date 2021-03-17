package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * This class is used to store the output streams of the Clients
 * so the server can organise the Clients.
 *
 */
public class ClientList {

    /*Socket of the clients*/
    protected Socket client;

    /*PrintWriter object used to write to clients Streams*/
    protected PrintWriter out;

    /**
     * Constructor which sets the local variables to that which is
     * passed as parameters from the ChatServer class
     *
     * @param client : Socket teh client is connected to.
     */
    public ClientList(Socket client) {
        try {
            this.client = client;
            this.out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}