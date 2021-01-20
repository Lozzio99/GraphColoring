package graph.selection.MLP;
import graph.Configuration;
import graph.selection.MLP.controls.NeuralNetwork;
import graph.selection.MLP.libs.Matrix;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Trainer
{
    private static NeuralNetwork nn;
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
            nn.feedforward(test.get(i)).printMatrix();
            System.out.println("//------------------------//");
        }
    }
    public static void main(String [] args) throws IOException
    {
        Configuration.TRAINING_MODE_ENABLED = true;
        String fileName = "C:\\Users\\Lorenzo\\Documents\\graph-colouring-group05_2020-main\\phase3\\models\\result.txt";
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
        nn = new NeuralNetwork(Configuration.NUMBER_OF_FEATURES,Configuration.NUMBER_OF_HIDDEN,Configuration.NUMBER_OF_OUTPUTS,Configuration.MLP_RESET);
        setup();
        nn.setLearningRate(0.005);
        while (true)
        {
            //int k = new Random().nextInt(in.size());
            for (int k = 0; k< in.size(); k++)
                nn.train(in.get(k),out.get(k));
        }

    }
    public static void setup()
    {
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
        KeyListener k = new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (Configuration.TRAINING_MODE_ENABLED)
                {
                    if (e.getKeyCode()==KeyEvent.VK_SPACE)
                    {
                        nn.exportModel();
                        System.out.println("training stopped -> saved");
                        Trainer.test();
                        System.exit(0);
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (Configuration.TRAINING_MODE_ENABLED)
                {
                    if (e.getKeyCode()==KeyEvent.VK_SPACE)
                    {
                        nn.exportModel();
                        System.out.println("training stopped -> saved");
                        Trainer.test();
                        System.exit(0);
                    }
                }
            }
        };
        p.addKeyListener(k);
        p.add(j);
        f.add(p);
        f.setVisible(true);
    }

}
