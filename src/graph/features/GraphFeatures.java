package graph.features;

import graph.Configuration;
import graph.Main;

public class GraphFeatures
{
    private Double avgDegree;
    private Double density;
    private Double [] localClusteringCoefficient;
    private Double globalClusteringCoefficient;
    private Double averageClustingCoefficient;
    public static Double maxDegree = null;
    public static Double minDegree = null;
    public static Double maxEdges = null;
    public static Double minEdges = null;
    public static Double maxVertices = null;
    public static Double minVertices = null;
    public static Double maxAverage = null;
    public static Double minAverage = null;
    public static Double maxUB = null;
    public static Double minUB = null;
    public static Double maxLB = null;
    public static Double minLB = null;
    public static Double maxCLUSTER = null;
    public static Double minCLUSTER = null;
    public static Double maxDensity = null;
    public static Double minDensity = null;



    public void evaluateFeatures()
    {

        this.avgDegree = null;
        this.density = null;
        this.localClusteringCoefficient = null;
        this.globalClusteringCoefficient = null;
        this.averageClustingCoefficient = null;
        this.density = (double)(2 * Main.factory.getGraphRepository().getEdgeCount()) / (double) (Main.factory.getGraphRepository().getVertexCount() * (Main.factory.getGraphRepository().getVertexCount() - 1));
        calculateLocalClustering();
        calculateGlobalClustering();
        setGraphFeatures();
    }
    public void checkMax()
    {
        if (minDensity == null||this.density < minDensity ) {
            minDensity = this.density;
        }
        if ( maxDensity == null|| maxDensity<this.density)
            maxDensity = this.density;

        if (maxCLUSTER == null||this.globalClusteringCoefficient>maxCLUSTER )
            maxCLUSTER = this.globalClusteringCoefficient;
        if (minCLUSTER == null||this.globalClusteringCoefficient<minCLUSTER  )
            minCLUSTER = this.globalClusteringCoefficient;

        if ( minEdges == null|| minEdges > Main.factory.getGraphRepository().getEdgeCount() )
            minEdges =(double) Main.factory.getGraphRepository().getEdgeCount();
        if ( maxEdges == null||maxEdges < Main.factory.getGraphRepository().getEdgeCount() )
            maxEdges =(double) Main.factory.getGraphRepository().getEdgeCount();

        if ( minVertices == null||minVertices > Main.factory.getGraphRepository().getVertexCount() )
            minVertices =(double) Main.factory.getGraphRepository().getVertexCount();
        if ( maxVertices == null||maxVertices < Main.factory.getGraphRepository().getVertexCount())
            maxVertices =(double) Main.factory.getGraphRepository().getVertexCount();

        if (minAverage == null||this.avgDegree<minAverage  )
            minAverage = this.avgDegree;
        if (maxAverage == null||this.avgDegree>maxAverage )
            maxAverage = this.avgDegree;

        if (minDegree == null||Main.factory.getGraphRepository().getMinimumDegree()<minDegree )
            minDegree = (double) Main.factory.getGraphRepository().getMinimumDegree();
        if ( maxDegree == null||Main.factory.getGraphRepository().getMaximumDegree()>maxDegree )
            maxDegree = (double) Main.factory.getGraphRepository().getMaximumDegree();

        if (maxUB == null||maxUB < Main.factory.getGraphRepository().getUpperBound() )
            maxUB =(double) Main.factory.getGraphRepository().getUpperBound();
        if (minUB == null||minUB > Main.factory.getGraphRepository().getUpperBound() )
            minUB =(double) Main.factory.getGraphRepository().getUpperBound();

        if (minLB == null||minLB > Main.factory.getGraphRepository().getLowerBound() )
            minLB =(double) Main.factory.getGraphRepository().getLowerBound();
        if (maxLB == null||maxLB < Main.factory.getGraphRepository().getLowerBound() )
            maxLB =(double) Main.factory.getGraphRepository().getLowerBound();
    }

    public void calculateLocalClustering()    // and average degree
    {

        this.localClusteringCoefficient = new Double[Main.factory.getGraphRepository().getVertexCount()];
        double sum = 0;
        double clusterSum = 0;
        for (int i = 0; i< Main.factory.getGraphRepository().getNodeDegree().length; i++)
        {
            sum+= Main.factory.getGraphRepository().getNodeDegree()[i];
            this.localClusteringCoefficient[i] = (2 *(double) Main.factory.getGraphRepository().getNodeDegree()[i])/(Main.factory.getGraphRepository().getVertexCount()*(double)(Main.factory.getGraphRepository().getVertexCount()-1));
            clusterSum+= this.localClusteringCoefficient[i];
        }
        this.avgDegree = sum/(double)Main.factory.getGraphRepository().getVertexCount();
        this.averageClustingCoefficient = clusterSum/(double)Main.factory.getGraphRepository().getVertexCount();
    }
    public void calculateGlobalClustering ()
    {
        int closedTriplets = 0;
        int openedTriplets = 0;
        for (int i = 0; i< Main.factory.getGraphRepository().getVertexCount(); i++)
            for (int k = 0; k < Main.factory.getGraphRepository().getVertexCount(); k++)
                if (Main.factory.getGraphRepository().getAdjacentMatrix()[i][k] == 1 && i != k)
                    for (int j = 0; j < Main.factory.getGraphRepository().getVertexCount(); j++)
                        if (k != j && i != j)
                            if (Main.factory.getGraphRepository().getAdjacentMatrix()[k][j] == 1)
                            {
                                if (Main.factory.getGraphRepository().getAdjacentMatrix()[j][i] == 1)
                                {
                                    closedTriplets++;
                                }
                                if (Main.factory.getGraphRepository().getAdjacentMatrix()[j][i] == 0)
                                {
                                    openedTriplets++;
                                }
                            }
        closedTriplets = closedTriplets/3;
        openedTriplets = openedTriplets/3;
        this.globalClusteringCoefficient = closedTriplets/(double)(openedTriplets+closedTriplets);
    }
    public void setGraphFeatures()
    {
        double [] input = setFeatures(13);
        if (Configuration.VERBOSE)
        {
            System.out.println("\n Graph features : \n" + " //_______________________________//\n"+
                    "EDGES : "+ input[1] );
            System.out.println("VERTICES : "+ input[0]);
            System.out.println("EDGE RATIO : "+ input[12]);
            System.out.println("VERTEX RATIO : "+ input[11]);
            System.out.println("LOWER BOUND : "+ input[2]);
            System.out.println("UPPER BOUND : "+ input[3]);
            System.out.println("MAXIMUM DEGREE : "+ input[4]);
            System.out.println("MINIMUM DEGREE : "+ input[5] );
            System.out.println("AVERAGE DEGREE : "+ input[6]);
            System.out.println("CLUSTERING AT MAXIMUM DEGREE : "+ input[9]);
            System.out.println("GRAPH DENSITY "+ input[7]);
            System.out.println("AVERAGE LOCAL CLUSTERING : "+ input[10]);
            System.out.println("GLOBAL CLUSTERING COEFFICIENT : "+ input[8]+"\n //_______________________________//\n" );
        }

        Main.factory.getGraphRepository().setGraphFeatures(input);
    }

    public  double[] setFeatures(int length)
    {
        double[] i = new double [length];
        i [0] = Main.factory.getGraphRepository().getVertexCount();
        i [1] = Main.factory.getGraphRepository().getEdgeCount();
        i [2] = Main.factory.getGraphRepository().getLowerBound();
        i [3] = Main.factory.getGraphRepository().getUpperBound();
        i [4] = Main.factory.getGraphRepository().getMaximumDegree();
        i [5] = Main.factory.getGraphRepository().getMinimumDegree();
        i [6] = Main.factory.getGraphRepository().features().avgDegree;
        i [7] = Main.factory.getGraphRepository().features().density;
        i [8] = Main.factory.getGraphRepository().features().globalClusteringCoefficient;
        i [9] = this.localClusteringCoefficient[Main.factory.getGraphRepository().getMaximumDegree()];
        i [10] = this.averageClustingCoefficient;
        i [11] = Main.factory.getGraphRepository().getVertexCount()/(double)Main.factory.getGraphRepository().getEdgeCount();
        i [12] = Main.factory.getGraphRepository().getEdgeCount()/(double)Main.factory.getGraphRepository().getVertexCount();
        return i ;
    }




}
