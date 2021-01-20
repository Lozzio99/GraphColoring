package graph.selection.MLP.controls;

import graph.Configuration;
import graph.selection.MLP.libs.Matrix;
import graph.selection.MLP.utils.ModelHandler;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

public class NeuralNetwork extends KeyAdapter
{
    private Matrix weights_ih;
    private Matrix weights_ho;
    private Matrix weights_hh;
    private Matrix bias_hh;
    private Matrix bias_ih;
    private Matrix bias_ho;

    private static double lk = 0.1;


    public void exportModel()
    {
        Matrix[] modelMatrixObject = new Matrix[6];
        modelMatrixObject[0] = weights_ih;
        modelMatrixObject[1] = weights_ho;
        modelMatrixObject[2] = bias_ih;
        modelMatrixObject[3] = bias_ho;
        modelMatrixObject[4] = weights_hh;
        modelMatrixObject[5] = bias_hh;

        ModelHandler.export(String.format("%s/model.mlp", Configuration.MODEL_PATH), modelMatrixObject);
    }

    private void loadModel()
    {
        if (!new File(String.format("%s/model.mlp", Configuration.MODEL_PATH)).exists()) {
            return;
        }

        Matrix[] modelMatrixObject = ModelHandler.loadMatrix(String.format("%s/model.mlp", Configuration.MODEL_PATH));

        if (modelMatrixObject == null) {
            System.out.println("NOT LOADED");
            return;
        }
        this.weights_ih = modelMatrixObject[0];
        this.weights_ho = modelMatrixObject[1];
        this.bias_ih = modelMatrixObject[2];
        this.bias_ho = modelMatrixObject[3];
        this.weights_hh = modelMatrixObject[4];
        this.bias_hh = modelMatrixObject[5];
    }

    public void setLearningRate(double lk)
    {
        NeuralNetwork.lk = lk;
    }
    public NeuralNetwork(int in_nodes, int hid_nodes, int out_nodes, boolean random)
    {
        if (random)  //  -1  < random <  1  //
        {
            this.weights_ih = new Matrix(hid_nodes, in_nodes);
            this.weights_ih= Matrix.randomize(this.weights_ih);
            this.weights_hh = new Matrix(hid_nodes,hid_nodes);
            this.weights_hh= Matrix.randomize(this.weights_hh);
            this.weights_ho = new Matrix(out_nodes,hid_nodes);
            this.weights_ho= Matrix.randomize(this.weights_ho);
            this.bias_ih = new Matrix(hid_nodes,1);
            this.bias_ih = Matrix.randomize(this.bias_ih);
            this.bias_hh = new Matrix(hid_nodes,1);
            this.bias_hh = Matrix.randomize(this.bias_hh);
            this.bias_ho = new Matrix(out_nodes,1);
            this.bias_ho = Matrix.randomize(this.bias_ho);
            exportModel();
        }
        else
        {
            loadModel();
        }

       // exportModel();
    }


    public void printAllWeights ()
    {
        this.weights_ho.printMatrix();
        this.weights_ih.printMatrix();
        this.bias_ih.printMatrix();
        this.bias_ho.printMatrix();
    }

    public Matrix feedforward(double [] input)
    {
        //generating hidden output
        Matrix inputs = new Matrix(input);
        Matrix hidden1 = Matrix.multiply(this.weights_ih,inputs);
        hidden1 = Matrix.add(hidden1,this.bias_ih);
        hidden1 = Matrix.map(hidden1);

        //generating 2nd hidden output
        Matrix hidden2 = Matrix.multiply(this.weights_hh,hidden1);
        hidden2 = Matrix.add(hidden2,this.bias_hh);
        hidden2 = Matrix.map(hidden2);

        //generating final output   NEW MATRIX RESULTING FROM THE EVOLVING OUTPUT
        Matrix outputs = Matrix.multiply(this.weights_ho,hidden2);
        outputs = Matrix.add(outputs,this.bias_ho);
        outputs= Matrix.map(outputs);
        return outputs;
    }

    public void train(Matrix given, Matrix targets)
    {
        //generating hidden output
        Matrix inputs = given;
        Matrix hidden1 = Matrix.multiply(this.weights_ih,inputs);
        hidden1 = Matrix.add(hidden1,this.bias_ih);
        //1
        hidden1= Matrix.map(hidden1);
        // 2ND HIDDEN OUTPUT
        Matrix hidden2 = Matrix.multiply(this.weights_hh,hidden1);
        hidden2 = Matrix.add(hidden2,this.bias_hh);
        hidden2 = Matrix.map(hidden2);
        //generating final output   NEW MATRIX RESULTING FROM THE EVOLVING OUTPUT
        Matrix outputs =Matrix.multiply(this.weights_ho,hidden2);
        outputs = Matrix.add(outputs,this.bias_ho);
        outputs= Matrix.map(outputs);

        //   map((E-O)(dfunc(O))H^)

        //E-O
        Matrix tgt = new Matrix(targets.getMatrix());
        //calculate out error
        Matrix out_error = Matrix.subtract(tgt, outputs);
        // calculate out gradient
        Matrix o_gradient = Matrix.dfunc(outputs);
        o_gradient = o_gradient.multiply(out_error);
        o_gradient = o_gradient.multiply(lk);
        //hidden layer transposed
        Matrix hid_transp = Matrix.transpose(hidden2);

        //calculate and adjust out weights
        //OH^
        Matrix dw_ho = Matrix.multiply(o_gradient,hid_transp);
        this.weights_ho = Matrix.add(this.weights_ho,dw_ho);
        this.bias_ho = Matrix.add(this.bias_ho,o_gradient);

        //calculate hidden error
        Matrix w_ho_transp = Matrix.transpose(this.weights_ho);
        Matrix hidden2_er = Matrix.multiply(w_ho_transp,out_error);
        //calculate hidden gradient
        Matrix h2_gradient = Matrix.dfunc(hidden2);
        h2_gradient = h2_gradient.multiply(hidden2_er);
        h2_gradient = h2_gradient.multiply(lk);

        //calculate and adjust hidden weights
        Matrix hidden2_T = Matrix.transpose(hidden1);
        Matrix d_hh = Matrix.multiply(h2_gradient,hidden2_T);
        this.weights_hh = Matrix.add(this.weights_hh,d_hh);
        this.bias_hh = Matrix.add(this.bias_hh,h2_gradient);

        //calculate hidden error2
        Matrix w_hh_transp = Matrix.transpose(this.weights_hh);
        Matrix hidden1_er = Matrix.multiply(w_hh_transp,hidden2_er);
        //calculate hidden gradient
        Matrix h1_gradient = Matrix.dfunc(hidden1);
        h1_gradient = h1_gradient.multiply(hidden1_er);
        h1_gradient = h1_gradient.multiply(lk);

        //calculate and adjust hidden weights
        Matrix inputs_T = Matrix.transpose(inputs);
        Matrix d_ih = Matrix.multiply(h1_gradient,inputs_T);
        this.weights_ih = Matrix.add(this.weights_ih,d_ih);
        this.bias_ih = Matrix.add(this.bias_ih,h1_gradient);    }


}
