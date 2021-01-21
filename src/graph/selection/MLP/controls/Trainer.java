package graph.selection.MLP.controls;
import graph.Configuration;
import graph.Main;
import graph.selection.MLP.controls.NeuralNetwork;
import graph.selection.MLP.libs.Matrix;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

public class Trainer
{
    private static ArrayList<Matrix> in = new ArrayList<>();
    private static ArrayList<double []> test = new ArrayList<>();
    private static ArrayList<Matrix> out = new ArrayList<>();

    public static void test()
    {
        for (int i = 0; i< test.size(); i++)
        {
            System.out.println("graph "+ (i+1));
            System.out.println(" target : ");
            out.get(i).printMatrix();
            System.out.println(" prediction : ");
            Main.factory.getGraphRepository().getNeuralNetwork().feedforward(test.get(i)).printMatrix();
            System.out.println("//------------------------//");
        }
        Main.factory.getGraphRepository().getTrainer().writeForNextTraining();
    }
    public void read() throws IOException
    {
        String fileName = String.format("%s/TRAINING.txt", Configuration.MODEL_PATH);
        File file = new File(fileName);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String record;
        BufferedReader br = new BufferedReader(fr);

        while ((record = br.readLine())!=null)
        {
            if (record.startsWith("["))
            {
                record = record.substring(1,record.indexOf("]"));
                String[] input = record.split(",");
                double inputs[] = new double[input.length];
                for (int i = 0; i< input.length; i++)
                {
                    inputs[i] = Double.parseDouble(input[i]);
                }
                test.add(inputs);
                in.add(new Matrix(inputs));
            }

            if(record.startsWith("real output :"))
            {
                double[] outputs = new double[5];
                for (int i = 0; i< outputs.length; i++)
                {
                    record = br.readLine();
                    outputs[i] = Double.parseDouble(record);
                }
                out.add(new Matrix(outputs));
                if (record.startsWith(" prediction :"))
                    for (int i = 0; i< 8; i++)
                    {
                        br.readLine();
                    }
            }
        }
    }
    public void train()
    {
        //setup();
        Main.factory.getGraphRepository().getNeuralNetwork().setLearningRate(Configuration.LEARNING_CONSTANT);
        while (true)
        {
            //int k = new Random().nextInt(in.size());
            for (int k = 0; k< in.size(); k++)
                Main.factory.getGraphRepository().getNeuralNetwork().train(in.get(k),out.get(k));
        }
    }
}
