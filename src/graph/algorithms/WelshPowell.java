package graph.algorithms;

import graph.Configuration;
import graph.Main;
import graph.exceptions.TimelimitExceededException;
import graph.utils.StopWatch;

import java.util.Arrays;
import java.util.Comparator;

public class WelshPowell {
    private int[][] matrix;
    private int V;

    public WelshPowell(int[][] matrix) {
        this.matrix = matrix;
        this.V = matrix.length;
    }

    public int chromatic() throws TimelimitExceededException {
        int row;
        int col;
        int[][] degrees = new int[V][2];

        for (int i = 0; i < V; i++) {
            degrees[i][0] = i;

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException();
            }
        }

        for (row = 0; row < V; row++) {
            for (col = 0; col < V; col++) {
                degrees[row][1] += matrix[row][col];

                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException();
                }
            }
        }

        int[][] colors = matrixClone(degrees);
        Arrays.sort(degrees, Comparator.comparingInt(arr -> arr[1]));

        flip(degrees);

        for (int i = 0; i < V; i++) {
            colors[i][1] = 0;
            degrees[i][1] = 0;

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException();
            }
        }

        for (int i = 1; i <= V; i++) {
            for (int k = 0; k < V; k++) {
                if (degrees[k][1] == 0) {
                    int vertex = degrees[k][0];
                    boolean free = true;

                    for (int g = 0; g < V; g++) {
                        if (matrix[vertex][g] == 1 && colors[g][1] == i) {
                            free = false;
                        }

                        if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                            throw new TimelimitExceededException();
                        }
                    }

                    if (free) {
                        degrees[k][1] = i;
                        colors[(degrees[k][0])][1] = i;
                    }
                }
            }
        }

        int max = 0;
        for (int i = 0; i < V; i++) {
            if (colors[i][1] >= max) {
                max = colors[i][1];
            }

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max);
            }
        }

        return max;
    }

    private void flip(int[][] matrix) throws TimelimitExceededException {
        int top = 0;
        int bottom = matrix.length - 1;

        int[] temp = new int[2];

        while (top < bottom) {
            temp[0] = matrix[top][0];
            temp[1] = matrix[top][1];
            matrix[top][0] = matrix[bottom][0];
            matrix[top][1] = matrix[bottom][1];
            matrix[bottom][0] = temp[0];
            matrix[bottom][1] = temp[1];

            top++;
            bottom--;

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException();
            }
        }
    }

    private int[][] matrixClone(int[][] matrix) throws TimelimitExceededException {
        int length = matrix.length;

        int[][] target = new int[length][matrix[0].length];

        for (int i = 0; i < length; i++) {
            System.arraycopy(matrix[i], 0, target[i], 0, matrix[i].length);

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException();
            }
        }

        return target;
    }

    public static void calculate() {
        Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_UPPER_BOUND_TIMELIMLT);
        WelshPowell welshPowell = new WelshPowell(Main.factory.getGraphRepository().getAdjacentMatrix());
        int chromaticNumber = 0;
        try {
            chromaticNumber = welshPowell.chromatic();
            if (Configuration.VERBOSE)
            {
                System.out.print("Welsh Powell lower bound : ");
                System.out.println(chromaticNumber);
            }
            Main.factory.getGraphRepository().setUpperBound(chromaticNumber);
        } catch (TimelimitExceededException e) {
            Main.factory.getGraphRepository().setUpperBound(e.getValue());

            if(Configuration.isDebugging()) {
                e.printStackTrace();
            }
        }

        Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
    }
}
