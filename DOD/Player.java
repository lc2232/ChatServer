package DOD;

/**
 *
 * Abstract class player containing static and abstract methods related 
 * to the player sub-classes
 *
 */

public abstract class Player{

   /* static integer variable turn used to calculate whether its
    * the bot's turn or human player turn, defined as static so it
    * is the same across all subclasses.*/
    private static int turn;


   /**
    * super constructor for player class sets turn to default value of 0.
    */
    public Player(){
      turn = 0;
    }

   /**
    * @return turn: the turn that the game is currently on
    */
    protected int getTurn() {
      return turn;
    }

   /**
    * Sets current turn to parameter.
    * @param newTurn : the new incremented value of turn
    */
    protected static void setTurn(int newTurn){
      turn = newTurn;
    }



   /**
    * Processes the command. It should return a reply in form of a String, as the protocol dictates.
    * Otherwise it should return the string "Invalid".
    *
    * @return Processed output or Invalid if the parameter command is wrong.
    */
    public abstract String getNextAction(); //abstract so can be changed depending on which class calls it.

}

