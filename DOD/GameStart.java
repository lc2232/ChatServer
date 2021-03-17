package DOD;
import java.net.Socket;

/**
 *
 * Class that starts the game by giving the user a selection of maps and calling the
 * relevant functions to start the game.
 *
 */
 public class GameStart {

   /* File-path leading to the file selected by the user*/
   private String fileName;

   /* Creating the instance of GameLogic that is used to dictate the game.*/
   private GameLogic logic;

   private Socket server;

  /**
   * Constructor that finds the path of map file using the selectMapFile method
   * also creates an instance of GameLogic and calls the playGame method on it starting the game.
   */
   public GameStart(Socket server) {
	   System.out.println("Gane straetes");
	// this.server = server;
     logic = new GameLogic(fileName, server);
     logic.start();
   }

}