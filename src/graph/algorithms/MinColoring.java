package graph.algorithms;

import graph.Configuration;
import graph.Main;

public class MinColoring {
    int min;
    int[] best;

    private int minimumColoring(int[][] matrix) {
        int n = matrix.length;
        best = new int[n];
        int[] nodes = new int[n+1];
        int[] degrees = new int[n+1];

        for (int i = 0; i <= n; i++) {
            nodes[i] = i;
        }

        best = new int[n];

        int solution = 1;

        int from = 0;
        for(int to = 1; to <= n; to++) {
            int best = to;

            for (int i = to; i < n; i++) {
                if (matrix[nodes[to - 1]][nodes[i]] == 1) {
                    ++degrees[nodes[i]];
                }

                if (degrees[nodes[best]] < degrees[nodes[i]]) {
                    best = i;
                }
            }

            int t = nodes[to];
            nodes[to] = nodes[best];
            nodes[best] = t;

            if (degrees[nodes[to]] == 0) {
                min = n+1;
                dfs(matrix, nodes, new int[n], from, to, from, 0);
                from = to;
                solution = Math.max(solution, min);
            }
        }

        return solution;
    }

    private void dfs(int[][] matrix, int[] nodes, int[] coloring, int from, int to, int currentIndex, int usedColors) {
        if (usedColors >= min) {
            return;
        }

        if (currentIndex == to) {
            for (int i = from; i < to; i++) {
                best[nodes[i]] = coloring[i];
            }

            min = usedColors;
            return;
        }

        boolean[] used = new boolean[usedColors + 1];

        for (int i = 0; i < currentIndex; i++) {
            if (matrix[nodes[currentIndex]][nodes[i]] == 1) {
                used[coloring[i]] = true;
            }
        }

        for (int i = 0; i <= usedColors; i++) {
            if (!used[i]) {
                int temp = coloring[currentIndex];

                coloring[currentIndex] = i;
                dfs(matrix, nodes, coloring, from, to, currentIndex + 1, Math.max(usedColors, i+1));
                coloring[currentIndex] = temp;
            }
        }
    }

    public int getMin() {
        return this.min;
    }

    public int[] getBest() {
        return this.best;
    }

    public static void calculate() {
        MinColoring minColoring = new MinColoring();

        try {
            minColoring.minimumColoring(Main.factory.getGraphRepository().getAdjacentMatrix());
        } catch (Exception e) {
            if(Configuration.isDebugging()) {
                e.printStackTrace();
            }
        }

        Main.factory.getGraphRepository().setLowerBound(minColoring.getMin());
    }
}
