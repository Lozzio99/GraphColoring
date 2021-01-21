package graph.repositories;

import graph.Configuration;
import graph.domain.Edge;
import graph.enums.Algorithm;
import graph.enums.GraphType;
import graph.enums.LowerBound;
import graph.enums.UpperBound;
import graph.interfaces.GraphRepositoryInterface;
import graph.features.GraphFeatures;
import graph.selection.MLP.controls.NeuralNetwork;
import graph.selection.MLP.utils.PersonalTrainer;
import graph.utils.StopWatch;

import java.util.*;


public class GraphRepository implements GraphRepositoryInterface
{
    private Integer vertexCount;
    private Integer edgeCount;
    private Edge[] edges;
    private int[][] adjacentMatrix;
    private int[] nodeDegree;
    private Integer maxDegree;
    private Integer minDegree;
    private Integer chromaticNumber;
    private Integer upperBound;
    private Integer lowerBound;
    private Algorithm chosenAlgorithm;
    private UpperBound upperBoundAlgorithm;
    private LowerBound lowerBoundAlgorithm;
    private GraphType graphType;
    private NeuralNetwork MLP;
    private double[] graphFeatures;
    private Double best_time;
    private Integer best_chromatic;
    private  Algorithm bestAlgorithm;
    public GraphFeatures features ;
    private PersonalTrainer trainer;



    private StopWatch watch;

    public GraphRepository() {
        this.chromaticNumber = null;
        this.chosenAlgorithm = null;
        this.upperBound = null;
        this.lowerBound = null;
        this.watch = new StopWatch();
        features = new GraphFeatures();
        this.MLP = new NeuralNetwork(Configuration.NUMBER_OF_FEATURES,Configuration.NUMBER_OF_HIDDEN,Configuration.NUMBER_OF_OUTPUTS,Configuration.MLP_RESET);
        if (Configuration.TRAINING_MODE_ENABLED)
        {
            this.trainer = new PersonalTrainer();
        }
    }

    @Override
    public void reset() {
        this.vertexCount = null;
        this.edgeCount = null;
        this.edges = null;
        this.adjacentMatrix = null;
        this.chromaticNumber = null;
        this.lowerBound = null;
        this.upperBound = null;
        this.maxDegree = null;
        this.minDegree = null;
        this.nodeDegree = null;
        this.bestAlgorithm = null;
        this.graphFeatures =null;
        this.best_time = null;
        this.best_chromatic = null;
        this.chosenAlgorithm = Configuration.DEFAULT_ALGORITHM;
        this.lowerBoundAlgorithm = Configuration.DEFAULT_LOWER_BOUND;
        this.upperBoundAlgorithm = Configuration.DEFAULT_UPPER_BOUND;
        this.graphType = null;
        this.watch = new StopWatch();
    }
    @Override
    public PersonalTrainer getTrainer()
    {
        return this.trainer;
    }

    @Override
    public Integer getUpperBound() {
        return upperBound;
    }

    @Override
    public GraphFeatures features() {
        return features;
    }

    @Override
    public void setUpperBound(Integer upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public Integer getLowerBound() {
        return lowerBound;
    }

    @Override
    public void setLowerBound(Integer lowerBound) {
        this.lowerBound = lowerBound;
    }

    @Override
    public Algorithm getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    @Override
    public void setChosenAlgorithm(Algorithm chosenAlgorithm) {
        this.chosenAlgorithm = chosenAlgorithm;
    }

    @Override
    public Integer getVertexCount() {
        return vertexCount;
    }


    @Override
    public void setVertexCount(Integer vertexCount) {
        this.vertexCount = vertexCount;
    }

    @Override
    public Integer getEdgeCount() {
        return edgeCount;
    }

    @Override
    public void setEdgeCount(Integer edgeCount) {
        this.edgeCount = edgeCount;
    }

    @Override
    public Edge[] getEdges() {
        return edges;
    }

    @Override
    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }

    @Override
    public int[][] getAdjacentMatrix() {
        return adjacentMatrix;
    }

    @Override
    public void createAdjacentMatrix() {
        int max = 0;
        int min = this.getVertexCount();
        int[][] matrix = new int[this.getVertexCount()][this.getVertexCount()];
        int[] nodeDegree = new int [this.getVertexCount()];

        for (int i = 0; i < this.getEdges().length; i++)
        {
            int vertex = this.getEdges()[i].getFrom() - 1;
            int relation = this.getEdges()[i].getTo() - 1;

            nodeDegree[vertex]++;
            nodeDegree[relation]++ ;

            matrix[vertex][relation] = 1;
            matrix[relation][vertex] = 1;

            if (nodeDegree[vertex] > max) {
                max = nodeDegree[vertex];
            }
            if (nodeDegree[relation] > max) {
                max = nodeDegree[relation];
            }

            if (nodeDegree[vertex] < min) {
                min = nodeDegree[vertex];
            }
            if (nodeDegree[relation] < min) {
                min = nodeDegree[relation];
            }
        }

        this.maxDegree = max;
        this.minDegree = min;
        this.nodeDegree = nodeDegree;
        this.adjacentMatrix = matrix;
    }

    @Override
    public ArrayList<Integer> getVertexEdgesById(int id) {
        ArrayList<Integer> relations = new ArrayList<>();

        for (Edge e: this.getEdges()) {
            if (e.getFrom() == id) {
                relations.add(e.getTo());
            } else if(e.getTo() == id) {
                relations.add(e.getFrom());
            }
        }

        return relations;
    }

    @Override
    public ArrayList<Edge> getEdgesAsArrayList() {
        return new ArrayList<Edge>(Arrays.asList(this.edges));
    }

    @Override
    public Integer getChromaticNumber() {
        return chromaticNumber;
    }

    @Override
    public void setChromaticNumber(Integer chromaticNumber) {
        this.chromaticNumber = chromaticNumber;
    }

    public StopWatch getWatch() {
        return watch;
    }

    public void setWatch(StopWatch watch) {
        this.watch = watch;
    }

    @Override
    public UpperBound getUpperBoundAlgorithm() {
        return upperBoundAlgorithm;
    }

    @Override
    public void setUpperBoundAlgorithm(UpperBound algorithm) {
        this.upperBoundAlgorithm = algorithm;
    }

    @Override
    public LowerBound getLowerBoundAlgorithm()
    {
        return lowerBoundAlgorithm;
    }

    @Override
    public void setLowerBoundAlgorithm(LowerBound algorithm)
    {
        this.lowerBoundAlgorithm = algorithm;
    }

    @Override
    public int[] getNodeDegree()
    {
        return this.nodeDegree;
    }

    @Override
    public Integer getMaximumDegree()
    {
        return this.maxDegree;
    }

    @Override
    public Integer getMinimumDegree()
    {
        return this.minDegree;
    }

    @Override
    public GraphType getGraphType()
    {
        return this.graphType;
    }

    @Override
    public void setGraphType(GraphType type)
    {
        this.graphType = type;
    }

    @Override
    public double[] getGraphFeatures()
    {
        return this.graphFeatures;
    }

    @Override
    public void setGraphFeatures(double[] features)
    {
        this.graphFeatures = features;
    }

    @Override
    public Double getBest_time()
    {
        return this.best_time;
    }

    @Override
    public void setBest_time(double time )
    {
        this.best_time = time;
    }

    @Override
    public Integer getBest_chromatic() {
        return this.best_chromatic;
    }

    @Override
    public void setBest_chromatic(int chromatic) {
        this.best_chromatic = chromatic;
    }



    @Override
    public Algorithm getBestAlgorithm() {
        return this.bestAlgorithm;
    }

    @Override
    public void setBestAlgorithm(Algorithm best)
    {
        this.bestAlgorithm = best;
    }

    @Override
    public NeuralNetwork getNeuralNetwork ()
    {
        return this.MLP;
    }
}
