package tyber.environment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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

      ArrayList<String> resOne = new ArrayList<String>();
      ArrayList<String> resTwo = new ArrayList<String>();

      Set<XYLocation> disabledLocations = new LinkedHashSet<XYLocation>();

      XYLocation[] robots = { robotOne, robotTwo };

      for (XYLocation robot : robots) {
        ArrayList<String> res = (robot == robotOne) ? resOne : resTwo;
        for (XYLocation dust : board.getDustsAroundRobot(robot))
          for (XYLocation dustAdj : board.getElementSurroundings(dust))
            if (board.canMove(dust, dustAdj)) {
              res.add(board.getStringFromRelativeLocation(robot, dust) +
                      board.getStringFromRelativeLocation(dust, dustAdj));
              disabledLocations.add(dustAdj);
            }
      }

      for (XYLocation robot : robots) {
        ArrayList<String> res = (robot == robotOne) ? resOne : resTwo;
        for (XYLocation robotAdj : board.getElementSurroundings(robot))
          if (board.canMove(robot, robotAdj) && !disabledLocations.contains(robotAdj))
              res.add(board.getStringFromRelativeLocation(robot, robotAdj));
      }

      for (String one : resOne)
        for (String two : resTwo)
          actions.add(new RobotAction(one + "#" + two));

      return actions;
    }
  }

  private static class TRResultFunction implements ResultFunction {
    public Object result(Object s, Action a) {
      if (a instanceof RobotAction) {
        RobotAction ra = (RobotAction) a;
        TyberRobotBoard board = (TyberRobotBoard) s;
        TyberRobotBoard newBoard = board.copyWithoutRobotsAndDusts();

        XYLocation robotOne = board.getRobotOne();
        XYLocation robotTwo = board.getRobotTwo();
        XYLocation[] robots = { robotOne, robotTwo };

        StringTokenizer tok = new StringTokenizer(ra.getName(), RobotAction.DELIMITER);

        String robotOneAction = tok.nextToken();
        String robotTwoAction = tok.nextToken();

        List<XYLocation> removedDusts = new ArrayList<XYLocation>();

        // Handle sweep actions.
        for (XYLocation robot : robots) {
          String action = (robot == robotOne) ? robotOneAction : robotTwoAction;

          XYLocation oldDust = null;
          XYLocation newDust = null;
          if (action.equals(RobotAction.SWEEP_UP_UP))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.UP)), TyberRobotBoard.UP);
          else if (action.equals(RobotAction.SWEEP_UP_LEFT))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.UP)), TyberRobotBoard.LEFT);
          else if (action.equals(RobotAction.SWEEP_UP_RIGHT))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.UP)), TyberRobotBoard.RIGHT);
          else if (action.equals(RobotAction.SWEEP_DOWN_DOWN))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.DOWN)), TyberRobotBoard.DOWN);
          else if (action.equals(RobotAction.SWEEP_DOWN_LEFT))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.DOWN)), TyberRobotBoard.LEFT);
          else if (action.equals(RobotAction.SWEEP_DOWN_RIGHT))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.DOWN)), TyberRobotBoard.RIGHT);
          else if (action.equals(RobotAction.SWEEP_LEFT_UP))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.LEFT)), TyberRobotBoard.UP);
          else if (action.equals(RobotAction.SWEEP_LEFT_DOWN))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.LEFT)), TyberRobotBoard.DOWN);
          else if (action.equals(RobotAction.SWEEP_LEFT_LEFT))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.LEFT)), TyberRobotBoard.LEFT);
          else if (action.equals(RobotAction.SWEEP_RIGHT_UP))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.RIGHT)), TyberRobotBoard.UP);
          else if (action.equals(RobotAction.SWEEP_RIGHT_DOWN))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.RIGHT)), TyberRobotBoard.DOWN);
          else if (action.equals(RobotAction.SWEEP_RIGHT_RIGHT))
            newDust = board.getFinalLocation((oldDust = board.getFinalLocation(robot, TyberRobotBoard.RIGHT)), TyberRobotBoard.RIGHT);

          if (oldDust != null)
            removedDusts.add(oldDust);
          if (newDust != null)
            newBoard.putElement(newDust, TyberRobotBoard.DUST);
        }

        for (XYLocation dust : board.getDusts())
          if (!newBoard.getDusts().contains(dust) && !removedDusts.contains(dust))
            newBoard.putElement(dust, TyberRobotBoard. DUST);

        newBoard.cleanPan();

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

        // TyberRobotGoalTest test = new TyberRobotGoalTest();
        // System.out.print("A: " + robotOneAction + "#" + robotTwoAction);
        // System.out.print(", D: " + newBoard.getNumberOfDusts());
        // System.out.println(", G: " + test.isGoalState(newBoard));
        // newBoard.print();
        // System.out.println();

        // try { 
        //   Thread.sleep(2000);
        // } catch (Exception e) {}

        s = newBoard;
      }
      return s;
    }
  }
}