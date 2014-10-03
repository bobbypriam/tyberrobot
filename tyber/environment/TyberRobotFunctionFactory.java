package tyber.environment;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;
import aima.core.util.datastructure.XYLocation;

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

      XYLocation robotOne = board.getRobotOne();
      XYLocation robotTwo = board.getRobotTwo();
      List<XYLocation> robotOneSurroundings = board.getRobotSurroundings(robotOne);
      List<XYLocation> robotTwoSurroundings = board.getRobotSurroundings(robotTwo);

      Set<XYLocation> dusts = board.getDusts();
      for (XYLocation dust : dusts)
        if (isBeside(robotOne, dust))

      return actions;
    }
  }

  private static class TRResultFunction implements ResultFunction {
    public Object result(Object s, Action a) {
      if (a instanceof RobotAction) {
        RobotAction ra = (RobotAction) a;
        TyberRobotBoard board = (TyberRobotBoard) s;
        TyberRobotBoard newBoard = new TyberRobotBoard(board);

        XYLocation robotOne = board.getRobotOne();
        XYLocation robotTwo = board.getRobotTwo();

        StringTokenizer tok = new StringTokenizer(ra.getName());

        String robotOneAction = tok.nextToken();
        String robotTwoAction = tok.nextToken();

        // Handle move actions.
        XYLocation newRobotOne = robotOne;
        XYLocation newRobotTwo = robotTwo;

        if (robotOneAction.equals(RobotAction.MOVE_UP))
          newRobotOne = board.getFinalLocation(robotOne, TyberRobotBoard.UP);
        else if (robotOneAction.equals(RobotAction.MOVE_DOWN))
          newRobotOne = board.getFinalLocation(robotOne, TyberRobotBoard.DOWN);
        else if (robotOneAction.equals(RobotAction.MOVE_LEFT))
          newRobotOne = board.getFinalLocation(robotOne, TyberRobotBoard.LEFT);
        else if (robotOneAction.equals(RobotAction.MOVE_RIGHT))
          newRobotOne = board.getFinalLocation(robotOne, TyberRobotBoard.RIGHT);

        if (robotTwoAction.equals(RobotAction.MOVE_UP))
          newRobotTwo = board.getFinalLocation(robotTwo, TyberRobotBoard.UP);
        else if (robotTwoAction.equals(RobotAction.MOVE_DOWN))
          newRobotTwo = board.getFinalLocation(robotTwo, TyberRobotBoard.DOWN);
        else if (robotTwoAction.equals(RobotAction.MOVE_LEFT))
          newRobotTwo = board.getFinalLocation(robotTwo, TyberRobotBoard.LEFT);
        else if (robotTwoAction.equals(RobotAction.MOVE_RIGHT))
          newRobotTwo = board.getFinalLocation(robotTwo, TyberRobotBoard.RIGHT);

        // Place robots in new positions.
        newBoard.putElement(newRobotOne, TyberRobotBoard.ROBOT_ONE);
        newBoard.putElement(newRobotTwo, TyberRobotBoard.ROBOT_TWO);

        s = newBoard;
      }
      return s;
    }
  }
}