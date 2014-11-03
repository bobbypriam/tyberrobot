package tyber.environment;

import aima.core.search.framework.HeuristicFunction;
import aima.core.util.datastructure.XYLocation;
import java.util.ArrayList;

/**
 * A class implementing the heuristic function that computes the manhattan distance
 * of the dusts to the nearest pan and returns the biggest value.
 *
 * @author Widyanto Bagus Priambodo - 1206208315
 */
public class FurthestDustPanManhattanDistanceHeuristicFunction implements HeuristicFunction {
  
  @Override
  public double h(Object state) {
    TyberRobotBoard board = (TyberRobotBoard) state;
    ArrayList<Double> distances = new ArrayList<Double>();

    for (XYLocation dust : board.getDusts()) {
      double minDistance = Double.MAX_VALUE;
    	for (XYLocation pan : board.getPans()) {
        double distance = computeManhattanDistance(dust, pan);
        minDistance = (minDistance > distance) ? distance : minDistance;
      }
      distances.add(minDistance);
    }

    double maxDistance = Double.MIN_VALUE;
    for (Double d : distances)
      maxDistance = (d > maxDistance) ? d : maxDistance;

    return maxDistance;
  }

  private double computeManhattanDistance(XYLocation a, XYLocation b) {
  	int x1 = a.getXCoOrdinate(), y1 = a.getYCoOrdinate();
  	int x2 = b.getXCoOrdinate(), y2 = b.getYCoOrdinate();
  	return Math.abs(x1-x2) + Math.abs(y1-y2);
  }

}