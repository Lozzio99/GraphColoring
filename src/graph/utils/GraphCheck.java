package graph.utils;

import graph.Main;

import java.util.LinkedList;

public class GraphCheck {

    /**
     * A Null Graph is a graph with no edges, while a Trivial Graph only has one vertex.
     * Both cases can be simply detected by checking the vertex count and edge count.
     * @param n - vertices
     * @param m - edges
     * @return - whether or not the graph is a null or trivial graph
     */
    public static boolean isNullOrTrivialGraph(int n, int m) {
        if (n == 1) {
            return true;
        }

        return m == 0;
    }

    /**
     * A complete graph can be detected by checking if nC2 is equal to the amount of edges
     * (where n is the amount of vertices)
     * @param n - vertices
     * @param m - edges
     * @return - whether or not the graph is a complete graph
     */
    public static boolean isCompleteGraph(int n, int m) {
        return Combinatronic.nCr(n, 2) == m;
    }

    /**
     * A star graph can be detected by taking the adjacency matrix
     * and keeping track on the number of vertices with a degree of 1 and a degree of n-1.
     * @param matrix - the Adjacency matrix
     * @param n - amount of vertices
     * @return - whether or not the graph is a star graph
     */
    public static boolean isStarGraph(int[][] matrix, int n) {
        int numDegreeOne = 0;
        int numDegreeNMinusOne = 0;

        // Only 1 vertex with no edges
        if (n == 1) {
            return (matrix[0][0] == 0);
        }

        // 2 vertices, each with a degree of one
        if (n == 2) {
            return (
                    matrix[0][0] == 0 &&
                    matrix[0][1] == 1 &&
                    matrix[1][0] == 1 &&
                    matrix[1][1] == 0
            );
        }

        // Standard Star Graph criteria
        for (int row = 0; row < n; row++) {
            int degree = 0;
            for (int col = 0; col < n; col++) {
                if (matrix[row][col] == 1) {
                    degree++;
                }
            }

            if (degree == 1) {
                numDegreeOne++;
            } else if (degree == n - 1) {
                numDegreeNMinusOne++;
            }
        }

        return (numDegreeOne == (n - 1) && numDegreeNMinusOne == 1);
    }

    /**
     * A Bipartite graph can be detected if we can divide the vertices into two independent sets (U and V),
     * such that every edge (u, v) will be either connected from U to V or from V to U, but NOT from U to U or V to V.
     * So there is no conncetion between vertices of the same set.
     * @param matrix - the adjacent matrix
     * @param n - vertices
     * @return - Whether or not the graph is bipartite
     */
    public static boolean isBipartiteGraph(int[][] matrix, int n) {
        int[] colors = new int[n];

        for (int i = 0; i < n; i++) {
            colors[i] = -1;
        }

        for (int i = 0; i < n; i++) {
            if (colors[i] == -1) {
                colors[i] = 1;

                LinkedList<Integer> queue = new LinkedList<>();
                queue.add(i);

                while (!queue.isEmpty()) {
                    int u = queue.getFirst();
                    queue.pop();

                    if (matrix[u][u] == 1) {
                        return false;
                    }

                    for (int v = 0; v < n; ++v) {
                        if (matrix[u][v] == 1 && colors[v] == -1) {
                            colors[v] = 1 - colors[u];
                            queue.push(v);
                        } else if (matrix[u][v] == 1 && colors[v] == colors[u]) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * This method checks if the graph has a cycle or not
     * @param matrix - adjacency matrix.
     * @param visited - vertices already checked.
     * @param node - the vertex that is been checked
     * @param parent - the vertex from the previous iteration
     * @return - recursively until all cycles have been found.
     */
    public static boolean hasCycle(int [][] matrix,boolean [] visited, int node, int parent)
    {
        visited[node] = true;
        for (int i = 0; i< matrix.length; i++)
        {
            if (matrix[node][i]==1)
                if (!visited[i])
                    if (node+1 < matrix.length)
                        return hasCycle(matrix,visited,node+1,node);
                else if (i != parent)
                    return true;
        }
        return false;
    }
    /**
     * This method checks if the graph is cyclic or not
     * @param matrix - adjacency matrix.
     * @param n - number of vertices.
     * @return - whether it's a cycle, or not.
     */
    public static boolean isCycleGraph (int [][] matrix, int n)
    {
        boolean [] visited = new boolean[n];
        for (int i = 0; i< n; i++)
        {
            visited[i] = false;
        }
        for (int i = 0; i< n; i++)
        {
            if (!visited[i])
                return hasCycle(matrix,visited,i,-1);
        }
        return false;
    }

    /**
     * This method checks if the graph is a tree or not
     * @param matrix - adjacency matrix
     * @param n - number of vertices
     * @return - whether its a tree or not
     */
    public static boolean isTreeGraph(int[][] matrix, int n) {
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            visited[i] = false;
        }

        // If there is a cycle it's obviously not a tree
        if (hasCycle(matrix,  visited, 0, -1)) {
            return false;
        }

        // if there is a vertex not reachable by the first vertex, then its not a tree graph.
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method checks if there's a vertex that's connected to every other vertex of the graph (center vertex).
     * @param matrix -the adjacency matrix
     * @param n The amount of vertices.
     * @return - the center vertex if applicable, else a negative value .
     */
    public static int centerOfWheel(int [][] matrix, int n) {
        int midVertex = -1;
        for (int i = 1; i < n; i++)
        {
            int amountOfVertices = 0;
            for (int j = 0; j < n; j++)
            {
                if (matrix [i][j] == 1)
                    amountOfVertices++;
            }
            if (amountOfVertices == n - 1 ) // If it's connected to all other vertices,
            {
                if (midVertex == -1)   // we need one and only one center
                    midVertex = i;
                else
                    midVertex = -2;
            }
        }
        return midVertex;
    }
    /**
     * This method checks if there's a wheel around the center vertex.
     * @param matrix -the adjacency matrix
     * @param n The amount of vertices.
     * @return - the center vertex if applicable, else a negative value .
     */
    public static boolean isWheelGraph(int [][] matrix, int n)
    {
        int center = centerOfWheel(matrix,n);
        if (center>-1)
        {
            int wheel = 0;
            for (int i = 0; i< n; i++)
            {
                if (i != center)
                {
                    int n_edges = 0;
                    for (int k = 0; k< n; k++)
                    {
                        if (matrix[i][k] ==1)
                            n_edges++;
                    }
                    if (n_edges ==2)
                        wheel++;
                }
            }
            return wheel == n-1;
        }
        return false;
    }
}
