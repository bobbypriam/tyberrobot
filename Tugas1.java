import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import tyber.environment.TyberRobotBoard;
import tyber.environment.TyberRobotRunner;

// import aima.core.util.datastructure.XYLocation;

public class Tugas1 {
  public static void main(String[] args) throws IOException {

    if (args.length < 3) {
      System.out.println("Usage: java Tugas1 <strategy> <input> <output>");
      System.exit(1);
    }

    String strategyString = args[0];
    String inputFilename = args[1];
    String outputFilename = args[2];

    File inputFile = new File(inputFilename);

    if (!inputFile.exists()) {
      System.err.println("Error: File " + inputFilename + " doesn't exist");
      System.exit(1);
    }

    BufferedReader in = new BufferedReader(new FileReader(inputFile.getAbsoluteFile()));
    StringTokenizer token;
    String output = "";

    try {
      // Board initialization.
      token = new StringTokenizer(in.readLine());
      int n = Integer.parseInt(token.nextToken());
      int m = Integer.parseInt(token.nextToken());
      TyberRobotBoard board = new TyberRobotBoard(n, m);

      // Agents initialization.
      token = new StringTokenizer(in.readLine());
      int x1 = Integer.parseInt(token.nextToken());
      int y1 = Integer.parseInt(token.nextToken());
      int x2 = Integer.parseInt(token.nextToken());
      int y2 = Integer.parseInt(token.nextToken());
      board.putElement(x1, y1, TyberRobotBoard.ROBOT_ONE);
      board.putElement(x2, y2, TyberRobotBoard.ROBOT_TWO);

      // Pan, dust, and obstacle numbers
      token = new StringTokenizer(in.readLine());
      int H = Integer.parseInt(token.nextToken());
      int K = H + Integer.parseInt(token.nextToken());
      int L = K + Integer.parseInt(token.nextToken());
      int i = 0;

      // Elements generation.
      while (i < L) {
        token = new StringTokenizer(in.readLine());
        int x = Integer.parseInt(token.nextToken());
        int y = Integer.parseInt(token.nextToken());
        int type = (i < H) ? TyberRobotBoard.PAN : (i < K) ? TyberRobotBoard.DUST : TyberRobotBoard.OBSTACLE;
        board.putElement(x, y, type);
        i++;
      }
      
      // For debug purposes.
      // board.print();

      TyberRobotRunner runner = new TyberRobotRunner(board);
      int strategy = strategyString.equals("ids") ? TyberRobotRunner.IDS :
                     strategyString.equals("astar1") ? TyberRobotRunner.ASTAR1 :
                     TyberRobotRunner.ASTAR2;
      if (!runner.hasSolution())
        output = "TIDAK ADA SOLUSI";
      else
        output = runner.run(strategy);
      
      // For debug purposes.
      // System.out.println();
      // System.out.println(output);
    } catch (NoSuchElementException e) {
      System.err.println("Error in the input file \'" + inputFilename + "\'. Please check again.");
      e.printStackTrace();
      System.exit(1);
    }

    in.close();

    File outputFile = new File(outputFilename);

    if (!outputFile.exists())
      outputFile.createNewFile();

    BufferedWriter out = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile()));
    out.write(output +"\n");
    out.close(); 
  }
}