package tyber.environment;

import aima.core.search.framework.HeuristicFunction;

public class NumberOfDustsDividedByTwoHeuristicFunction implements HeuristicFunction {
  
  public double h(Object state) {
    TyberRobotBoard board = (TyberRobotBoard) state;
    return board.getNumberOfDusts() / 2;
  }

}