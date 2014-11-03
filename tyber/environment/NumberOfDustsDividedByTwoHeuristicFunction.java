package tyber.environment;

import aima.core.search.framework.HeuristicFunction;

/**
 * A class implementing the heuristic function counting the number of
 * dust in board divided by two.
 *
 * @author Widyanto Bagus Priambodo - 1206208315
 */
public class NumberOfDustsDividedByTwoHeuristicFunction implements HeuristicFunction {
  
  @Override
  public double h(Object state) {
    TyberRobotBoard board = (TyberRobotBoard) state;
    return board.getNumberOfDusts() / 2.0;
  }

}