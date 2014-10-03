package tyber.environment;

import aima.core.search.framework.GoalTest;

public class TyberRobotGoalTest implements GoalTest {
  
  public boolean isGoalState(Object state) {
    TyberRobotBoard board = (TyberRobotBoard) state;
    return board.allDustIsInPan();
  }

}