package tyber.environment;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

public class TyberRobotFunctionFactory {
  private static ActionsFunction _actionsFunction = null;
  private static ResultFunction _resultFunction = null;

  public static ActionsFunction getActionsFunction() {
    if (null == _actionsFunction) {
      _actionsFunction = new TRActionsFunction();
    }
    return _actionsFunction;
  }

  public static ResultFunction getResultFunction() {
    if (null == _resultFunction) {
      _resultFunction = new TRResultFunction();
    }
    return _resultFunction;
  }
  
  private static class TRActionsFunction implements ActionsFunction {
    public Set<Action> actions(Object state) {
      TyberRobotBoard board = (TyberRobotBoard) state;

      Set<Action> actions = new LinkedHashSet<Action>();

      // to be implemented
      // possible actions:
      // 1. move: up, down, left, right
      // 2. sweep: up-up, up-down, up-left, up-right, etc.

      return actions;
    }
  }

  private static class TRResultFunction implements ResultFunction {
    public Object result(Object s, Action a) {
      if (a instanceof RobotAction) {
        RobotAction ra = (RobotAction) a;
        TyberRobotBoard board = (TyberRobotBoard) s;
        TyberRobotBoard newBoard = new TyberRobotBoard(board.getN(), board.getM());

        // to be implemented
        // resulting states after action a

        s = newBoard;
      }
      return s;
    }
  }
}