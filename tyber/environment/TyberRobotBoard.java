package tyber.environment;

import java.util.LinkedHashSet;
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
  public static final int RIGHT = 2;
  public static final int LEFT = 3;

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

  public void putRobots(int x1, int y1, int x2, int y2) {
    robotOne = new XYLocation(x1, y1);
    robotTwo = new XYLocation(x2, y2);
  }

  public void putElement(XYLocation loc, int type) {
    putElement(loc.getXCoOrdinate(), loc.getYCoOrdinate(), type);
  }

  public void putElement(int x, int y, int type) {
    switch(type) {
      case DUST     : dusts.add(new XYLocation(x, y)); break;
      case PAN      : pans.add(new XYLocation(x, y)); break;
      case OBSTACLE : obstacles.add(new XYLocation(x, y)); break;
    }
  }

  public boolean allDustIsInPan() {
    return dusts.size() == 0;
  }

  public void move(XYLocation element, int direction) {
    XYLocation to = getDirection(element, direction);

    if (element == robotOne)
      robotOne = to;
    else if (element == robotTwo)
      robotTwo = to;
    else {
      removeDust(element);
      if (!dusts.contains(to))
        putElement(to, DUST);
    }
  }

  public XYLocation getDirection(XYLocation element, int direction) {
    XYLocation to = null;
    switch (direction) {
      case UP: to = element.left(); break;
      case DOWN: to = element.right(); break;
      case LEFT: to = element.up(); break;
      case RIGHT: to = element.down(); break;
    }
    return to;
  }

  public void removeDust(XYLocation loc) {
    for (XYLocation dust : dusts)
      if (dust.equals(loc)) {
        dusts.remove(dust);
        break;
      }
  }

  public boolean isValidMove(XYLocation loc) {
    return isInBoard(loc) && isNotObstacle(loc);
  }

  private boolean isInBoard(XYLocation loc) {
    return loc.getXCoOrdinate() > 0 && loc.getXCoOrdinate() <= m &&
           loc.getYCoOrdinate() > 0 && loc.getYCoOrdinate() <= n;
  }

  private boolean isNotObstacle(XYLocation loc) {
    for (XYLocation obstacle : obstacles)
      if (obstacle.equals(loc))
        return false;
    return true;
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