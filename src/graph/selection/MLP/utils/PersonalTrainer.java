package graph.selection.MLP.utils;

import graph.Configuration;
import graph.Main;
import graph.enums.Algorithm;
import graph.features.GraphFeatures;
import graph.selection.MLP.controls.Controller;
import graph.selection.MLP.controls.Trainer;
import graph.selection.MLP.libs.Matrix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PersonalTrainer
{
    private ArrayList<double []> features;
    private ArrayList<Performance> performances;
    private static Matrix[] MLP_INPUT;
    private static Matrix[] MLP_OUTPUT;

    public PersonalTrainer()
    {
        this.features = new ArrayList<>();
        this.performances = new ArrayList<>();
    }
    public void executeTraining()
    {
        convertToMatrix();
        if (Configuration.FEATURE_RESET)
            writeForNextTraining();
        trainMLP();
    }
    public void trainMLP()
    {
        //writeForNextTraining();
        Controller.setup();
        Trainer brock = new Trainer();
        try {
            brock.read();
        } catch (IOException e) {
            if (Configuration.isDebugging())
                e.printStackTrace();
        }
        brock.train();
        //while true, end of flow
    }

    public void addLinkedPerformance (double [] features, Performance bestPerformance)
    {
        features = normalization(features);
        this.features.add(features);
        this.performances.add(bestPerformance);
    }

    public static double[] normalization(double[] m)
    {
        double normalized [] = new double [Configuration.NUMBER_OF_FEATURES];
        normalized [0] = normalize(m[0], GraphFeatures.maxVertices,0);
        normalized [1] = normalize(m[1],GraphFeatures.maxEdges,0);
        normalized [2] = normalize(m[2],GraphFeatures.maxLB,0);
        normalized [3] = normalize(m[3],GraphFeatures.maxUB,0);
        normalized [4] = normalize(m[4],GraphFeatures.maxDegree,0);
        normalized [5] = normalize(m[5],GraphFeatures.maxDegree,0);
        normalized [6] = normalize(m[6],GraphFeatures.maxAverage,0);
        normalized [7] = normalize(m[7],GraphFeatures.maxDensity,0);
        normalized [8] = normalize(m[8],GraphFeatures.maxCLUSTER,0);
        return normalized;
    }

    public static double normalize(double x, double max, double min)
    {
        if (x == min)
            return 0;
        else if (x == max)
            return 1;
        return (x/(max - min));
    }

    public void convertToMatrix()
    {
        MLP_INPUT = new Matrix[this.features.size()];
        MLP_OUTPUT = new Matrix[this.features.size()];
        for (int i = 0; i<MLP_INPUT.length; i++)
        {
            MLP_INPUT[i] = new Matrix(this.features.get(i));
            MLP_INPUT[i].printMatrix();

            Performance k = this.getPerformances().get(i);

            if (k.algorithm().equals(Algorithm.GREEDY))
            {
                MLP_OUTPUT[i] = new Matrix(new double[][]{{1},{0},{0},{0},{0}});
            }
            else if (k.algorithm().equals(Algorithm.GA))
            {
                MLP_OUTPUT[i] = new Matrix(new double[][]{{0},{1},{0},{0},{0}});
            }
            else if (k.algorithm().equals(Algorithm.RLF))
            {
                MLP_OUTPUT[i] = new Matrix(new double[][]{{0},{0},{1},{0},{0}});
            }
            else if (k.algorithm().equals(Algorithm.DSATUR))
            {
                MLP_OUTPUT[i] = new Matrix(new double[][]{{0},{0},{0},{1},{0}});
            }
            else if (k.algorithm().equals(Algorithm.BACKTRACKING))
            {
                MLP_OUTPUT[i] = new Matrix(new double[][]{{0},{0},{0},{0},{1}});
            }
        }
    }
    public void setAllFeatures(ArrayList<double[]> features)
    {
        this.features = features;
    }
    public ArrayList<double []> getFeatures ()
    {
        return this.features;
    }
    public void setPerformances(ArrayList<Performance> performances)
    {
        this.performances = performances;
    }
    public ArrayList<Performance> getPerformances ()
    {
        return this.performances;
    }

    public void writeForNextTraining()
    {
        try
        {
            File myObj = new File(String.format("%s/TRAINING.txt", Configuration.MODEL_PATH));
            FileWriter myFw = new FileWriter(myObj,false);
            for (int i = 0; i< this.features.size(); i++)
            {

                myFw.write(" graph "+ (i+1)+"\n");
                myFw.write(Arrays.toString(this.features.get(i)));
                // System.out.println(this.TRAINING_PERFORMANCE.get(i).BEST_ALGORITHM);
                myFw.write("\nreal output : \n");
                myFw.write(MLP_OUTPUT[i].getPrintMatrix());
                myFw.write("prediction :\n");
                myFw.write(Main.factory.getGraphRepository().getNeuralNetwork().feedforward(this.features.get(i)).getPrintMatrix());
                myFw.write("\n ---------------\n");
                myFw.write("---------------");
            }
            myFw.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
