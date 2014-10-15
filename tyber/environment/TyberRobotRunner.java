package tyber.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

import aima.core.util.datastructure.XYLocation;

public class TyberRobotRunner {
  
  public static final int IDS = 0;
  public static final int ASTAR1 = 1;
  public static final int ASTAR2 = 2;

  private TyberRobotBoard board;

  public TyberRobotRunner(TyberRobotBoard board) {
    this.board = board;
  }

  public String run(int strategy) {
    String output = "";
    switch (strategy) {
      case IDS:
        output = runIDSSearch();
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

  public boolean hasSolution() {
    char[][] map = new char[board.getN()][board.getM()];

    for (int i = 0; i < board.getN(); i++)
      for (int j = 0; j < board.getM(); j++) {
        XYLocation now = new XYLocation(i+1, j+1);
        char c = '.';
        
        if (now.equals(board.getRobotOne()) ||
            now.equals(board.getRobotTwo()))
          c = 'R';

        for (XYLocation loc : board.getDusts())
          if (now.equals(loc)) c = 'D';

        for (XYLocation loc : board.getPans())
          if (now.equals(loc)) c = 'P';

        for (XYLocation loc : board.getObstacles())
          if (now.equals(loc)) c = 'O';

        map[i][j] = c;
      }

    boolean hasSolution = true;
    for (XYLocation dust : board.getDusts()) {
      boolean[][] visited = new boolean[board.getN()][board.getM()];
      hasSolution = checkForSolution(map, visited, dust.getXCoOrdinate() - 1, dust.getYCoOrdinate() - 1);
      if (!hasSolution) break;
    }

    return hasSolution;
  }

  private boolean checkForSolution(char[][] map, boolean[][] visited, int x, int y) {
    if (!isInBoard(map, x, y) || map[x][y] == 'O' || map[x][y] == 'R' || visited[x][y])
      return false;

    if (map[x][y] == 'P')
      return true;

    visited[x][y] = true;

    return checkForSolution(map, visited, x, y-1) || checkForSolution(map, visited, x+1, y) ||
           checkForSolution(map, visited, x, y+1) || checkForSolution(map, visited, x-1, y);
  }

  private boolean isInBoard(char[][] map, int x, int y) {
    return x >= 0 && y >= 0 && x < map.length && y < map[0].length;
  }

  private String runIDSSearch() {
    try {
      Problem problem = new Problem(board, TyberRobotFunctionFactory
        .getActionsFunction(), TyberRobotFunctionFactory
        .getResultFunction(), new TyberRobotGoalTest());
      Search search = new IterativeDeepeningSearch();
      SearchAgent agent = new SearchAgent(problem, search);
      String output = "";
      output += getPathCost(agent.getInstrumentation()) + "\n";
      output += getActionNames(agent.getActions());
      return output;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

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
      return output;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

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
      return output;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

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
}