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

  public TyberRobotBoard copyWithoutRobotsAndDusts(TyberRobotBoard board) {
    TyberRobotBoard copy = new TyberRobotBoard(board.getN(), board.getM());

    copy.pans = board.pans;
    copy.obstacles = board.obstacles;

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

  public void move(XYLocation element, int direction) {
    XYLocation to = getFinalLocation(element, direction);

    if (element == robotOne)
      robotOne = to;
    else if (element == robotTwo)
      robotTwo = to;
    else {
      removeDust(element);
      if (!dusts.contains(to) && !pans.contains(to))
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

  public List<XYLocation> getRobotSurroundings(XYLocation robot) {
    List<XYLocation> surroundings = new ArrayList<XYLocation>();

    surroundings.add(getFinalLocation(robot, UP));
    surroundings.add(getFinalLocation(robot, DOWN));
    surroundings.add(getFinalLocation(robot, LEFT));
    surroundings.add(getFinalLocation(robot, RIGHT));

    return surroundings;
  }

  public String getStringFromRelativeLocation(XYLocation a, XYLocation b) {
    if (b.equals(getFinalLocation(a, UP)))          return STRING_UP;
    else if (b.equals(getFinalLocation(a, DOWN)))   return STRING_DOWN;
    else if (b.equals(getFinalLocation(a, LEFT)))   return STRING_LEFT;
    else if (b.equals(getFinalLocation(a, RIGHT)))  return STRING_RIGHT;

    return null;
  }

  public void removeDust(XYLocation loc) {
    for (XYLocation dust : dusts)
      if (dust.equals(loc)) {
        dusts.remove(dust);
        break;
      }
  }

  public boolean canMove(XYLocation from, XYLocation to) {
    return isBeside(from, to) && isInBoard(to) && !isObstacle(to) &&
           // if moving dust, cannot be the same place as robot, or
           ((!isRobot(from) && !isRobot(to)) ||
           // if moving robot, cannot be the same place as dust
           !isDust(to));
  }

  public boolean isBeside(XYLocation a, XYLocation b) {
    return a.equals(b.up()) ||
           a.equals(b.down()) ||
           a.equals(b.left()) ||
           a.equals(b.right());
  }

  private boolean isInBoard(XYLocation loc) {
    return loc.getXCoOrdinate() > 0 && loc.getXCoOrdinate() <= m &&
           loc.getYCoOrdinate() > 0 && loc.getYCoOrdinate() <= n;
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

  public int              getN()          { return n; }
  public int              getM()          { return m; }
  public XYLocation       getRobotOne()   { return robotOne; }
  public XYLocation       getRobotTwo()   { return robotTwo; }
  public Set<XYLocation>  getDusts()      { return dusts; }
  public Set<XYLocation>  getPans()       { return pans; }
  public Set<XYLocation>  getObstacles()  { return obstacles; }

  public void print() {
    System.out.println("---MAP---");
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= m; j++)
        if (robotOne.equals(new XYLocation(i, j)))
          System.out.print('1');
        else if (robotTwo.equals(new XYLocation(i, j)))
          System.out.print('2');
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

          System.out.print(c);
        }
      System.out.println();
    }
    System.out.println();
    System.out.println("ONE: " + robotOne);
    System.out.println("TWO: " + robotTwo);
    System.out.println("Dusts: " + dusts);
    System.out.println("Pans: " + pans);
    System.out.println("Obstacles: " + obstacles);
  }
}