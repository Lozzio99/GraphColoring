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
import java.util.Arrays;

public class Trainer
{
    private static ArrayList<Matrix> in ;
    private static ArrayList<double []> test;
    private static ArrayList<Matrix> out ;

    public Trainer()
    {
        in = new ArrayList<>();
        test = new ArrayList<>();
        out = new ArrayList<>();
        Controller.setup();
    }
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
        Main.factory.getGraphRepository().getNeuralNetwork().setLearningRate(Configuration.LEARNING_CONSTANT);
        while (true)
        {
            //int k = new Random().nextInt(in.size());
            for (int k = 0; k< in.size(); k++)
                Main.factory.getGraphRepository().getNeuralNetwork().train(in.get(k),out.get(k));
        }
    }
    public static void overwriteTraining()
    {
        try
        {
            File myObj = new File(String.format("%s/TRAINING.txt", Configuration.MODEL_PATH));
            FileWriter myFw = new FileWriter(myObj,false);
            for (int i = 0; i< in.size(); i++)
            {
                myFw.write(" graph "+ (i+1)+"\n");
                myFw.write(Arrays.toString(test.get(i)));
                myFw.write("\nreal output : \n");
                myFw.write(out.get(i).getPrintMatrix());
                myFw.write("prediction :\n");
                myFw.write(Main.factory.getGraphRepository().getNeuralNetwork().feedforward(test.get(i)).getPrintMatrix());
                myFw.write("---------------\n ");
                myFw.write("\n---------------\n");
            }
            myFw.close();
        }
        catch (IOException e)
        {
            if (Configuration.isDebugging())
                e.printStackTrace();
        }
    }
}
