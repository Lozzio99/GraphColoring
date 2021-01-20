package graph.algorithms;

import graph.Configuration;
import graph.Main;
import graph.enums.Algorithm;
import graph.exceptions.TimelimitExceededException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RecursiveLargeFirst
{
    private int chromaticNumber;
    private int max;
    private int [] colours;

    public void doAlgorithm() throws TimelimitExceededException
    {
        int length = Main.factory.getGraphRepository().getVertexCount();
        List<Integer> vertexes = new ArrayList<>();

        for (int i = 0; i< length; i++){
            vertexes.add(i);
        }

        vertexes.sort((o1, o2) ->
                Main.factory.getGraphRepository().getNodeDegree()[o2] -
                        Main.factory.getGraphRepository().getNodeDegree()[o1]);
//        System.out.println("Sorted vertexes : " + vertexes);

        List<Integer> resultColoring = new ArrayList<>();
        resultColoring.add(0);
        List<Integer> coloredVertexes = new ArrayList<>();
        coloredVertexes.add(vertexes.get(0));

        for (int i = 1; i < length; i++){
            int currentVertex = vertexes.get(i);

            boolean[] availableColors = new boolean[length];
            for (int j = 0; j < length; j++){
                availableColors[j] = true;
            }
            int lastColor = 0;
            for (int k = 0; k < coloredVertexes.size(); k++){
                if (Main.factory.getGraphRepository().getAdjacentMatrix()[currentVertex] [coloredVertexes.get(k)] == 1)
                {
                    int color = resultColoring.get(k);
                    availableColors[color] = false;
                }
            }
            for (int j = 0; j < availableColors.length; j++){
                if (availableColors[j]){
                    lastColor = j;
                    break;
                }
            }
            resultColoring.add(lastColor);
            coloredVertexes.add(currentVertex);
        }
        this.colours = new int[resultColoring.size()];
        for (int i = 0; i < length; i++)
        {
//            System.out.println("Vertex " + vertexes.get(i) + " --->  Color " + resultColoring.get(i));
            this.colours[vertexes.get(i)] = resultColoring.get(i);
        }
//        System.out.println(" ------------------- \n"+ this.isValidColoring());

    }
    public Integer chromatic() throws TimelimitExceededException
    {
        colours = new int[Main.factory.getGraphRepository().getVertexCount()];
        doAlgorithm();
        for (Integer x : this.colours)
        {
            if (x> max)
                max = x;
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1);
            }
        }
        this.chromaticNumber = max+1;
//        System.out.println(" ------- >" + this.chromaticNumber);
        return this.chromaticNumber;
    }


    public static void calculate() {
        RecursiveLargeFirst lf = new RecursiveLargeFirst();
        try
        {
            Instant start = Instant.now();
            int chrom = lf.chromatic();
            double end = Duration.between(start,Instant.now()).toNanos();
            Main.factory.getGraphRepository().setChromaticNumber(chrom);
            if (Configuration.VERBOSE)
            {
                System.out.print("Recursive Largest First : "+chrom);
                //System.out.print(" valid coloring : "+ lf.isValidColoring());
                System.out.println("  > "+end);
            }
            if (Configuration.TRAINING_MODE_ENABLED)
            {
                checkPerformance(chrom, end);
            }
            else if (Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.RLF))
            {
                Main.factory.getGraphRepository().setChromaticNumber(chrom);
            }
        }
        catch (TimelimitExceededException e)
        {
            Main.factory.getGraphRepository().setChromaticNumber(lf.max + 1);
            if(Configuration.isDebugging()) {
                e.printStackTrace();
            }
        }
    }

    public static void checkPerformance(int chrom, double end)
    {
        if (  Main.factory.getGraphRepository().getBest_chromatic() == null || chrom < Main.factory.getGraphRepository().getBest_chromatic())
        {
            Main.factory.getGraphRepository().setBest_chromatic(chrom);
            Main.factory.getGraphRepository().setBest_time(end);
            Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.RLF);
        }
        if (Main.factory.getGraphRepository().getBest_chromatic() == null || chrom == Main.factory.getGraphRepository().getBest_chromatic())
            if (Main.factory.getGraphRepository().getBest_time() == null || end < Main.factory.getGraphRepository().getBest_time()  )
            {
                Main.factory.getGraphRepository().setBest_time(end);
                Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.RLF);
            }
    }

    public boolean isValidColoring() throws TimelimitExceededException
    {
        for (int i = 0; i < Main.factory.getGraphRepository().getVertexCount(); i++)
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1);
            }
            for (int k = 0; k < Main.factory.getGraphRepository().getVertexCount(); k++) {
                if (Main.factory.getGraphRepository().getAdjacentMatrix()[i][k] == 1 && i != k && colours[i] == colours[k]) {
                    return false;
                }
            }
        }
        return true;
    }
}
