package tyber.environment;

import aima.core.search.framework.HeuristicFunction;
import aima.core.util.datastructure.XYLocation;

public class NearestDustPanManhattanDistanceHeuristicFunction implements HeuristicFunction {
  
  public double h(Object state) {
    TyberRobotBoard board = (TyberRobotBoard) state;
    double minDistance = Double.MAX_VALUE;

    for (XYLocation dust : board.getDusts())
    	for (XYLocation pan : board.getPans()) {
        double distance = computeManhattanDistance(dust, pan);
        minDistance = (minDistance > distance) ? distance : minDistance;
      }

    return minDistance;
  }

  private double computeManhattanDistance(XYLocation a, XYLocation b) {
  	int x1 = a.getXCoOrdinate(), y1 = a.getYCoOrdinate();
  	int x2 = b.getXCoOrdinate(), y2 = b.getYCoOrdinate();
  	return Math.abs(x1-x2) + Math.abs(y1-y2);
  }

}