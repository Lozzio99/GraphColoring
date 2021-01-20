package graph.repositories;

import graph.Configuration;
import graph.domain.Edge;
import graph.enums.Algorithm;
import graph.enums.GraphType;
import graph.enums.LowerBound;
import graph.enums.UpperBound;
import graph.interfaces.GraphRepositoryInterface;
import graph.selection.GraphFeatures;
import graph.selection.MLP.Performance;
import graph.selection.MLP.controls.NeuralNetwork;
import graph.selection.MLP.libs.Matrix;
import graph.utils.StopWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private double[] MLP_INPUT;
    private ArrayList<double[]> TRAINING_FEATURES ;
    private ArrayList<double[]> TRAINING_SUPPLY;
    private ArrayList<Performance> TRAINING_PERFORMANCE;
    private Double best_time;
    private Integer best_chromatic;
    private  Algorithm bestAlgorithm;
    public GraphFeatures features ;



    private StopWatch watch;

    public GraphRepository() {
        this.chromaticNumber = null;
        this.chosenAlgorithm = null;
        this.upperBound = null;
        this.lowerBound = null;
        this.watch = new StopWatch();
        features = new GraphFeatures();
        if (Configuration.TRAINING_MODE_ENABLED)
        {
            this.TRAINING_FEATURES = new ArrayList<>();
            this.TRAINING_PERFORMANCE = new ArrayList<>();
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
        this.MLP_INPUT = null;
        this.best_time = null;
        this.best_chromatic = null;
        this.chosenAlgorithm = Configuration.DEFAULT_ALGORITHM;
        this.lowerBoundAlgorithm = Configuration.DEFAULT_LOWER_BOUND;
        this.upperBoundAlgorithm = Configuration.DEFAULT_UPPER_BOUND;
        this.graphType = null;
        this.watch = new StopWatch();
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
    public LowerBound getLowerBoundAlgorithm() {
        return lowerBoundAlgorithm;
    }

    @Override
    public void setLowerBoundAlgorithm(LowerBound algorithm) {
        this.lowerBoundAlgorithm = algorithm;
    }

    @Override
    public int[] getNodeDegree() {
        return this.nodeDegree;
    }

    @Override
    public Integer getMaximumDegree() {
        return this.maxDegree;
    }

    @Override
    public Integer getMinimumDegree() {
        return this.minDegree;
    }

    @Override
    public GraphType getGraphType() {
        return this.graphType;
    }

    @Override
    public void setGraphType(GraphType type) {
        this.graphType = type;
    }

    @Override
    public double[] getGraphFeatures() {
        return this.graphFeatures;
    }

    @Override
    public void setGraphFeatures(double[] features) {
        this.graphFeatures = features;
    }

    @Override
    public double[] getMLP_INPUT() {
        return this.MLP_INPUT;
    }

    @Override
    public void normalizePREDICTION(double[] input)
    {
        double[] normalized = new double [Configuration.NUMBER_OF_FEATURES];
        normalization(input, normalized);
        this.MLP_INPUT = normalized;
    }


    @Override
    public void addPerformance(double [] features, Performance performance)
    {
        if (features!=null && performance!= null)
        {
            this.TRAINING_FEATURES.add(features);
            this.TRAINING_PERFORMANCE.add(performance);
        }
    }

    @Override
    public ArrayList<Performance> getPerformance()
    {
        return this.TRAINING_PERFORMANCE;
    }

    @Override
    public ArrayList<double []> getFeatures()
    {
        return this.TRAINING_FEATURES;
    }

    @Override
    public void setFeatures(ArrayList<double []> training)
    {
        this.TRAINING_SUPPLY = training;
    }

    @Override
    public void normalizeTRAINING()
    {
        ArrayList<double[]> training_supply = new ArrayList<>();
        for (double[] m : this.getFeatures())
        {
            double normalized [] = new double [Configuration.NUMBER_OF_FEATURES];
            normalization(m, normalized);
            training_supply.add(normalized);
        }
        this.setFeatures(training_supply);
    }
    @Override
    public void normalization(double[] m, double[] normalized) {
        normalized [0] = normalize(m[0],5000,0);
        normalized [1] = normalize(m[1],1200000,0);
        normalized [2] = normalize(m[2],100,0);
        normalized [3] = normalize(m[3],100,0);
        normalized [4] = normalize(m[4],700,0);
        normalized [5] = normalize(m[5],700,0);
        normalized [6] = normalize(m[6],600,0);
        normalized [7] = normalize(m[7],1,0);
        normalized [8] = normalize(m[8],1,0);
    }

    @Override
    public double normalize(double x, double max, double min)
    {
        if (x == min)
            return 0;
        else if (x == max)
            return 1;
        return (x/(max - min));
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
    public void trainMLP()
    {
        System.out.println("TRAINING START");
        /*
         * USAGE:
         * if @param random is true weights are overwritten with random values
         *
         * if @param random is false weights are imported from the model
         */

        this.MLP = new NeuralNetwork(Configuration.NUMBER_OF_FEATURES,Configuration.NUMBER_OF_HIDDEN,Configuration.NUMBER_OF_OUTPUTS,Configuration.MLP_RESET);


        //TODO: fileWriter for the features - store them permanently
        normalizeTRAINING();
        //number of graphs  -> IMPORTANT!
        Matrix [] mlp_inputs = new Matrix[this.getPerformance().size()];
        Matrix [] mlp_outputs = new Matrix[this.getPerformance().size()];


        for (int i = 0; i<mlp_inputs.length; i++)
        {


            mlp_inputs[i] = new Matrix(this.TRAINING_SUPPLY.get(i));
            mlp_inputs[i].printMatrix();

            Performance k = this.getPerformance().get(i);

            if (k.algorithm().equals(Algorithm.GREEDY))
            {
                mlp_outputs[i] = new Matrix(new double[][]{{1},{0},{0},{0},{0}});
            }
            else if (k.algorithm().equals(Algorithm.GA))
            {
                mlp_outputs[i] = new Matrix(new double[][]{{0},{1},{0},{0},{0}});
            }
            else if (k.algorithm().equals(Algorithm.RLF))
            {
                mlp_outputs[i] = new Matrix(new double[][]{{0},{0},{1},{0},{0}});
            }
            else if (k.algorithm().equals(Algorithm.DSATUR))
            {
                mlp_outputs[i] = new Matrix(new double[][]{{0},{0},{0},{1},{0}});
            }
            else if (k.algorithm().equals(Algorithm.BACKTRACKING))
            {
                mlp_outputs[i] = new Matrix(new double[][]{{0},{0},{0},{0},{1}});
            }
        }

        JFrame f = new JFrame();
        f.setSize(300,100);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JPanel p = new JPanel();
        p.setBackground(Color.BLACK);
        p.setFocusable(true);
        p.setPreferredSize(f.getSize());
        JLabel j=new JLabel("press SPACE to stop training");
        j.setForeground(Color.WHITE);
        j.setLabelFor(p);
        KeyListener k = new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (Configuration.TRAINING_MODE_ENABLED)
                {
                    if (e.getKeyCode()==KeyEvent.VK_SPACE)
                    {
                        MLP.exportModel();
                        System.out.println("training stopped -> saved");
                        System.exit(0);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Configuration.TRAINING_MODE_ENABLED)
                {
                    if (e.getKeyCode()==KeyEvent.VK_SPACE)
                    {
                        MLP.exportModel();
                        System.out.println("training stopped -> saved");
                        System.exit(0);
                    }
                }
            }
        } ;
        p.addKeyListener(k);
        p.add(j);
        f.add(p);
        f.setVisible(true);
        double lk = Configuration.LEARNING_CONSTANT;
        for (int i =0; i< 50; i++)
        {
            if (i%10000==0 && lk<0.25)
            {
                lk+=0.005;
                this.MLP.setLearningRate(lk);
            }
            int test = new Random().nextInt(mlp_inputs.length);
            this.MLP.train(mlp_inputs[test],mlp_outputs[test]);
        }
        this.MLP.exportModel();

        if (Configuration.VERBOSE)
        {
            if (Configuration.FEATURE_RESET && Configuration.TRAINING_MODE_ENABLED)
            {
                try
                {
                    File myObj = new File(String.format("%s/result.txt", Configuration.MODEL_PATH));
                    FileWriter myFw = new FileWriter(myObj,false);
                    for (int i = 0; i< mlp_inputs.length; i++)
                    {

                        myFw.write("---------------");
                        myFw.write(" graph "+ (i+1));
                        myFw.write(Arrays.toString(this.TRAINING_SUPPLY.get(i)));
                        // System.out.println(this.TRAINING_PERFORMANCE.get(i).BEST_ALGORITHM);
                        myFw.write("real output : ");
                        myFw.write(mlp_outputs[i].getPrintMatrix());
                        myFw.write(" prediction :");
                        myFw.write(this.MLP.feedforward(this.TRAINING_SUPPLY.get(i)).getPrintMatrix());
                        myFw.write(" ---------------\n");
                    }
                    myFw.close();
                }
                catch (IOException e)
                {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
            for (int i = 0; i< mlp_inputs.length; i++)
            {

                System.out.println("---------------");
                System.out.println(" graph "+ (i+1));
                System.out.println(Arrays.toString(this.TRAINING_SUPPLY.get(i)));
             // System.out.println(this.TRAINING_PERFORMANCE.get(i).BEST_ALGORITHM);
                System.out.println("real output : ");
                mlp_outputs[i].printMatrix();
                System.out.println(" prediction :");
                this.MLP.feedforward(this.TRAINING_SUPPLY.get(i)).printMatrix();
                System.out.println(" ---------------\n");
            }
            System.out.println(" TRAINING FINISHED ");
        }

        // save the training changes
        this.MLP.exportModel();
    }

    @Override
    public double[][] getMLP_GUESS()
    {
        /* CLASS NEURAL NETWORK USAGE:
         *
         * if @param random is true weights are overwritten with random values
         *
         * if @param random is false weights are imported from the model
         */
        this.MLP = new NeuralNetwork(Configuration.NUMBER_OF_FEATURES,Configuration.NUMBER_OF_HIDDEN,Configuration.NUMBER_OF_OUTPUTS,Configuration.MLP_RESET);

        //get the prediction
        double [][] MLP_GUESS = this.MLP.feedforward(this.MLP_INPUT).getMatrix();

        if (!Configuration.VERBOSE)
        {
            System.out.println("---------------");
            System.out.println(Arrays.toString(this.getMLP_INPUT()));
            System.out.println(" prediction :");
            System.out.println(Arrays.deepToString(MLP_GUESS));
            System.out.println(" ---------------\n");
        }
        return MLP_GUESS;
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
}
