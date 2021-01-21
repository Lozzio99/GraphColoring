package graph.selection.MLP.utils;

import graph.Main;
import graph.selection.MLP.libs.Matrix;
import graph.selection.MLP.utils.PersonalTrainer;

public class Prediction
{
    private double [] features;
    public void testGraph(double [] features)
    {
        this.features  = PersonalTrainer.normalization(features);
    }
    public Matrix prediction(double [] features)
    {
        this.features  = PersonalTrainer.normalization(features);
        return Main.factory.getGraphRepository().getNeuralNetwork().feedforward(this.features);
    }
}
