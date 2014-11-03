package tyber.environment;

import aima.core.agent.impl.DynamicAction;

/**
 * A class representing the robots' action.
 *
 * @author Widyanto Bagus Priambodo - 1206208315
 */
public class RobotAction extends DynamicAction {
  
  /** Constants for action move up. */
  public static final String MOVE_UP = "U";
  /** Constants for action move down. */
  public static final String MOVE_DOWN = "D";
  /** Constants for action move left. */
  public static final String MOVE_LEFT = "L";
  /** Constants for action move right. */
  public static final String MOVE_RIGHT = "R";

  /** Constants for action sweep up up. */
  public static final String SWEEP_UP_UP = "UU";
  /** Constants for action sweep up left. */
  public static final String SWEEP_UP_LEFT = "UL";
  /** Constants for action sweep up right. */
  public static final String SWEEP_UP_RIGHT = "UR";

  /** Constants for action sweep down down. */
  public static final String SWEEP_DOWN_DOWN = "DD";
  /** Constants for action sweep down left. */
  public static final String SWEEP_DOWN_LEFT = "DL";
  /** Constants for action sweep down right. */
  public static final String SWEEP_DOWN_RIGHT = "DR";

  /** Constants for action sweep left up. */
  public static final String SWEEP_LEFT_UP = "LU";
  /** Constants for action sweep left down. */
  public static final String SWEEP_LEFT_DOWN = "LD";
  /** Constants for action sweep left left. */
  public static final String SWEEP_LEFT_LEFT = "LL";

  /** Constants for action sweep right up. */
  public static final String SWEEP_RIGHT_UP = "RU";
  /** Constants for action sweep right down. */
  public static final String SWEEP_RIGHT_DOWN = "RD";
  /** Constants for action sweep right right. */
  public static final String SWEEP_RIGHT_RIGHT = "RR";

  public static final String DELIMITER = "#";

  /**
   * Creates a robot action.
   *
   * @param type  The type of action.
   */
  public RobotAction(String type) {
    super(type);
  }
}