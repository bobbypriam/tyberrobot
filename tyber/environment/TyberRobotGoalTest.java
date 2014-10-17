package tyber.environment;

import aima.core.search.framework.GoalTest;

/**
 * A class implementing the goal test. In this problem, a state is in goal
 * state if the all dust is in the pan.
 *
 * @author Widyanto Bagus Priambodo - 1206208315
 */
public class TyberRobotGoalTest implements GoalTest {
  
  @Override
  public boolean isGoalState(Object state) {
    TyberRobotBoard board = (TyberRobotBoard) state;
    return board.allDustIsInPan();
  }

}