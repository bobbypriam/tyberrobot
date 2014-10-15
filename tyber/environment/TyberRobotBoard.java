package tyber.environment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.util.datastructure.XYLocation;

public class TyberRobotBoard {

  public static final int ROBOT_ONE = 0;
  public static final int ROBOT_TWO = 1;
  public static final int DUST = 3;
  public static final int PAN = 4;
  public static final int OBSTACLE = 5;

  public static final int UP = 0;
  public static final int DOWN = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 3;

  public static final String[] DIRECTIONS = { "U", "D", "L", "R" };

  private int n;
  private int m;

  XYLocation robotOne;
  XYLocation robotTwo;
  
  private Set<XYLocation> dusts;
  private Set<XYLocation> pans;
  private Set<XYLocation> obstacles;

  public TyberRobotBoard(int n, int m) {
    this.n = n;
    this.m = m;

    dusts = new LinkedHashSet<XYLocation>();
    pans = new LinkedHashSet<XYLocation>();
    obstacles = new LinkedHashSet<XYLocation>();
  }

  public TyberRobotBoard copyWithoutRobotsAndDusts() {
    TyberRobotBoard copy = new TyberRobotBoard(n, m);

    copy.pans = pans;
    copy.obstacles = obstacles;

    return copy;
  }

  public void putElement(XYLocation loc, int type) {
    putElement(loc.getXCoOrdinate(), loc.getYCoOrdinate(), type);
  }

  public void putElement(int x, int y, int type) {
    switch(type) {
      case ROBOT_ONE  : robotOne = new XYLocation(x, y); break;
      case ROBOT_TWO  : robotTwo = new XYLocation(x, y); break;
      case DUST       : dusts.add(new XYLocation(x, y)); break;
      case PAN        : pans.add(new XYLocation(x, y)); break;
      case OBSTACLE   : obstacles.add(new XYLocation(x, y)); break;
    }
  }

  public boolean allDustIsInPan() {
    return dusts.size() == 0;
  }

  public int getNumberOfDusts() {
    return dusts.size();
  }

  public void move(XYLocation element, int direction) {
    XYLocation to = getFinalLocation(element, direction);

    if (element == robotOne)
      robotOne = to;
    else if (element == robotTwo)
      robotTwo = to;
    else {
      removeDust(element);
      if (!pans.contains(to))
        putElement(to, DUST);
    }
  }

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

  public List<XYLocation> getElementSurroundings(XYLocation element) {
    List<XYLocation> surroundings = new ArrayList<XYLocation>();

    for (int dir = UP; dir <= RIGHT; dir++) {
      XYLocation dest = getFinalLocation(element, dir);
      if (isValid(dest))
        surroundings.add(dest);
    }

    return surroundings;
  }

  public List<XYLocation> getDustsAroundRobot(XYLocation robot) {
    List<XYLocation> surroundings = getElementSurroundings(robot);
    List<XYLocation> res = new ArrayList<XYLocation>();
    
    for (XYLocation loc : surroundings)
      if (isDust(loc))
        res.add(loc);
    
    return res;
  }

  public String getStringFromRelativeLocation(XYLocation a, XYLocation b) {
    if (b.equals(getFinalLocation(a, UP)))          return DIRECTIONS[UP];
    else if (b.equals(getFinalLocation(a, DOWN)))   return DIRECTIONS[DOWN];
    else if (b.equals(getFinalLocation(a, LEFT)))   return DIRECTIONS[LEFT];
    else if (b.equals(getFinalLocation(a, RIGHT)))  return DIRECTIONS[RIGHT];

    return null;
  }

  public void removeDust(XYLocation loc) {
    for (XYLocation dust : dusts)
      if (dust.equals(loc)) {
        dusts.remove(dust);
        break;
      }
  }

  public void cleanPan() {
    ArrayList<XYLocation> toBeRemoved = new ArrayList<XYLocation>();
    for (XYLocation dust : dusts)
      if (pans.contains(dust))
        toBeRemoved.add(dust);
    for (XYLocation dust : toBeRemoved)
      dusts.remove(dust);
  }

  public boolean canMove(XYLocation from, XYLocation to) {
    return isValid(to) &&
           // if moving robot, cannot move to dust, or
           ((isRobot(from) && !isDust(to) && !isPan(to)) ||
           // if moving dust, cannot move to robot
           (isDust(from) && !isRobot(to)));
  }

  public boolean isValid(XYLocation loc) {
    return isInBoard(loc) && !isObstacle(loc);
  }

  private boolean isInBoard(XYLocation loc) {
    return loc.getXCoOrdinate() > 0 && loc.getXCoOrdinate() <= n &&
           loc.getYCoOrdinate() > 0 && loc.getYCoOrdinate() <= m;
  }

  private boolean isObstacle(XYLocation loc) {
    for (XYLocation obstacle : obstacles)
      if (obstacle.equals(loc))
        return true;
    return false;
  }

  private boolean isRobot(XYLocation loc) {
    return loc.equals(robotOne) || loc.equals(robotTwo);
  }

  private boolean isDust(XYLocation loc) {
    for (XYLocation dust : dusts)
      if (dust.equals(loc))
        return true;
    return false;
  }

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

  public void getDoableActions() {
      ArrayList<String> resOne = new ArrayList<String>();
      ArrayList<String> resTwo = new ArrayList<String>();

      Set<XYLocation> disabledLocations = new LinkedHashSet<XYLocation>();

      XYLocation[] robots = { robotOne, robotTwo };

      for (XYLocation robot : robots) {
        ArrayList<String> res = (robot == robotOne) ? resOne : resTwo;
        for (XYLocation dust : getDustsAroundRobot(robot))
          for (XYLocation dustAdj : getElementSurroundings(dust))
            if (canMove(dust, dustAdj)) {
              res.add(getStringFromRelativeLocation(robot, dust) +
                      getStringFromRelativeLocation(dust, dustAdj));
              disabledLocations.add(dustAdj);
            }
      }

      for (XYLocation robot : robots) {
        ArrayList<String> res = (robot == robotOne) ? resOne : resTwo;
        for (XYLocation robotAdj : getElementSurroundings(robot))
          if (canMove(robot, robotAdj) && !disabledLocations.contains(robotAdj))
              res.add(getStringFromRelativeLocation(robot, robotAdj));
      }

      System.out.println("Disabled locations: " + disabledLocations);
      System.out.println("Result one: " + resOne);
      System.out.println("Result two: " + resTwo);

      Set<String> actions = new LinkedHashSet<String>();

      for (String one : resOne)
        for (String two : resTwo)
          actions.add(one + "#" + two);

      System.out.println("Actions: " + actions);
    }
}