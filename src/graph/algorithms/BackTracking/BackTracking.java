package graph.algorithms.BackTracking;

import graph.Main;
import graph.exceptions.TimelimitExceededException;

import java.time.Duration;
import java.time.Instant;

public class BackTracking {
    public int [][] adjacencyMatrix;
    public int [] colours;
    public static int max;

    public BackTracking(int[][] matrix) throws TimelimitExceededException
    {
        this.colours = new int [matrix.length];
        this.adjacencyMatrix = matrix;
        graphColour(0);
    }
    /**
     method to apply the backtracking algorithm
     @param k the index to be coloured first
     return one valid color per index via print method
     */
    public void graphColour (int k ) throws TimelimitExceededException {
        for (int c = 1; c<= Main.factory.getGraphRepository().getUpperBound() ; c++)
        {

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max, Duration.between(BTChromaticNumber.START, Instant.now()).toNanos());
            }

            if (iCanAssign(k,c))
            {
                //if the colour is valid according to the adjacency matrix
                this.colours[k]=c;
                //it get assigned at the position
                //and recursively go again for the next position
                if ((k+1)<Main.factory.getGraphRepository().getVertexCount())
                {
                    graphColour(k+1);
                }
                return;
            }
        }
    }
    /**
     method to check if the selected colour respect the adjacency parameters
     @param k the position to be checked
     @param c the colour to be checked
     @return true if the colour can be assigned at that place
     */
    public boolean iCanAssign (int k, int c) throws TimelimitExceededException {
        for (int j = 0; j< Main.factory.getGraphRepository().getVertexCount(); j++)
        {
            if (this.adjacencyMatrix[k][j]==1 && c== this.colours[j])
            {
                return false;
            }

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max, Duration.between(BTChromaticNumber.START, Instant.now()).toNanos());
            }
        }
        return true;
    }
    public int [][] getGraph()
    {
        return this.adjacencyMatrix;
    }

    public int chromatic() throws TimelimitExceededException {
        max = 0;
        for (int i = 0; i< colours.length ; i++)
        {
            if (this.colours[i]>max)
            {
                max = this.colours[i];
            }
        }
        return max;
    }
    public boolean isValidColoring ()
    {
        for (int i = 0; i< Main.factory.getGraphRepository().getVertexCount(); i++)
        {
            for (int k =0 ; k< Main.factory.getGraphRepository().getVertexCount();k++)
            {
                if ( i!= k && this.adjacencyMatrix[i][k] == 1 && this.colours[i] == this.colours[k])
                {
                    return false;
                }
            }
        }
        return true;
    }
    public int [] colouredArray()
    {
        return this.colours;
    }
}
