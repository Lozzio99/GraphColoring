package graph.algorithms.BackTracking;
import graph.Configuration;
import graph.Main;
import graph.enums.Algorithm;
import graph.exceptions.TimelimitExceededException;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class BTChromaticNumber {
    private static int MAX_ITERATIONS = 500000;
    public  int [][] graph;
    public int [] route;
    public static int [][] BESTGRAPH;
    public static int [] BESTROUTE;
    public static int chrom;
    public static double END;

    public BTChromaticNumber(int[][] matrix)
    {
        this.graph = matrix;
    }

    protected void execute() throws TimelimitExceededException {

        Instant START = Instant.now();

        this.route = new int[Main.factory.getGraphRepository().getVertexCount()];
        for (int i = 0; i< Main.factory.getGraphRepository().getVertexCount(); i++)
        {
            this.route[i] = i;
        }
        // Calculate the chromatic number
        BackTracking bt = new BackTracking (this.graph);
        chrom = bt.chromatic();
        if (Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.BACKTRACKING))
        {
            Main.factory.getGraphRepository().setChromaticNumber(chrom);
        }
        END = Duration.between(START,Instant.now()).toNanos();
        if (Configuration.VERBOSE)
        {
            System.out.print("Backtracking : "+ chrom);
            //System.out.print(" valid coloring : "+ bt.isValidColoring());
            System.out.println("  "+END);
        }
        if (Configuration.TRAINING_MODE_ENABLED)
        {
            if (  Main.factory.getGraphRepository().getBest_chromatic() == null || chrom < Main.factory.getGraphRepository().getBest_chromatic())
            {
                Main.factory.getGraphRepository().setBest_chromatic(chrom);
                Main.factory.getGraphRepository().setBest_time(END);
                Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.BACKTRACKING);
            }
            if (Main.factory.getGraphRepository().getBest_chromatic() == null || chrom == Main.factory.getGraphRepository().getBest_chromatic())
                if (Main.factory.getGraphRepository().getBest_time() == null || END < Main.factory.getGraphRepository().getBest_time()  )
                {
                    Main.factory.getGraphRepository().setBest_time(END);
                    Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.BACKTRACKING);
                }
        }
        else if (Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.BACKTRACKING))
        {
            Main.factory.getGraphRepository().setChromaticNumber(chrom);
        }
        if (chrom == Main.factory.getGraphRepository().getLowerBound())
            return;
        int lastIteration = 0;
        int result = 0;
        for (int i = 0;i< MAX_ITERATIONS ;i++)      //O(500.000)
        {
            if (i-lastIteration < 5000)
            {
                Casual random = new Casual (this.graph, this.route, 1);
                this.graph = random.changeMatrix();
                this.route = random.getRoute();
                bt = new BackTracking (this.graph);
                result = bt.chromatic();
            }
            else
            {
                int x = new Random().nextInt(Main.factory.getGraphRepository().getVertexCount());
                Casual random = new Casual (BESTGRAPH, BESTROUTE, x);
                this.graph = random.changeMatrix();
                this.route = random.getRoute();
                bt = new BackTracking (this.graph);
                result = bt.chromatic();
                lastIteration = i;
            }
            if (result<chrom)
            {
                END = Duration.between(START,Instant.now()).toNanos();
                if (Configuration.VERBOSE)
                {
                    System.out.print(" found -> "+ result);
                    //System.out.print(" valid coloring : " +bt.isValidColoring());
                    System.out.println("  > "+END);
                }
                chrom = result;
                if (Configuration.TRAINING_MODE_ENABLED)
                {
                    if (  Main.factory.getGraphRepository().getBest_chromatic() == null || chrom < Main.factory.getGraphRepository().getBest_chromatic())
                    {
                        Main.factory.getGraphRepository().setBest_chromatic(chrom);
                        Main.factory.getGraphRepository().setBest_time(END);
                        Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.BACKTRACKING);
                    }
                    if (Main.factory.getGraphRepository().getBest_chromatic() == null || chrom == Main.factory.getGraphRepository().getBest_chromatic())
                        if (Main.factory.getGraphRepository().getBest_time() == null || END < Main.factory.getGraphRepository().getBest_time()  )
                        {
                            Main.factory.getGraphRepository().setBest_time(END);
                            Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.BACKTRACKING);
                        }
                }
                else if (Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.BACKTRACKING))
                {
                    Main.factory.getGraphRepository().setChromaticNumber(chrom);
                }
                lastIteration = i;
                BESTGRAPH = this.graph;
                BESTROUTE = this.route;

            }
            if (chrom == Main.factory.getGraphRepository().getLowerBound()) {
                return;
            }
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(chrom);
            }
        }
    }

    public static void calculate() {
        BTChromaticNumber backTracking = new BTChromaticNumber(Main.factory.getGraphRepository().getAdjacentMatrix());
        try {
            if(Configuration.VERBOSE)
                System.out.println("BackTracking : START ");
            backTracking.execute();
            if (Configuration.VERBOSE)
            {
                System.out.println("BackTracking : > END");
            }
        } catch (TimelimitExceededException e) {
            System.out.println("Chromatic Number Calculations Timed out");
        }
    }
}
