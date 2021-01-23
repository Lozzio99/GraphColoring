package graph.selection.MLP.controls;

import graph.Configuration;
import graph.Main;
import graph.selection.MLP.controls.Trainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller
{
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
        p.addKeyListener(addListener());
        p.add(j);
        f.add(p);
        f.setVisible(true);
    }
    public static KeyListener addListener()
    {
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
                        Main.factory.getGraphRepository().getNeuralNetwork().exportModel();
                        System.out.println("training stopped -> saved");
                        Trainer.overwriteTraining();
                        Trainer.test();
                        System.exit(0);
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e)
            {
               keyPressed(e);
            }
        };
        return k;
    }
}
