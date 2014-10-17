package tyber.environment;

import aima.core.util.datastructure.XYLocation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing the Tyber Robot Board. This class acts as the
 * state in state space search and provides fields and methods for
 * helping in searching the solution using aima-java framework.
 *
 * @author Widyanto Bagus Priambodo - 1206208315
 */
public class TyberRobotBoard {

  /** Constants representing robot one. */
  public static final int ROBOT_ONE = 0;
  /** Constants representing robot two. */
  public static final int ROBOT_TWO = 1;
  /** Constants representing dust. */
  public static final int DUST = 3;
  /** Constants representing pan. */
  public static final int PAN = 4;
  /** Constants representing obstacle. */
  public static final int OBSTACLE = 5;

  /** Constant for UP direction. */
  public static final int UP = 0;
  /** Constant for DOWN direction. */
  public static final int DOWN = 1;
  /** Constant for LEFT direction. */
  public static final int LEFT = 2;
  /** Constant for RIGHT direction. */
  public static final int RIGHT = 3;

  /** String constants for direction representations. */
  private static final String[] DIRECTIONS = { "U", "D", "L", "R" };

  /** The width and height of the board. */
  private int n;
  private int m;

  /** Location of the robots */
  private XYLocation robotOne;
  private XYLocation robotTwo;
  
  /** Location of the dusts, pans, and obstacles. */
  private Set<XYLocation> dusts;
  private Set<XYLocation> pans;
  private Set<XYLocation> obstacles;

  /**
   * Constructs a board with size n x m.
   *
   * @param n   Height of board.
   * @param m   Width of board.
   */
  public TyberRobotBoard(int n, int m) {
    this.n = n;
    this.m = m;

    dusts = new LinkedHashSet<XYLocation>();
    pans = new LinkedHashSet<XYLocation>();
    obstacles = new LinkedHashSet<XYLocation>();
  }

  /**
   * Returns a copy of board without the robots and boards.
   *
   * @return the copy of such board.
   */
  public TyberRobotBoard copyWithoutRobotsAndDusts() {
    TyberRobotBoard copy = new TyberRobotBoard(n, m);

    copy.pans = pans;
    copy.obstacles = obstacles;

    return copy;
  }

  /**
   * Puts an element (robots, dusts, pans, or obstacles) to board.
   *
   * @param loc   Location of the element
   * @param type  Type of the element
   */
  public void putElement(XYLocation loc, int type) {
    switch(type) {
      case ROBOT_ONE  : robotOne = loc; break;
      case ROBOT_TWO  : robotTwo = loc; break;
      case DUST       : dusts.add(loc); break;
      case PAN        : pans.add(loc); break;
      case OBSTACLE   : obstacles.add(loc); break;
    }
  }

  /**
   * Puts an element (robots, dusts, pans, or obstacles) to board.
   *
   * @param x     The X coordinate of the element
   * @param y     The Y coordinate of the element
   * @param type  Type of the element
   */
  public void putElement(int x, int y, int type) {
    putElement(new XYLocation(x, y), type);
    
  }

  /**
   * Checks if all dust is already in pan, used in goal test. This method
   * compares if the dusts' size equals to zero.
   *
   * @return true if all dust is in pan, false if otherwise.
   */
  public boolean allDustIsInPan() {
    return dusts.size() == 0;
  }

  /**
   * Returns the number of dusts in board.
   *
   * @return number of dusts.
   */
  public int getNumberOfDusts() {
    return dusts.size();
  }

  /**
   * Returns the translation of the problem's coordinate to XYLocation's
   * coordinate.
   *
   * @param element   The element's location.
   * @param direction The direction to which the element will move.
   * @return the translated location.
   */
  public XYLocation getFinalLocation(XYLocation element, int direction) {
    XYLocation to = null;
    switch (direction) {
      case UP: to = element.left(); break;
      case DOWN: to = element.right(); break;
      case LEFT: to = element.up(); break;
      case RIGHT: to = element.down(); break;
    }
    return to;
  }

  /**
   * Returns a list of valid XYLocations corresponding to the surroundings
   * of an element.
   * 
   * @param element   The element's location.
   * @return the list of the valid surrounding XYLocations.
   */
  public List<XYLocation> getElementSurroundings(XYLocation element) {
    List<XYLocation> surroundings = new ArrayList<XYLocation>();

    for (int dir = UP; dir <= RIGHT; dir++) {
      XYLocation dest = getFinalLocation(element, dir);
      if (isValid(dest))
        surroundings.add(dest);
    }

    return surroundings;
  }

  /**
   * Returns a list of dust locations around a robot.
   * 
   * @param robot   The robot.
   * @return the list of dust around robot.
   */
  public List<XYLocation> getDustsAroundRobot(XYLocation robot) {
    List<XYLocation> surroundings = getElementSurroundings(robot);
    List<XYLocation> res = new ArrayList<XYLocation>();
    
    for (XYLocation loc : surroundings)
      if (isDust(loc))
        res.add(loc);
    
    return res;
  }

  /**
   * Returns the string representation of relative location between
   * two elements (U for up, D for down, L for left, and R for right).
   * 
   * @param a   The first element.
   * @param b   The second element.
   * @return the string representation.
   */
  public String getStringFromRelativeLocation(XYLocation a, XYLocation b) {
    if (b.equals(getFinalLocation(a, UP)))          return DIRECTIONS[UP];
    else if (b.equals(getFinalLocation(a, DOWN)))   return DIRECTIONS[DOWN];
    else if (b.equals(getFinalLocation(a, LEFT)))   return DIRECTIONS[LEFT];
    else if (b.equals(getFinalLocation(a, RIGHT)))  return DIRECTIONS[RIGHT];

    return null;
  }

  /**
   * Cleans the pan; that is, for each dust in dusts, if it is at the same
   * location as one of the pans, then it is removed.
   */
  public void cleanPan() {
    ArrayList<XYLocation> toBeRemoved = new ArrayList<XYLocation>();
    for (XYLocation dust : dusts)
      if (pans.contains(dust))
        toBeRemoved.add(dust);
    for (XYLocation dust : toBeRemoved)
      dusts.remove(dust);
  }

  /**
   * Checks if an element can move to a specific location.
   *
   * @param from  The element's initial location.
   * @param to    The element's intended next location.
   * @return true if the move can be done.
   */
  public boolean canMove(XYLocation from, XYLocation to) {
    return isValid(to) &&
           // if moving robot, cannot move to dust and pan, or
           ((isRobot(from) && !isDust(to) && !isPan(to)) ||
           // if moving dust, cannot move to robot
           (isDust(from) && !isRobot(to)));
  }

  /**
   * Checks if a location is a valid location; that is, it is still
   * in the board and is not an obstacle.
   * 
   * @param loc   The location in question.
   * @return true if the location is valid.
   */
  public boolean isValid(XYLocation loc) {
    return isInBoard(loc) && !isObstacle(loc);
  }

  /**
   * Checks if a location is in the board.
   * 
   * @param loc   The location in question.
   * @return true if the location is in the board.
   */
  private boolean isInBoard(XYLocation loc) {
    return loc.getXCoOrdinate() > 0 && loc.getXCoOrdinate() <= n &&
           loc.getYCoOrdinate() > 0 && loc.getYCoOrdinate() <= m;
  }

  /**
   * Checks if a location is an obstacle.
   * 
   * @param loc   The location in question.
   * @return true if the location is an obstacle.
   */
  private boolean isObstacle(XYLocation loc) {
    for (XYLocation obstacle : obstacles)
      if (obstacle.equals(loc))
        return true;
    return false;
  }

  /**
   * Checks if a location is a robot.
   * 
   * @param loc   The location in question.
   * @return true if the location is a robot.
   */
  private boolean isRobot(XYLocation loc) {
    return loc.equals(robotOne) || loc.equals(robotTwo);
  }

  /**
   * Checks if a location is a dust.
   * 
   * @param loc   The location in question.
   * @return true if the location is a dust.
   */
  private boolean isDust(XYLocation loc) {
    for (XYLocation dust : dusts)
      if (dust.equals(loc))
        return true;
    return false;
  }

  /**
   * Checks if a location is a pan.
   * 
   * @param loc   The location in question.
   * @return true if the location is a pan.
   */
  private boolean isPan(XYLocation loc) {
    for (XYLocation pan : pans)
      if (pan.equals(loc))
        return true;
    return false;
  }

  public int              getN()          { return n; }
  public int              getM()          { return m; }
  public XYLocation       getRobotOne()   { return robotOne; }
  public XYLocation       getRobotTwo()   { return robotTwo; }
  public Set<XYLocation>  getDusts()      { return dusts; }
  public Set<XYLocation>  getPans()       { return pans; }
  public Set<XYLocation>  getObstacles()  { return obstacles; }

  /**
   * [DEBUGGING ONLY] Prints the board.
   */
  public void print() {
    System.out.print("  ");
    for (int i = 1; i <= m; i++)
      System.out.print(i + " ");
    System.out.println();

    for (int i = 1; i <= n; i++) {
      System.out.print(i + " ");
      for (int j = 1; j <= m; j++) {
        if (robotOne.equals(new XYLocation(i, j)))
          System.out.print('A' + " ");
        else if (robotTwo.equals(new XYLocation(i, j)))
          System.out.print('B' + " ");
        else {
          char c = '.';

          for (XYLocation loc : dusts)
            if (loc.equals(new XYLocation(i, j)))
              c = 'D';

          for (XYLocation loc : pans)
            if (loc.equals(new XYLocation(i, j)))
              c = 'P';
          
          for (XYLocation loc : obstacles)
            if (loc.equals(new XYLocation(i, j)))
              c = 'O';

          System.out.print(c + " ");
        }
      }
      System.out.println();
    }
  }
}