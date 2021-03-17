package DOD;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Contains the main logic part of the game,
 * as it processes moves from human player and a bot.
 */
 public class GameLogic extends Thread{

   /* Instance of the Map class being created to view the game board.*/
   private Map map;

   /* Another instance being created to help when picking up gold and exit tiles.*/
   private Map originalmap;

   /* Instance of HumanPlayer to be controlled.*/
   private HumanPlayer human;

   /* Instance of BotPlayer to catch the human player.*/
   private BotPlayer bot;

   /* Boolean variable to determine when game finished.*/
   private boolean play;

   private PrintWriter serverOut;

  /**
   * Constructor, creates a new instance of GameLogic,
   * which creates, instances of Map, BotPlayer and HumanPlayer
   * and sets play to true which starts the game.
   *
   * @param fileName : The map file that the player has selected in GameStart
   *
   */
   public GameLogic(String fileName, Socket server) {
	   System.out.println("Gane logigc");
    try {
        map = new Map(server);
	    originalmap = new Map(server); //this map is used by the pickup function.
	    bot = new BotPlayer();
	    human = new HumanPlayer(server);
		serverOut = new PrintWriter(server.getOutputStream(), true);
	    serverOut.println("DOD");
		play = true;
	} catch (IOException e) {
		   System.out.println("IO exceptipon");

		e.printStackTrace();
	}

   }


   public void run() {
	   playGame();
   }

  /**
   * Spawns in the Human and Bot players, prints out the
   * information for the map and decides whos turn it is.
   */
   public void playGame(){
     map.Spawn('P');
     map.Spawn('B');
     map.printMapStats();
     while (play == true){
       int currentTurn =0;
       if((currentTurn %2 ) == 0){ //player starts then alternates between even/odd = human/bot
         currentTurn = human.getTurn();
         playerAction();
       }else {
      // while((currentTurn %2 ) == 1){

         currentTurn = human.getTurn();
         botAction();
       }
     }
   }

  /**
   * Handles the bot players actions based upon the input from getNextAction()
   * This could be a random move or a decided move depending on if
   * the bot has spotted the player yet.
   */
   protected void botAction(){
     switch(bot.getNextAction()) { //switch case statements handling the input from bot move generation
       case "N":
         processMove('N', 'B'); //bots actions get carried out
         break;
       case "W":
         processMove('W', 'B');
         break;
       case "S":
         processMove('S', 'B');
         break;
       case "E":
         processMove('E', 'B');
         break;
       case "L":
         look('B');
         break;
     }
   }

  /**
   * Handles the human players actions based upon their input from getNextAction()
   * and prints out whether a move was successful or not
   */
   protected void playerAction(){
     String success = " ";
     switch(human.getNextAction()) { //switch case determined by userInput in getNextAxtion method.
       case "1":
         look('P');
         break;
       case "2":
         hello();
         break;
       case "3":
         gold();
         break;
       case "4":
         pickup();
         break;
       case "5":
         quitGame();
         break;
       case "N":
         success = processMove('N', 'P');
         sendToServer(success);
         break;
       case "W":
         success = processMove('W', 'P');
         sendToServer(success);
         break;
       case "S":
         success = processMove('S', 'P');
         sendToServer(success);
         break;
       case "E":
         success = processMove('E', 'P');
         sendToServer(success);
         break;
       case "INVALID EXPRESSION": //invalid expressions dealt with here
    	 sendToServer("INVALID EXPRESSION");
         incrementTurn();
         break;
       case "INVALID DIRECTION":
         incrementTurn();
         break;
       case "DOD":
    	   dod();
     }
   }

  /**
   * Increments the static field in the player classes
   */
   protected void incrementTurn() { //increments the static variable.
     int currentTurn = human.getTurn();
     currentTurn = currentTurn +1;
     human.setTurn(currentTurn);
   }

  /**
   * Outputs the gold required to win and increments turn.
   */
   protected void hello() { //processes hello command
     int goldReq = map.getGoldRequired();
     sendToServer("Gold required to win: " + goldReq);
     incrementTurn();
   }

  /**
   * Outputs the amount of gold owned by the human player and increments turn.
   */
   protected void gold() { //processes gold command
     int goldOwn = human.getGold();
     sendToServer("Gold owned: " + goldOwn);
     incrementTurn();
   }

  /**
   * Checks the position the player is about to move to,
   * if the player has chosen to move into the same space as a bot/player
   * then the game uses the quitGame function to end the game as the bot
   *has caught the player.
   *
   * @param direction : The direction of the players movement.
   * @param player : The player that is executing the move. (bot/human)
   * @return true or false depending if the move is valid or not.
   */
   protected boolean checkMove(Direction direction, char player) {
     char[][] grid = map.getMap();
     int[] location = map.find(player);
     int y = location[0];
     int x = location[1];
     int dx = direction.getX(); //uses enum to determine change in direction based on input
     int dy = direction.getY();
     if(y==0){
       return false;
     }else if (grid[y+dy][x+dx] == '.' ||grid[y+dy][x+dx] == 'G'||grid[y+dy][x+dx] == 'E'){
       return true;
     }else if (grid[y+dy][x+dx] == 'B'||grid[y+dy][x+dx] == 'P'){
       sendToServer("Caught by the bot: ");
       quitGame();
       return true;
     }else{
       return false;
     }
   }

  /**
   * Executes the movement of the player on the map and increments turn.
   *
   * @param direction : The direction of the players movement
   * @param player : The player that is executing the move. (bot/human)
   */
   protected void carryOutMove(Direction direction, char player){
     int[] location = map.find(player);
     char[][] gridItems = originalmap.getMap();
     char [][] grid = map.getMap();
     int y = location[0];
     int x = location[1];
     int dx = direction.getX(); //enum used to carry out the move after it has been checked and deemed valid
     int dy = direction.getY();
     grid[y+dy][x+dx]=player;
     grid[y][x]=gridItems[y][x];
     map.setMap(grid);
     incrementTurn();
   }

  /**
   * Checks if movement is legal and updates player's location on the map.
   *
   * @param direction : The direction of the movement.
   * @param player : The player that is executing the move. (bot/human)
   * @returns whether success or not success.
   */
   protected String processMove(char direction, char player) {
     if(direction == 'N'){ //checks each possible direction and carries out appropriate action
       if(checkMove(Direction.N, player)==true){
         carryOutMove(Direction.N, player);
         return("SUCCESS");
       }else{
         return("NOT SUCCESSFUL");
       }
     }
     if(direction == 'S'){
       if(checkMove(Direction.S, player)==true){
         carryOutMove(Direction.S, player);
         return("SUCCESS");
       }else{
         return("NOT SUCCESSFUL");
       }
     }
     if(direction == 'E'){
       if(checkMove(Direction.E, player)==true){
         carryOutMove(Direction.E, player);
         return("SUCCESS");
       }else{
         return("NOT SUCCESSFUL");
       }
     }
     if(direction == 'W'){
       if(checkMove(Direction.W, player)==true){
         carryOutMove(Direction.W, player);
         return("SUCCESS");
       }else{
         return("NOT SUCCESSFUL");
       }
     }
     return("NOT SUCCESSFUL");
   }

  /**
   * Decides if player is on gold by comparing the same location on two maps, one with the player
   * and another without, to see if the player is stood on a 'G' tile.
   *
   * Processes the player's pickup command, updating the instance ("map") of the Map class
   * and the gold amount in the instance ("human") of the Player class.
   * Also increments the player's turn
   */
   protected void pickup() {
     char[][] gridItems = originalmap.getMap();
     int[] location = map.find('P');
     int y = location[0];
     int x = location[1];
     int goldOwned = human.getGold();
     if(gridItems[y][x]=='G'){
    	 sendToServer("GOLD ACQUIRED");
       goldOwned = goldOwned+1;
       human.setGold(goldOwned);
       gridItems[y][x] = '.';
     }else{
    	 sendToServer("NO GOLD TO PICK UP"); //feedback on users input
     }
     incrementTurn();
   }

  /**
   * Quits the game, shutting down the application, and prints out appropriate win/lose message.
   *
   * Decides win/lose by comparing the same location on two different maps, one with the players
   * and another without to determine if they are stood on a 'E'
   */
   protected void quitGame() {
     char[][] gridItems = originalmap.getMap();
     int[] location = map.find('P');
     int playerY = location[0];
     int playerX = location[1];
     int goldOwned = human.getGold();
     int goldReq = map.getGoldRequired();
     if(gridItems[playerY][playerX]=='E' && goldReq == goldOwned){
    	 sendToServer("YOU WIN"); //approprite messages printed based on win condions
       play = false;
     }else{
    	 sendToServer("LOSE");
       play = false; //turn not incremented as ends game whether win or lose
     }
   }

  /**
   * This function generates the 5x5 grid that the user sees when using the look function,
   * it is also used by the bot to find the player character and see its environment.
   * @param player : The character which represents the bot/human controlled player
   * @return lookGrid : Returns the 2D char array that can be seen using the look function
   */
   protected char[][] viewPlayer(char player) {
     int rowCounter = 0;
     int colCounter = 0;
	 char[][] grid = map.getMap();
	 char[][] lookGrid = new char[5][5];
	 int colNum = 20;
	 int rowNum = 9;
	 int[] location = map.find(player);
	 int playerY = location[0];
	 int playerX = location[1];
	 for(int row = playerY-2;row < playerY+3; row++){//height of 5x5 grid
	   for(int col = playerX-2;col < playerX+3; col++){ //length of 5x5 grid
	     if(row>=rowNum||row<=0){
	       lookGrid[rowCounter][colCounter]= '#'; //if outside bounds use #
	     }else if(col>=colNum||col<=0){
	       lookGrid[rowCounter][colCounter]= '#';
	     }else{
	       lookGrid[rowCounter][colCounter]= grid[row][col];
	     }
	     colCounter++;
	   }
	   colCounter = 0;
	   rowCounter++;
	 }
	 return(lookGrid);
   }

  /**
   * prints out a 5x5 grid of the map when the parameter passed is 'P' for the human player
   * when the parameter passed is 'B' for the bot player it scans the 5x5 grid of the map
   * in search for the player character ('P').
   * @param player : A character representing which player has called the function
   *
   */

   /*
   protected void look(char player) {
     char[][] playerGrid = viewPlayer(player);
     char[][] printGrid = new char[5][5];
     for(int row = 0; row<5;row++) {
       for(int col = 0;col<5;col++) {
         if(player == 'B') {
    	   if(playerGrid[row][col]=='P') {
    	     bot.setHumanLocation(row,col); //checks if player is present in the look grid for bot.
    	   }
    	  }else{
    		  printGrid[row][col] = playerGrid[row][col];
    		  //sendToServer(String.valueOf(playerGrid[row][col])); //outputs the look grid for the human player
    	  }
       }
      // if(player!='B') {
    	//   sendToServer(""); //new line
       //}
     }
     System.out.print(playerGrid);
     incrementTurn();
   }
   */

   protected void look(char player) {
	     char[][] playerGrid = viewPlayer(player);
	     String line ="";
	     for(int row = 0; row<5;row++) {
	       for(int col = 0;col<5;col++) {
	         if(player == 'B') {
	    	   if(playerGrid[row][col]=='P') {
	    	     bot.setHumanLocation(row,col); //checks if player is present in the look grid for bot.
	    	   }
	    	  }else{
	    		  line = line + playerGrid[row][col];
	    		  //System.out.println(line + "<- line"); //outputs the look grid for the human player
	    	  }
	       }
	       if(player!='B') {
	    	   try {
				sleep(100);
				sendToServer(line);
		    	   line = "";
	    	   } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    	   }
	       }
	     }
	     incrementTurn();
	  }

   protected synchronized void sendGridToServer(String[][] message) {
			serverOut.println(message);
	}

	protected void dod() {
		try {
			sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    synchronized void sendToServer(String message) {
		//try {
			//PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
    		serverOut.println(message);
		//} catch (IOException e) {
		//	System.out.println("Issues ");
		//	e.printStackTrace();
		//}
	}
}