package tyber.environment;

import aima.core.agent.Action;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

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

  private String runIDSSearch() {
    try {
      Problem problem = new Problem(board, TyberRobotFunctionFactory
        .getActionsFunction(), TyberRobotFunctionFactory
        .getResultFunction(), new TyberRobotGoalTest());
      Search search = new IterativeDeepeningSearch();
      SearchAgent agent = new SearchAgent(problem, search);
      printActions(agent.getActions());
      return "IDS"; // placeholder
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String runAStar1Search() {
    try {
      Problem problem = new Problem(board, TyberRobotFunctionFactory
        .getActionsFunction(), TyberRobotFunctionFactory
        .getResultFunction(), new TyberRobotGoalTest());
      Search search = new AStarSearch(new TreeSearch(),
        new NumberOneHeuristicFunction());
      SearchAgent agent = new SearchAgent(problem, search);
      printActions(agent.getActions());
      return "ASTAR1"; // placeholder
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String runAStar2Search() {
    try {
      Problem problem = new Problem(board, TyberRobotFunctionFactory
        .getActionsFunction(), TyberRobotFunctionFactory
        .getResultFunction(), new TyberRobotGoalTest());
      Search search = new AStarSearch(new TreeSearch(),
        new NumberTwoHeuristicFunction());
      SearchAgent agent = new SearchAgent(problem, search);
      printActions(agent.getActions());
      return "ASTAR2"; // placeholder
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void printActions(List<Action> actions) {
    for (int i = 0; i < actions.size(); i++) {
      String action = actions.get(i).toString();
      System.out.println(action);
    }
  }
}