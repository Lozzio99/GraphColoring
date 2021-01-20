package graph.algorithms.BackTracking;

import graph.Main;
import graph.exceptions.TimelimitExceededException;
import java.util.Random;

public class Casual {
    public static int [][] graphCloned;
    public static int [] route;
    /**
     This class creates an object referencing to a random variation of a given
     adjacency matrix
     @param matrix the initial matrix (adjacency represented by 1)
     return the reference of the new random matrix (accordingly to adjacency
     parameters)
     first we set the new graph starting from null size
     */
    public Casual (int [][] matrix , int [] path, int x) throws TimelimitExceededException
    {
        shuffle(path, x);
        route = path;
        int [][] clone = new int [matrix.length][matrix.length];
        for (int k = 0;k < Main.factory.getGraphRepository().getVertexCount();k++)
        {
            for (int j= 0;j< Main.factory.getGraphRepository().getVertexCount(); j++)
            {
                //we re-set the adjacency parameters on the clone
                if (Main.factory.getGraphRepository().getAdjacentMatrix()[route[k]][route[j]]==1)
                {
                    clone[k][j]=1;
                }
                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException();
                }
            }
        }
        //addressing the reference of the clone to the public one
        graphCloned = clone;
    }

    /**
     method to give the reference of the clone created
     @return the reference of the public graph
     */
    public int [][] changeMatrix ()
    {
        return graphCloned;
    }
    /**
     method to select the element to be randomly changed
     @param route the array of the order to be followed
     */
    public void shuffle (int [] route, int x) throws TimelimitExceededException {
        int n = route.length;
        Random random = new Random();
        random.nextInt();
        for (int k = 0; k<= x; k++)
        {
            int i= (int)(Math.random()*n);
            int change = i + random.nextInt(n - i);
            swap(route, i, change);
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException();
            }
        }
    }
    /**
     method to change the position of two element of an Array
     @param a array of positions
     @param i the victim of the random choice
     @param change the reflex of the victim from the middle of the array
     */
    public void swap(int[] a, int i, int change)
    {
        int helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }
    public int [] getRoute()
    {
        return route;
    }
}
