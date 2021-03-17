package DOD;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * Reads and contains in memory the map of the game along side its properties
 * such as gold required to win, name and the amount of rows, columns of the map.
 *
 */
public class Map {

	/* Representation of the map */
	private char[][] map;

	/* Map name */
	private String mapName;

	/* Gold required for the human player to win */
	private int goldRequired;

	/* Number of rows in the map */
    private int row;

    /* Number of columns in the map */
    private int column;

    private Socket server;

	 public Map(Socket server) {
			mapName = "Very small Labyrinth of Doom";
			goldRequired = 2;
			row = 9;
			column=20;
			map = new char[][]{
			{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
			};
			this.server = server;
		}

	/**
	 * reads through the file and finds how many lines there are in total.
	 *
	 * @param fileName : the file-path of the file that is being accessed
	 * @return rows : the number of lines present in the file
	 * @throws Exception : NullPointerException being thrown in case the file-path is invalid
	 */
	 protected int countRow(String fileName)throws Exception{
	   BufferedReader reader = new BufferedReader(new FileReader(fileName));
	   int rows = 0;
	   while (reader.readLine() != null) rows++;
	     reader.close(); //closing input stream
	     return rows;
	   }

	/**
	 * Reads the first two lines, to skip over them then finds the length of the first
	 * row of # in the map.
	 *
	 * @param fileName : the file-path of the file that is being accessed
	 * @return col : the number of columns present in the map section of the file (length of the map)
	 * @throws Exception : NullPointerException being thrown in case the file-path is invalid
	 */
	 protected int countColumn(String fileName)throws Exception{
	   BufferedReader reader = new BufferedReader(new FileReader(fileName));
	   reader.readLine();
	   reader.readLine();
	   String line = reader.readLine();
	   int col = line.length(); //find length of first line of '#' on the map.
	   reader.close();
	   return(col);
	 }

	/**
	 * Prints the maps name, gold required to win and the size of the map.
	 */
	 public void printMapStats() {
		sendToServer("Dungeon Name: "+ mapName);
		sendToServer("Gold Required: "+ goldRequired);
		sendToServer("Map Size: "+ row+"x"+column);
	 }

    /**
	 * spawns the player in random location on the map, as long as nothing
	 * is already present at that point on the map.
	 *
	 * @param item : a character that should be spawned on the map.
     */
     protected void Spawn(char item){
       boolean validPosition = false;
       while(validPosition == false){ //keeps generating random numbers until a valid position is found
         int randomRow = (int)(Math.random()*(((row-1)-1)+1))+1; //random number from 1 to max row number
         int randomColumn = (int)(Math.random()*(((column-1)-1)+1))+1;//random number from 1 to max column number
         char tile = map[randomRow][randomColumn];
         if(tile == '.' || tile == 'E'||tile=='P'||tile=='B'){
           map[randomRow][randomColumn] = item;
           validPosition = true;
         }
       }
     }

    /**
	 * finds first location of the given char on the map when read from left to right,
	 * one row at a time. This is used for characters that only occur once, such as
	 * 'P' and 'B'.
	 *
	 * @param letter : the character that we are looking for the location of
     * @return location : returns an array of integers of length 2, the first integer is the
     * 					  first 2D array value and the second in the second 2D array value.
     */
     protected int[] find(char letter){
       int [] location = {0,0}; //iterates through the map, until it reaches the searched for letter.
         for(int y = 0; y<row;y++){
           for(int x =0; x<column;x++){
             if(map[y][x] == letter){
               location[0]= y;
               location[1]= x;
               return location;
             }
           }
         }
       return location;
      }

    /**
     * @return : Gold required to exit the current map.
     */
     protected int getGoldRequired() {
        return goldRequired;
     }

    /**
     * @return map : The map as stored in memory.
     */
     protected char[][] getMap() {
        return map;
     }

    /**
     * sets the instance of Map called with this to a new/updated form of the map.
     * @param newMap : an updated 2D character representation of the map
     */
     public void setMap(char[][] newMap) {
       this.map = newMap;
     }

    /**
     * @return row : the number of lines in the text file.
     */
     protected int getRow(){
       return row;
     }

    /**
     * @return column : the length of one line of the map.
     */
     protected int getColumn(){
       return column;
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
