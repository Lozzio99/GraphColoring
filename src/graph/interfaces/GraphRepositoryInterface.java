package graph.interfaces;

import graph.domain.Edge;
import graph.enums.Algorithm;
import graph.enums.GraphType;
import graph.enums.LowerBound;
import graph.enums.UpperBound;
import graph.selection.GraphFeatures;
import graph.selection.MLP.Performance;
import graph.utils.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;

public interface GraphRepositoryInterface {
    Integer getUpperBound();

    GraphFeatures features();

    void setFeatures(ArrayList<double[]> features);

    void setUpperBound(Integer upperBound);

    Integer getLowerBound();

    void setLowerBound(Integer lowerBound);

    Algorithm getChosenAlgorithm();

    void setChosenAlgorithm(Algorithm chosenAlgorithm);

    Integer getVertexCount();

    void setVertexCount(Integer vertexCount);

    Integer getEdgeCount();

    void setEdgeCount(Integer edgeCount);

    Edge[] getEdges();

    void setEdges(Edge[] edges);

    int[][] getAdjacentMatrix();

    void createAdjacentMatrix();

    Integer getChromaticNumber();

    void setChromaticNumber(Integer chromaticNumber);

    ArrayList<Integer> getVertexEdgesById(int id);

    ArrayList<Edge> getEdgesAsArrayList();

    void reset();

    StopWatch getWatch();

    void setWatch(StopWatch watch);

    UpperBound getUpperBoundAlgorithm();

    void setUpperBoundAlgorithm(UpperBound algorithm);

    LowerBound getLowerBoundAlgorithm();

    void setLowerBoundAlgorithm(LowerBound algorithm);

    int [] getNodeDegree ();

    Integer getMaximumDegree();

    Integer getMinimumDegree();

    GraphType getGraphType();

    void setGraphType(GraphType type);

    double[] getGraphFeatures();

    void setGraphFeatures(double[] features);

    void addPerformance(double [] features, Performance performance);

    ArrayList<Performance> getPerformance();

    ArrayList<double[]> getFeatures();

    Double getBest_time ();

    void setBest_time (double time);

    Integer getBest_chromatic ();

    void setBest_chromatic (int chromatic);

    void trainMLP();

    void normalizeTRAINING();

    double [][] getMLP_GUESS();

    Algorithm getBestAlgorithm();

    void setBestAlgorithm(Algorithm bestAlgorithm);

    void normalizePREDICTION(double [] input);

    double [] getMLP_INPUT();

    void normalization(double[] m, double[] normalized);

    double normalize(double x, double max, double min);
}
