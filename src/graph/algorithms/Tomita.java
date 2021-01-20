package graph.algorithms;

import graph.Configuration;
import graph.Main;
import graph.exceptions.TimelimitExceededException;

import java.util.ArrayList;

/**
 * Source: P. Prosser, “Exact Algorithms for Maximum Clique: A Computational Study,” Algorithms, vol. 5, no. 4, Art. no. 4, Dec. 2012, doi: 10.3390/a5040545.
 */
public class Tomita
{
    private int[][] matrix;
    private int[] solution;
    private int maxSize;

    private Tomita(int[][] adjMatrix){
        this.matrix = adjMatrix;
        this.solution = new int[0];
    }

    private void search() throws TimelimitExceededException {
        ArrayList<Integer> C = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            C.add(i);

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(solution.length);
            }
        }

        expand(new ArrayList<Integer>(), C);
        maxSize = solution.length;
    }

    private void expand(ArrayList<Integer> C, ArrayList<Integer> P) throws TimelimitExceededException {
        for (int p = P.size(); p > 0; p--) {
            if (C.size() + P.size() <= solution.length) return;

            int lastVertex = P.get(P.size() - 1);
            C.add(lastVertex);

            ArrayList<Integer> newP = new ArrayList<>();

            for (int w : P) {
                if (matrix[lastVertex][w] == 1) {
                    newP.add(w);
                }

                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException(solution.length);
                }
            }

            if (newP.isEmpty() && C.size() > solution.length) {
                solution = new int[C.size()];

                for (int i = 0; i < solution.length; i++) {
                    solution[i] = C.get(i);

                    if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                        throw new TimelimitExceededException(solution.length);
                    }
                }
            } else {
                expand(C, newP);
            }

            C.remove((Integer) lastVertex);
            P.remove((Integer) lastVertex);
        }
    }

    public static void calculate() {
        Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_LOWER_BOUND_TIMELIMIT);
        Tomita max = new Tomita(Main.factory.getGraphRepository().getAdjacentMatrix());

        int size = 0;

        try {
            max.search();
            size = max.maxSize;
        } catch (TimelimitExceededException e) {
            size = e.getValue();

            if(Configuration.isDebugging()) {
                e.printStackTrace();
            }
        }

        Main.factory.getGraphRepository().setLowerBound(size);
        Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
    }
}
