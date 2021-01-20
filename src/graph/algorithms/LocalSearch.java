package graph.algorithms;

import graph.Configuration;
import graph.Main;
import graph.exceptions.TimelimitExceededException;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Min-Conflicts local search heuristic
 * @author Shalin Shah
 * Email: shah.shalin@gmail.com
 */
public class LocalSearch
{
    public static int [] colours ;
    public static int chromaticNumber;
    public int max;
    public static int greedyChromatic;

    /*
     * Take a valid coloring and try to improve the coloring
     * by reducing one color and then trying to remove the conflicts
     * using the min-conflicts heuristic
     */

    public LocalSearch() throws TimelimitExceededException
    {
        if (Configuration.VERBOSE)
            System.out.println("Local search : ");
        Configuration.VERBOSE = false;
        Greedy.calculate();
        colours = Greedy.getColours();
        Configuration.VERBOSE = true;
        if (isValidColoring())
        {
            greedyChromatic = chromatic();
            if (!Configuration.TRAINING_MODE_ENABLED && Configuration.VERBOSE)
                System.out.println(" greedy starting -> " + isValidColoring());
        }
        else
        {
            if (Configuration.VERBOSE)
                System.out.println(" this test has already failed since now ");
        }
    }
    public int [] localSearch(int k) throws TimelimitExceededException
    {
        int [] coloring = new int[Main.factory.getGraphRepository().getVertexCount()];
        for(int i=0; i< coloring.length;  i++)
        {
            coloring[i] = colours[i];
        }
        for(int p=0; p<5000;p++)
        {
            //TIME CHECK
            if (Main.factory.getGraphRepository().getWatch().isExceeded())
            {
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
            for(int n=0; n<5000; n++)
            {
                List conflicts = new ArrayList();
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
                if (Main.factory.getGraphRepository().getWatch().isExceeded())
                {
                    throw new TimelimitExceededException(max);
                }
                if(conflicts.size() == 0)
                {
                    if(Configuration.VERBOSE)
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
                    conflicts = new ArrayList();
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
                if (Main.factory.getGraphRepository().getWatch().isExceeded())
                {
                    throw new TimelimitExceededException(max);
                }
                for(int i=0; i<5000; i++)
                {
                    if(conflicts.size() == 0)
                    {
                        break;
                    }
                    int rand = (int)(Math.random() * conflicts.size());
                    Integer node = (Integer) conflicts.get(rand);
                    int bestcolor = -1;
                    int bestconflicts = -1;
                    if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                        throw new TimelimitExceededException(max);
                    }
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
        if (Main.factory.getGraphRepository().getWatch().isExceeded())
        {
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
        List conflicts = new ArrayList();
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
    public boolean isValidColoring() throws TimelimitExceededException
    {
        for (int i = 0; i < Main.factory.getGraphRepository().getVertexCount(); i++)
        {
            for (int k = 0; k < Main.factory.getGraphRepository().getVertexCount(); k++)
            {
                if (Main.factory.getGraphRepository().getAdjacentMatrix()[i][k] == 1 && i != k && colours[i] == colours[k]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void calculate() throws TimelimitExceededException
    {
        LocalSearch ls = new LocalSearch();
        try {
            double start = System.nanoTime();
            colours = ls.localSearch(Main.factory.getGraphRepository().getLowerBound());
            if (!ls.isValidColoring())
            {
                if (Configuration.VERBOSE)
                    System.out.println(" son of a b**** ");
                Main.factory.getGraphRepository().setChromaticNumber(greedyChromatic);
            }
            else
            {
                Main.factory.getGraphRepository().setChromaticNumber(ls.chromatic());
                if (Configuration.VERBOSE)
                {
                    System.out.println("Iterated Local Search : "+ls.chromatic());
                    System.out.println(" valid coloring : "+ ls.isValidColoring());
                    System.out.println(System.nanoTime()-start);
                }
            }
        } catch (TimelimitExceededException e) {
            Main.factory.getGraphRepository().setChromaticNumber(ls.max );
        }
    }
}