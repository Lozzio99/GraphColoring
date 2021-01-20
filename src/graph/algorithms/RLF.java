package graph.algorithms;

import graph.Main;
import graph.exceptions.TimelimitExceededException;

public class RLF {
    int[] degree = Main.factory.getGraphRepository().getNodeDegree();
    int[] color = new int[Main.factory.getGraphRepository().getVertexCount()];
    int unprocessed = Main.factory.getGraphRepository().getVertexCount();
    int NNCount;
    int n = Main.factory.getGraphRepository().getVertexCount();
    int[] NN = new int[Main.factory.getGraphRepository().getVertexCount()];
    public int chromaticNumber = 0;
    private int max = 0;


    public void coloring() throws TimelimitExceededException{
        int x, y;
        int ColorNumber = 0;
        Integer VerticesInCommon = 0;
        while (this.unprocessed > 0) // while there is an uncolored vertex
        {
            x = MaxDegreeVertex(); // find the one with maximum degree
            ColorNumber++;
            this.color[x] = ColorNumber; // give it a new color
            this.unprocessed--;
            UpdateNN(ColorNumber); // find the set of non-neighbors of x
            while (this.NNCount > 0) {
                if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                    throw new TimelimitExceededException(max+1);
                }
                // find y, the vertex has the maximum neighbors in common with x
                // VerticesInCommon is this maximum number
                y = FindSuitableY(ColorNumber, VerticesInCommon);
                // in case VerticesInCommon = 0
                // y is determined that the vertex with max degree in NN
                if (VerticesInCommon == 0)
                    y = MaxDegreeInNN();
                // color y the same to x
                color[y] = ColorNumber;
                unprocessed--;
                UpdateNN(ColorNumber);
                // find the new set of non-neighbors of x
            }
        }
    }

    public int MaxDegreeVertex() throws TimelimitExceededException{
        int max = -1;
        int max_i = 0;
        for (int i = 0; i < this.n; i++)
        {
            if (color[i] == 0)
            {
                if (degree[i] > max)
                {
                    max = degree[i];
                    max_i = i;
                }
            }
        }
        return max_i;
    }


    public void UpdateNN(int ColorNumber) throws TimelimitExceededException{
        this.NNCount = 0;
        // firstly, add all the uncolored vertices into NN set
        for (int i = 0; i < this.n; i++)
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1);
            }
            if (this.color[i] == 0)
            {
                this.NN[this.NNCount] = i;
                this.NNCount++; // when we add a vertex, increase the NNCount
            }
        }

        // then, remove all the vertices in NN that
        // is adjacent to the vertices colored ColorNumber
        for (int i = 0; i < n; i++)
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1);
            }
            if (this.color[i] == ColorNumber) // find one vertex colored ColorNumber
            {
                for (int j = 0; j < this.NNCount; j++) {
                    if (Main.factory.getGraphRepository().getAdjacentMatrix()[i][NN[j]] == 1 && i != NN[j]) {
                        NN[j] = NN[this.NNCount - 1];
                        this.NNCount--; // decrease the NNCount
                    }
                }

            }

        }

    }

    public int FindSuitableY(int ColorNumber, Integer VerticesInCommon) throws TimelimitExceededException{
        int temp, tmp_y, y = 0;
        // array scanned stores uncolored vertices
        // except the vertex is being processing
        int[] scanned = new int[Main.factory.getGraphRepository().getVertexCount()];
        VerticesInCommon = 0;
        for (int i = 0; i < this.NNCount; i++) // check the i-th vertex in NN
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1);
            }
            // tmp_y is the vertex we are processing
            tmp_y = this.NN[i];
            // temp is the neighbors in common of tmp_y
            // and the vertices colored ColorNumber
            temp = 0;
            scanned = scannedInit(scanned);
            //reset scanned array in order to check all
            //the vertices if they are adjacent to i-th vertex in NN
            for (int x = 0; x < this.n; x++)
            {
                if (this.color[x] == ColorNumber)
                {
                    for (int k = 0; k < this.n; k++)
                    {
                        if (this.color[k] == 0 && scanned[k] == 0)
                        {
                            if (Main.factory.getGraphRepository().getAdjacentMatrix()[x][k] == 1 && x != k && Main.factory.getGraphRepository().getAdjacentMatrix()[tmp_y][k] == 1 && tmp_y != k)   // if k is a neighbor in common of x and tmp_y
                            {
                                temp++;
                                scanned[k] = 1; // k is scanned
                            }
                        }
                    }
                }
            }

            if (temp > VerticesInCommon) {
                VerticesInCommon = temp;
                y = tmp_y;
            }
        }
        return y;
    }

    public int[] scannedInit(int scanned[]) {
        return new int[scanned.length];
    }


    public int MaxDegreeInNN() throws TimelimitExceededException{
        int tmp_y = 0; // the vertex has the current maximum degree
        int temp, max = 0;
        for (int i = 0; i < this.NNCount; i++)
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException(max+1);
            }
            temp = 0;
            for (int j = 0; j < this.n; j++)
            {
                if (this.color[j] == 0 && Main.factory.getGraphRepository().getAdjacentMatrix()[NN[i]][j] == 1 && i != j)
                    temp++;
            }

            if (temp > max) // if the degree of vertex NN[i] is higher than tmp_y's one
            {
                max = temp; // assignment NN[i] to tmp_y
                tmp_y = this.NN[i];
            }
        }
        if (max == 0) // so all the vertices have degree 0
            return this.NN[0];
        else // exist a maximum, return i
            return tmp_y;
    }

    public int chromatic()throws TimelimitExceededException
    {
        this.max = 0;
        for (int i = 0; i < this.color.length; i++)
        {
            if (this.color[i] > this.max)
            {
                this.max = this.color[i];
                this.chromaticNumber = this.max;
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
                if (Main.factory.getGraphRepository().getAdjacentMatrix()[i][k] == 1 && i != k && this.color[i] == this.color[k]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void calculate()
    {
        RLF rlf = new RLF();
        try {
            rlf.coloring();
            if (!rlf.isValidColoring())
            {
                System.out.println(" son of a bitch ");
            }
            else
            {
                System.out.println(rlf.chromatic());
                Main.factory.getGraphRepository().setChromaticNumber(rlf.chromaticNumber);
            }
        } catch (TimelimitExceededException e) {
            Main.factory.getGraphRepository().setChromaticNumber(rlf.max + 1);
        }


    }


}