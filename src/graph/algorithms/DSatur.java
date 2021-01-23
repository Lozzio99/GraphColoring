package graph.algorithms;

import graph.Configuration;
import graph.Main;
import graph.enums.Algorithm;
import graph.exceptions.TimelimitExceededException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * https://github.com/maeroso/dsatur/blob/master/src/Dsatur/Dsatur.java
 */
public class DSatur {
    //instance variables: graph to color, sorted set of values of used colors, collection of uncolored vertices
    public static int count;
    public int chromaticNumber = 0;
    private int max = 0;
    private static Instant start;
    private static double end;
    public DSatur()
    {
        start = Instant.now();
        count = 0;
    }

    public Integer chromatic() throws TimelimitExceededException {

        for (Integer x : doAlgorithm())
        {
            if (x> max)
                max = x;
        }
        if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
            throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
        }
        this.chromaticNumber = max+1;
        return this.chromaticNumber;
    }

    //returns saturation degree among list of vertices
    public ArrayList<Integer> doAlgorithm() throws TimelimitExceededException {
        int length = Main.factory.getGraphRepository().getVertexCount();
        Map<Integer, Integer> resultColoring = new LinkedHashMap<>();
        List<Integer> coloredVertexes = new ArrayList<>();
        int[] coloring = new int[length];
        for (int i = 0; i< length; i++)
        {
            coloring[i] = -1;

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
            }
        }
        List<Integer> withoutColor = new ArrayList<>();
        for (int i = 0; i < length; i++)
        {
            withoutColor.add(i);

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
            }
        }

        int highestDegreeVertex = Main.factory.getGraphRepository().getMaximumDegree();
        coloring[highestDegreeVertex] = 0;
        coloredVertexes.add(highestDegreeVertex);
        resultColoring.put(highestDegreeVertex, 0);
        withoutColor.remove((Object)highestDegreeVertex);

        while (withoutColor.size() > 0  )
        {
            int vertex = getHighestSaturation(coloring);
            while (resultColoring.containsKey(vertex))
            {
                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
                }

                if (count >5000)
                {
                    break;
                }
                vertex = getHighestSaturation(coloring);
            }
            if (count>5000)
            {
                break;
            }
            boolean[] availableColors = new boolean[length];
            for (int j = 0; j < length; j++)
            {
                availableColors[j] = true;

                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
                }
            }

            int lastColor = 0;
            for (int k = 0; k < coloredVertexes.size(); k++)
            {
                int currentVertex = coloredVertexes.get(k);
                if (Main.factory.getGraphRepository().getAdjacentMatrix()[vertex][currentVertex] == 1)
                {
                    int color = resultColoring.get(currentVertex);
                    availableColors[color] = false;
                }

                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
                }
            }
            for (int j = 0; j < availableColors.length; j++)
            {
                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
                }

                if (availableColors[j])
                {
                    lastColor = j;
                    break;
                }
            }
            resultColoring.put(vertex, lastColor);
            withoutColor.remove((Object)vertex);
            coloredVertexes.add(vertex);
            coloring[vertex] = lastColor;
        }


        //System.out.println(resultColoring);
        return new ArrayList<>(resultColoring.values());
    }

    public int getHighestSaturation(int[] coloring) throws TimelimitExceededException {
        int maxSaturation = 0;
        int vertexWithMaxSaturation = 0;
        int length = Main.factory.getGraphRepository().getVertexCount();
        count++;
        for(int i = 0; i < length; i++)
        {
            if (coloring[i] == -1)
            {
                Set<Integer> colors = new TreeSet<>();
                for (int j = 0; j < length; j++)
                {
                    if (Main.factory.getGraphRepository().getAdjacentMatrix()[i][j] == 1  && coloring[j] != -1)
                    {
                        colors.add(coloring[j]);

                        if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                            throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
                        }
                    }
                }
                int tempSaturation = colors.size();
                if (tempSaturation > maxSaturation)
                {
                    maxSaturation = tempSaturation;
                    vertexWithMaxSaturation = i;
                }
                else if (tempSaturation == maxSaturation && Main.factory.getGraphRepository().getNodeDegree()[i]
                        >= Main.factory.getGraphRepository().getNodeDegree()[vertexWithMaxSaturation])
                {
                    vertexWithMaxSaturation = i;
                }
            }

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1,Duration.between(start,Instant.now()).toNanos());
            }
        }
        //System.out.println("Saturation = " + maxSaturation + ", vertex = " + vertexWithMaxSaturation);
        return vertexWithMaxSaturation;
    }

    public static void calculate() {
        DSatur dSatur = new DSatur();
        try {
            int chrom = dSatur.chromatic();
            end = Duration.between(start,Instant.now()).toNanos();
            if (Configuration.VERBOSE)
            {
                System.out.print("Dsatur : "+ chrom);
                System.out.println("  > "+end);
            }
            if (Configuration.TRAINING_MODE_ENABLED)
                checkPerformance(chrom, end);
            else if (Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.DSATUR))
                Main.factory.getGraphRepository().setChromaticNumber(chrom);

        } catch (TimelimitExceededException e ) {
            if(Configuration.isDebugging()) {
                e.printStackTrace();
            }
        }
    }

    private static void checkPerformance(int chrom, double end)
    {
        if (  Main.factory.getGraphRepository().getBest_chromatic() == null || chrom < Main.factory.getGraphRepository().getBest_chromatic())
        {
            Main.factory.getGraphRepository().setBest_chromatic(chrom);
            Main.factory.getGraphRepository().setBest_time(end);
            Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.DSATUR);
        }
        else if ((Main.factory.getGraphRepository().getBest_chromatic() == null || chrom == Main.factory.getGraphRepository().getBest_chromatic())
                && (Main.factory.getGraphRepository().getBest_time() == null || end < Main.factory.getGraphRepository().getBest_time()  ))
            {
                Main.factory.getGraphRepository().setBest_time(end);
                Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.DSATUR);
            }
    }
}
