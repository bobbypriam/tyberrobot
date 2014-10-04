package tyber.environment;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

public class RobotAction extends DynamicAction {
  public static final String MOVE_UP = "U";
  public static final String MOVE_DOWN = "D";
  public static final String MOVE_LEFT = "L";
  public static final String MOVE_RIGHT = "R";

  public static final String SWEEP_UP_UP = "UU";
  public static final String SWEEP_UP_LEFT = "UL";
  public static final String SWEEP_UP_RIGHT = "UR";

  public static final String SWEEP_DOWN_DOWN = "DD";
  public static final String SWEEP_DOWN_LEFT = "DL";
  public static final String SWEEP_DOWN_RIGHT = "DR";

  public static final String SWEEP_LEFT_UP = "LU";
  public static final String SWEEP_LEFT_DOWN = "LD";
  public static final String SWEEP_LEFT_LEFT = "LL";

  public static final String SWEEP_RIGHT_UP = "RU";
  public static final String SWEEP_RIGHT_DOWN = "RD";
  public static final String SWEEP_RIGHT_RIGHT = "RR";

  /**
   * Creates a robot action.
   */
  public RobotAction(String type) {
    super(type);
  }
}