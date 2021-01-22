package graph.algorithms;

import graph.Configuration;
import graph.Main;
import graph.enums.Algorithm;
import graph.enums.UpperBound;
import graph.exceptions.TimelimitExceededException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Greedy {
    private int V;
    private ArrayList<Integer>[] adj;
    private int[][] adjMatrix;
    private static int[] coloured;

    public Greedy(int[][] adjMatrix, int n) throws TimelimitExceededException {
        this.adjMatrix = adjMatrix;
        this.V = n;
        coloured = new int[V];
        greedyColoring();
    }

    private void greedyColoring() throws TimelimitExceededException {
        Arrays.fill(coloured, -1);
        coloured[0] = 1;

        boolean[] available = new boolean[V];
        Arrays.fill(available, true);

        for (int u = 0; u < V; u++) {
            for (int i = 0; i < V; i++) {
                if (adjMatrix[u][i] == 1) {
                    if (coloured[i] != -1) {
                        available[coloured[i]] = false;
                    }
                }

                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException();
                }
            }

            int cr;
            for (cr = 1; cr < V; cr++) {
                if (available[cr]) {
                    break;
                }

                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException();
                }
            }

            coloured[u] = cr;
            Arrays.fill(available, true);
        }
    }

    public static int [] getColours()
    {
        return coloured;
    }

    public int chromatic() throws TimelimitExceededException {
        int max = 0;
        for (int value : coloured) {
            if (value > max) {
                max = value;
            }

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max);
            }
        }

        return max;
    }

    public static void calculate() {
        if (!Configuration.TRAINING_MODE_ENABLED && Configuration.DEFAULT_UPPER_BOUND.equals(UpperBound.GREEDY))
            Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_UPPER_BOUND_TIMELIMLT);
        try {
            Instant start = Instant.now();
            Greedy greedy = new Greedy(Main.factory.getGraphRepository().getAdjacentMatrix(), Main.factory.getGraphRepository().getVertexCount());
            int chrom = greedy.chromatic();
            double end = Duration.between(start,Instant.now()).toNanos();

            if (Configuration.TRAINING_MODE_ENABLED )
            {
                if (  Main.factory.getGraphRepository().getBest_chromatic() == null || chrom < Main.factory.getGraphRepository().getBest_chromatic())
                {
                    Main.factory.getGraphRepository().setBest_chromatic(chrom);
                    Main.factory.getGraphRepository().setBest_time(end);
                    Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.GREEDY);
                }
                if (Main.factory.getGraphRepository().getBest_chromatic() == null || chrom == Main.factory.getGraphRepository().getBest_chromatic())
                    if (Main.factory.getGraphRepository().getBest_time() == null || end < Main.factory.getGraphRepository().getBest_time()  )
                    {
                        Main.factory.getGraphRepository().setBest_time(end);
                        Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.GREEDY);
                    }
            }

            //can be also both
            if (Configuration.DEFAULT_UPPER_BOUND.equals(UpperBound.GREEDY) )
            {
                Main.factory.getGraphRepository().setUpperBound(chrom);
                if (Configuration.VERBOSE)
                {
                    System.out.print("Greedy upper bound : "+chrom );
                    //System.out.print(" valid coloring : "+ isValidColoring());
                }
            }
            else if (Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.GREEDY))
            {
                Main.factory.getGraphRepository().setChromaticNumber(chrom);
                if (Configuration.VERBOSE)
                {
                    System.out.print("Greedy : "+chrom );
                    System.out.println("  > "+end);
                }
            }

        } catch (TimelimitExceededException e) {
            Main.factory.getGraphRepository().setUpperBound(e.getValue());

            if(Configuration.isDebugging()) {
                e.printStackTrace();
            }
        }

        Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
    }
    public static boolean isValidColoring()
    {
        for (int i = 0; i< coloured.length; i++)
        {
            for (int k = 0; k< coloured.length; k++)
            {
                if (i!= k && Main.factory.getGraphRepository().getAdjacentMatrix()[i][k]==1 && coloured[i]== coloured[k])
                    return false;
            }
        }
        return true;
    }
}
