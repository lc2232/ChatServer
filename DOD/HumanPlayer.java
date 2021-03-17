package DOD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Reads the player inputs and controls the human players character as it 
 * moves around the board.
 *
 * Extends the abstract class Player and utilises the abstract method getNextAction.
 */
 public class HumanPlayer extends Player{

   /* Buffered reader to take user inputs. */
   private BufferedReader in;

   /* Integer value for the gold currently owned by the player. */
   private int gold;

    private Socket server;

    private BufferedReader chatMessages;

  /**
   * Constructor for the HumanPlayer class with calls to the Player super constructor,
   * defines start values of gold and opens an input stream for the Buffered Reader.
   */
   public HumanPlayer(Socket server){
	   super();
	     try {
	         this.server = server;
	         gold =0;
	         in = new BufferedReader(new InputStreamReader(System.in));
			chatMessages = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }

  /**
   * Reads player's input from the console and catches IOexceptions from the console.
   *
   * @return userInput : A string containing the input the human user entered.
   */
   protected String getInputFromConsole(){
     String userInput = "";
     try{
       userInput = in.readLine();
     }
     catch(IOException e){ //catches invalid inputs
       System.out.println(e);
     }
     return userInput;
   }

   /**
    * @return : Gold the player currently has.
    */
    protected int getGold() {
      return gold;
    }

   /**
    * Sets current gold.
    */
    protected void setGold(int newGold){
      this.gold = newGold;
    }




   /**
    * Processes the command
    * Otherwise it returns the string "INVALID EXPRESSION" or "INVALID DIRECTION" if N/S/E/W
    * has not been entered as a direction.
    *
    * @return : returns either with Invalid string, a direction to move, or a number which
    * 			relates to switch case statement and applies that command.
    */
    public String getNextAction() {
    	String fullServerInput = getServerIn();
    	while((fullServerInput != null) && (!(fullServerInput.substring(0,3)).equals("DOD"))) {
	      String userInput = splitServerIn(fullServerInput);
	        if(userInput.length()<4){ //no valid command is less than 4 chars in length
	          return ("INVALID EXPRESSION");
	        }else if (userInput.equalsIgnoreCase("LOOK")){ //compares users input to the command strings
	           return "1";
	        }else if (userInput.equalsIgnoreCase("HELLO")){
	          return "2";
	        }else if (userInput.equalsIgnoreCase("GOLD")){
	          return "3";
	        }else if (userInput.equalsIgnoreCase("PICKUP")){
	          return "4";
	        }else if (userInput.equalsIgnoreCase("DOD")){
	            return "DOD";
	        }else if ((userInput.substring(0,4).equalsIgnoreCase("MOVE"))){ //substring used to find move
	          String direction = userInput.substring(5,userInput.length());
	          if(direction.equalsIgnoreCase("N")){
	            return "N";
	          }else if(direction.equalsIgnoreCase("S")){
	            return "S";
	          }else if(direction.equalsIgnoreCase("E")){
	            return "E";
	          }else if(direction.equalsIgnoreCase("W")){
	            return "W";
	          }else{
	            sendToServer("UNSUCCESSFUL - Invalid Direction (N/S/E/W)"); //relevant unsuccessful outputs
	            return ("INVALID DIRECTION");
	          }
	       }else if (userInput.equalsIgnoreCase("QUIT")){
	         return "5";
	       }else{
	         return ("INVALID EXPRESSION");
	       }
    	}
      return("DOD");
    }

    private String getServerIn() {
		try {
			String chatMessage = chatMessages.readLine();
			while((chatMessage != null) && (!(chatMessage.substring(0,3)).equals("DOD"))) {
					return(chatMessage);
			}
		}catch (IOException e) {
			System.out.println("Server closed");
			System.exit(0);
		}
		//System.out.println("DOD instruction");
		return null;
	}

    private String splitServerIn(String fullServerInput) {
    	try {
    		if((fullServerInput != null) && (!(fullServerInput.substring(0,3)).equals("DOD"))){
    			String[] parts = fullServerInput.split("\\s+", 2);
		        return(parts[1]);
    		}
    	}catch(ArrayIndexOutOfBoundsException e){
    		return(null);
    	}
		return null;
	}

    protected synchronized  void sendToServer(String message) {
		try {
			PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
			serverOut.println(message);
		} catch (IOException e) {
			System.out.println("Issues ");
			e.printStackTrace();
		}
	}

 }


