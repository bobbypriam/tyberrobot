package tyber.environment;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.util.datastructure.XYLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * A class that can run the search using the strategy defined.
 * This class also checks whether an initial state has a solution
 * before actually running the search.
 *
 * @author Widyanto Bagus Priambodo - 1206208315
 */
public class TyberRobotRunner {
  
  /** Constants for the iterative deepening search strategy. */
  public static final int IDS = 0;
  /** Constants for the first A* search strategy. */
  public static final int ASTAR1 = 1;
  /** Constants for the second A* search strategy. */
  public static final int ASTAR2 = 2;

  private static final char ROBOT     = 'R';
  private static final char DUST      = 'D';
  private static final char PAN       = 'P';
  private static final char OBSTACLE  = 'O';
  private static final char EMPTY     = '.';

  /** The initial board. */
  private TyberRobotBoard board;

  /** 
   * Construct a runner from a board.
   *
   * @param board   The board.
   */
  public TyberRobotRunner(TyberRobotBoard board) {
    this.board = board;
  }

  /**
   * Runs the search using a specific strategy.
   *
   * @param strategy  The strategy used for the search (IDS, ASTAR1, ASTAR2).
   * @return the path cost and resulting actions (or a string "TIDAK ADA SOLUSI"
   *         if no solution is available).
   */
  public String run(int strategy) {
    if (!hasSolution())
      return "TIDAK ADA SOLUSI";

    String output = "";

    switch (strategy) {
      case IDS:
        output = runIDS();
        break;
      case ASTAR1:
        output = runAStar1Search();
        break;
      case ASTAR2:
        output = runAStar2Search();
        break;
    }
    return output;
  }

  /**
   * Checks if the board has a solution. This method initializes a map
   * from the board and for each dust it checks if there exists a way
   * from it to a pan.
   *
   * @return true if the board has a solution.
   */
  private boolean hasSolution() {
    char[][] map = new char[board.getN()][board.getM()];

    for (int i = 0; i < board.getN(); i++)
      for (int j = 0; j < board.getM(); j++) {
        XYLocation now = new XYLocation(i+1, j+1);
        char c = EMPTY;
        
        if (now.equals(board.getRobotOne()) ||
            now.equals(board.getRobotTwo()))
          c = ROBOT;

        for (XYLocation loc : board.getDusts())
          if (now.equals(loc)) c = DUST;

        for (XYLocation loc : board.getPans())
          if (now.equals(loc)) c = PAN;

        for (XYLocation loc : board.getObstacles())
          if (now.equals(loc)) c = OBSTACLE;

        map[i][j] = c;
      }

    boolean hasSolution = true;
    for (XYLocation dust : board.getDusts()) {
      int x = dust.getXCoOrdinate() - 1;
      int y = dust.getYCoOrdinate() - 1;

      boolean hasPathToPan = checkForSolution(map, new boolean[board.getN()][board.getM()], x, y, PAN);
      boolean hasPathToRobot = checkForSolution(map, new boolean[board.getN()][board.getM()], x, y, ROBOT);

      hasSolution = hasPathToPan && hasPathToPan;
      if (!hasSolution) break;
    }

    return hasSolution;
  }

  /**
   * Recursively flood fills the map using DFS algorithm to check for a way
   * from dust to a pan.
   *
   * @param map     The map to search.
   * @param visited A boolean array keeping track of visited squares.
   * @param x       The x coordinate of the current square.
   * @param y       The y coordinate of the current square.
   * @return true if there exists a way from dust to a pan.
   */
  private boolean checkForSolution(char[][] map, boolean[][] visited, int x, int y, char goal) {
    
    char bound = (goal == PAN) ? ROBOT : PAN;
    
    if (!isInBoard(map, x, y) || map[x][y] == OBSTACLE || map[x][y] == bound || visited[x][y])
      return false;

    if (map[x][y] == goal)
      return true;

    visited[x][y] = true;

    return checkForSolution(map, visited, x, y-1, goal) || checkForSolution(map, visited, x+1, y, goal) ||
           checkForSolution(map, visited, x, y+1, goal) || checkForSolution(map, visited, x-1, y, goal);
  }

  /**
   * Checks if a coordinate is in the map.
   * 
   * @param map   The map.
   * @param x     The x coordinate.
   * @param y     The y coordinate.
   * @return true if the coordinate is in the map.
   */
  private boolean isInBoard(char[][] map, int x, int y) {
    return x >= 0 && y >= 0 && x < map.length && y < map[0].length;
  }

  /**
   * Runs the IDS search.
   *
   * @return the path cost and actions.
   */
  private String runIDS() {
    try {
      Problem problem = new Problem(board, TyberRobotFunctionFactory
        .getActionsFunction(), TyberRobotFunctionFactory
        .getResultFunction(), new TyberRobotGoalTest());
      Search search = new IterativeDeepeningSearch();
      SearchAgent agent = new SearchAgent(problem, search);
      String output = "";
      output += getPathCost(agent.getInstrumentation()) + "\n";
      output += getActionNames(agent.getActions());
      System.out.println(getNumberOfNodesExpanded(agent.getInstrumentation()));
      return output;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Runs the A* search with the NumberOfDust heuristic.
   *
   * @return the path cost and actions.
   */
  private String runAStar1Search() {
    try {
      Problem problem = new Problem(board, TyberRobotFunctionFactory
        .getActionsFunction(), TyberRobotFunctionFactory
        .getResultFunction(), new TyberRobotGoalTest());
      Search search = new AStarSearch(new TreeSearch(),
        new NumberOfDustsHeuristicFunction());
      SearchAgent agent = new SearchAgent(problem, search);
      String output = "";
      output += getPathCost(agent.getInstrumentation()) + "\n";
      output += getActionNames(agent.getActions());
      System.out.println(getNumberOfNodesExpanded(agent.getInstrumentation()));
      return output;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Runs the A* search with the FurthestDustPanManhattanDistance heuristic.
   *
   * @return the path cost and actions.
   */
  private String runAStar2Search() {
    try {
      Problem problem = new Problem(board, TyberRobotFunctionFactory
        .getActionsFunction(), TyberRobotFunctionFactory
        .getResultFunction(), new TyberRobotGoalTest());
      Search search = new AStarSearch(new TreeSearch(),
        new FurthestDustPanManhattanDistanceHeuristicFunction());
      SearchAgent agent = new SearchAgent(problem, search);
      String output = "";
      output += getPathCost(agent.getInstrumentation()) + "\n";
      output += getActionNames(agent.getActions());
      System.out.println(getNumberOfNodesExpanded(agent.getInstrumentation()));
      return output;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Gets the actions in the desired string format.
   *
   * @param actions   The list of actions.
   * @return the string representation of the actions.
   */
  private String getActionNames(List<Action> actions) {
    ArrayList<String> act1 = new ArrayList<String>();
    ArrayList<String> act2 = new ArrayList<String>();
    
    for (int i = 0; i < actions.size(); i++) {
      StringTokenizer tok = new StringTokenizer(((DynamicAction) actions.get(i)).getName(), RobotAction.DELIMITER);
      act1.add(tok.nextToken());
      act2.add(tok.nextToken());
    }

    String output = "";
    for (String action : act1)
      output += action + " ";
    output += "\n";
    for (String action : act2)
      output += action + " ";

    return output;
  }

  /**
   * Returns the path cost of a search.
   *
   * @param properties  The agent's instrumentation.
   * @return the path cost.
   */
  private String getPathCost(Properties properties) {
    String res = "";
    Iterator<Object> keys = properties.keySet().iterator();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      if (key.equals("pathCost"))
        res = "" + (int) Double.parseDouble(properties.getProperty(key));
    }
    return res;
  }

  /**
   * Returns the number of nodes expanded in a search.
   *
   * @param properties  The agent's instrumentation.
   * @return the number of expanded nodes.
   */
  private String getNumberOfNodesExpanded(Properties properties) {
    String res = "";
    Iterator<Object> keys = properties.keySet().iterator();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      if (key.equals("nodesExpanded"))
        res = "" + Integer.parseInt(properties.getProperty(key));
    }
    return res;
  }
}