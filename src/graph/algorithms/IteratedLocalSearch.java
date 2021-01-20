package graph.algorithms;

import graph.Main;
import graph.exceptions.TimelimitExceededException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratedLocalSearch {
    public static int [] colours ;
    public static int chromaticNumber;
    public int max;
    public static int greedyChromatic;

    /*
     * Take a valid coloring and try to improve the coloring
     * by reducing one color and then trying to remove the conflicts
     * using the min-conflicts heuristic
     *
     * https://github.com/shah314/graphcoloring/blob/master/src/main/java/com/gcol/LocalSearch.java
     */

    public IteratedLocalSearch() throws TimelimitExceededException
    {
        Greedy.calculate();
        colours = Greedy.getColours();
        if (isValidColoring())
        {
            greedyChromatic = chromatic();
            System.out.println(" greedy -> " + isValidColoring());
        }
        else
        {
            System.out.println(" this test has already failed since now ");
        }
    }
    public int [] localSearch(int k, boolean verbose) throws TimelimitExceededException
    {
        int [] coloring = new int[Main.factory.getGraphRepository().getVertexCount()];

        for(int i=0; i< coloring.length;  i++)
        {
            coloring[i] = colours[i];
        }
        for(int p=0; p<3000;p++)
        {
            //TIME CHECK
            if (Main.factory.getGraphRepository().getWatch().isExceeded())
            {
                System.out.println("escaped with "+ max);
                throw new TimelimitExceededException(max);
            }
            int maxColor = k-1;
            for(int i=0; i<Main.factory.getGraphRepository().getVertexCount(); i++)
            {
                if(colours[i] == k)
                {
                    int c = ((int)(Math.random()*(double)maxColor)) + 1;
                    colours[i] = c;
                }
            }

            boolean flag = false;
            for(int n=0; n<1000; n++)
            {
                List<Integer> conflicts = new ArrayList<>();
                for(int i=0; i<Main.factory.getGraphRepository().getVertexCount(); i++)
                {
                    for (int j = 0; j< Main.factory.getGraphRepository().getVertexCount(); j++)
                    {

                        if ( i!= j && Main.factory.getGraphRepository().getAdjacentMatrix()[i][j]== 1)
                        {
                            conflicts.add(j);
                        }
                    }
                }
                //System.out.println(conflicts.size());
                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    System.out.println("escaped with "+ max);
                    throw new TimelimitExceededException(max);
                }
                if(conflicts.size() == 0)
                {
                    if(verbose)
                    {
                        System.out.println("Found Better Coloring - " + maxColor);
                    }

                    flag = true;
                    for(int i=0; i<coloring.length; i++)
                    {
                        coloring[i] = colours[i];
                    }
                    k--;
                    break;
                }
                else
                {
                    changeColorsRandomly(conflicts, maxColor);
                    conflicts = new ArrayList<>();
                    for(int i=0; i<Main.factory.getGraphRepository().getVertexCount();i++)
                    {

                        for (int j = 0; j< Main.factory.getGraphRepository().getVertexCount(); j++)
                        {

                            if ( i!= j && Main.factory.getGraphRepository().getAdjacentMatrix()[i][j]== 1)
                            {
                                conflicts.add(j);
                            }
                        }
                    }
                }
                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    System.out.println("escaped with "+ max);
                    throw new TimelimitExceededException(max);
                }
                for(int i=0; i<1000; i++)
                {
                    if(conflicts.size() == 0)
                    {
                        //System.out.println("Better Coloring Found!");
                        break;
                    }

                    int rand = (int)(Math.random() * conflicts.size());
                    Integer node = (Integer) conflicts.get(rand);
                    int bestcolor = -1;
                    int bestconflicts = -1;
                    for(int c=1; c<=maxColor; c++)
                    {
                        colours[node]= c;
                        List con = findConflictingNodes(node);

                        if(con.size() == 0)
                        {
                            bestcolor = c;
                            conflicts.remove(node);
                            break;
                        }
                        if(bestcolor == -1)
                        {
                            bestcolor = c;
                            bestconflicts = con.size();
                        }
                        else
                        {
                            if(bestconflicts > con.size())
                            {
                                bestconflicts = con.size();
                                bestcolor = c;
                            }
                        }
                    }
                    colours[node] = bestcolor;
                }
            }

            if(!flag)
            {
                break;
            }
        }

        return coloring;
    }

    public void changeColorsRandomly(List conflicts, int k) throws TimelimitExceededException
    {
        Iterator it = conflicts.iterator();
        if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
            System.out.println("escaped with "+ max);
            throw new TimelimitExceededException(max);
        }
        while(it.hasNext())
        {
            Integer node = (Integer) it.next();
            int color = ((int)(Math.random()*k)) + 1;
            colours[node] = color;
        }
    }

    public  List findConflictingNodes(int node) throws TimelimitExceededException
    {
        List<Integer> conflicts = new ArrayList<>();
        for (int j = 0; j< Main.factory.getGraphRepository().getVertexCount(); j++)
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max);
            }
            if ( node!= j && Main.factory.getGraphRepository().getAdjacentMatrix()[node][j]== 1)
            {
                conflicts.add(j);
            }
        }
        return conflicts;
    }
    public int chromatic () throws TimelimitExceededException
    {
        this.max = 0;
        for (int i = 0;i< colours.length; i++)
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max);
            }
            if (colours[i] > this.max)
            {
                this.max = colours[i];
                chromaticNumber = this.max;
            }
        }
        return this.max;
    }
    public boolean isValidColoring() throws TimelimitExceededException{
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

    public static void calculate() throws TimelimitExceededException {
        IteratedLocalSearch ls = new IteratedLocalSearch();
        try {
            colours = ls.localSearch(Main.factory.getGraphRepository().getLowerBound(),true);
            if (!ls.isValidColoring())
            {
                System.out.println(" not a valid coloring ");
                Main.factory.getGraphRepository().setChromaticNumber(greedyChromatic);
            }
            else
                Main.factory.getGraphRepository().setChromaticNumber(ls.chromatic());
        } catch (TimelimitExceededException e) {
            Main.factory.getGraphRepository().setChromaticNumber(ls.max );
        }
    }
}
