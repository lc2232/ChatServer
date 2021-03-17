package DOD;
/**
 * 
 * Enum used to determine the movement required when moving in each direction.
 *
 */
 public enum Direction {
   N(0, -1), E(1, 0), S(0, 1), W(-1, 0);
    
	/* Integer value to determine the change in x coordiates on the 2D array when a direction is called*/ 
   private int dx;
   
   /*Integer value to determine the change in y coordiates on the 2D array when a direction is called*/
   private int dy;

  /**
   * Constructor used to create instances of each movement
   * @param dx : change in x coordinates
   * @param dy : change in y coordiantes
   */
   private Direction(int dx, int dy) {
     this.dx = dx;
     this.dy = dy;
   }

  /**
   * Getter for dx
   * @return dx : change in x coordinates
   */
   public int getX() { 
     return dx; 
   }
   
  /**
   * Getter for dy
   * @return dy : change in y coordinates
   */
   public int getY() { 
     return dy; 
   }
   
}