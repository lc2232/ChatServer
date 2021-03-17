package DOD;
import java.util.Random;

/**
 *
 * Generates random movements until a player is spotted when the bot happens to call the look
 * function and can see the player, it then changes movement to try and catch the player
 * This class extends the abstract class Player and makes use of its abstract methods.
 *
 */
 public class BotPlayer extends Player{

   /* Boolean to determine if a player has been spotted yet.*/
   private boolean playerFound;

   /* x coordinate of the human player on the 5x5 look grid.*/
   private int humanX;

   /* y coordinate of the human player on the 5x5 look grid.*/
   private int humanY;

   /* Boolean flag which decides when to look in order to determine where the player has moved to. */
   private boolean justMoved;

  /**
   * BotPlayer constructor which calls the Player super constructor and defaults the humanX/Y values
   * to 0 and the playerFound and justMoved boolean variables to false.
   */
   public BotPlayer(){
     super();
     playerFound = false;
     humanX = 0;
     humanY = 0;
     justMoved = false;
   }

  /**
   * This sets the human player x and y values to coordinates relating to the look grid,
   * and sets playerFound to true, causing the random movement to stop
   *
   * @param row : The row(y) value that that human player currently is
   * @param col : The column(x) value that that human player currently is
   */
   public void setHumanLocation(int row, int col){
     humanX = col;
     humanY = row;
     playerFound = true; //changes the way the bot moves to find the player
   }

  /**
   * Abstract method which determines whether to use random movement or to call humanCatcher
   * which determines the best direction to go in order to catch the player character.
   */
   public String getNextAction(){
     if (playerFound == false){
       return(botRandomMovement());
     }
     else{
       return(humanCatcher());
     }
   }

  /**
   * Picks a random command for the bot player to do, with a higher chance on the look command
   * so it can find the player quickly if it comes within the radius of the look command.
   *
   * returns a random command for the bot to do.
   */
   public String botRandomMovement(){
     Random random = new Random();
     int move = random.nextInt(7);
     if(move==0){
       return "N";
     }else if(move==1){
       return "S";
     }else if(move==2){
       return "E";
     }else if(move==3){
       return "W";
     }else if(move==4|| move == 5||move ==6){ //look has higher % of being used so the bot can find player quicker
       return "L";
     }
     return(null);
   }

  /**
   * Uses pythagoras theorem to calculate which direction is the shortest path to the player character,
   * looking every other turn to determine where the player has now moved to giving new humanX/Y values.
   *
   * returns the direction which is the shortest path to the player
   */
   public String humanCatcher(){
	 //pythagoras used to find length between player and bot
	 //bot position is always at the center of the gird hence why always -2
	 double distanceN = Math.sqrt(Math.pow((humanY-2+1),2)+Math.pow((humanX-2),2));
	 double distanceS = Math.sqrt(Math.pow((humanY-2-1),2)+Math.pow((humanX-2),2));
	 double distanceE = Math.sqrt(Math.pow((humanY-2),2)+Math.pow((humanX-2-1),2));
	 double distanceW = Math.sqrt(Math.pow((humanY-2),2)+Math.pow((humanX-2+1),2));
	 if(justMoved == false) {
	   if(distanceS>=distanceN && distanceN<=distanceE &&distanceN<=distanceW) {
	     justMoved = true;
		 return("N");
	   }else if(distanceS<=distanceN &&distanceS<=distanceE &&distanceS<=distanceW) {
		 justMoved = true;
		 return("S");
	   }else if(distanceE<=distanceS &&distanceE<=distanceN &&distanceE<=distanceW) {
	     justMoved = true;
		 return("E");
	   }else if(distanceW<=distanceS &&distanceW<=distanceN &&distanceW<=distanceE) {
	     justMoved = true;
		 return("W");
	   }else{
	     return("L");
	   }
	 }else{
	   justMoved = false; //looks every other turn to re-establish player location
	   //and gives player the chance to escape if cornered.
	   return("L");
	 }
   }

 }