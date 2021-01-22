package graph;

import graph.exceptions.TimelimitExceededException;
import graph.factory.LocalFactory;
import graph.repositories.GraphRepository;
import graph.selection.MLP.controls.Trainer;
import graph.selection.PreCheck;
import graph.utils.tasks.ImportTask;

import java.io.IOException;

public class Main {
    public static LocalFactory factory = new LocalFactory(new GraphRepository());

    public static void main(String[] args)
    {

        if (!Configuration.FEATURE_RESET && Configuration.TRAINING_MODE_ENABLED)
        {
            Trainer preExistingFeaturesTrainer = new Trainer();
            try {
                preExistingFeaturesTrainer.read();
            } catch (IOException e) {
                if (Configuration.isDebugging())
                    e.printStackTrace();
            }
            preExistingFeaturesTrainer.train();
        }

        if (args.length < 1) {
            System.out.println("Error! No filename selected.");
            System.exit(0);
        }

        String inputfile = args[0];


        if (inputfile.contains("XXXX")) {
            int fileCount = Integer.parseInt(args[1]);

            for (int i = 1; i <= fileCount; i++)
            {
                Main.factory.getGraphRepository().reset();

                if (Configuration.TRAINING_MODE_ENABLED)
                {
                    Main.factory.getGraphRepository().getWatch().start(Configuration.TRAINING_TIMELIMIT);

                }
                else
                    Main.factory.getGraphRepository().getWatch().start(Configuration.DEFAULT_TIMELIMIT);
                String tempFile = inputfile.replace("XXXX", String.format("%02d", i));

                System.out.println(String.format("Loading: %s ...", tempFile));
                try {
                    ImportTask.execute(tempFile);
                    Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
                } catch (IOException | TimelimitExceededException e) {
                    Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();

                    if(Configuration.isDebugging()) {
                        e.printStackTrace();
                    }
                }

                try {

                    PreCheck.selectAndExecute();
                    if(Configuration.VERBOSE)
                    {
                        System.out.println(String.format("The Lower bound is %s", Main.factory.getGraphRepository().getLowerBound()));
                        System.out.println(String.format("The Upper bound is %s", Main.factory.getGraphRepository().getUpperBound()));
                    }
                    if (Main.factory.getGraphRepository().getChromaticNumber() != null)
                    {
                        if (Configuration.VERBOSE)
                            System.out.println("The best algorithm > "+ Main.factory.getGraphRepository().getBestAlgorithm().toString());
                        System.out.print(String.format("The Chromatic number is %s", Main.factory.getGraphRepository().getChromaticNumber()));
                        System.out.println("      <  "+ Main.factory.getGraphRepository().getBest_time()+ " ns.");
                    }
                } catch (Exception e) {
                    Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();

                    if(Configuration.isDebugging()) {
                        e.printStackTrace();
                    }
                }

                Main.factory.getGraphRepository().getWatch().stop();

                System.out.println(String.format("Graph %s took %s ms to execute", inputfile.replace("XXXX", Integer.toString(i)), Main.factory.getGraphRepository().getWatch().getTimeInMillis()));
            }
            if (Configuration.TRAINING_MODE_ENABLED)
            {
                Main.factory.getGraphRepository().getTrainer().executeTraining();
            }


        } else {
            Main.factory.getGraphRepository().reset();
            Main.factory.getGraphRepository().getWatch().start(Configuration.DEFAULT_TIMELIMIT);
            try {
                ImportTask.execute(inputfile);
                Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
            } catch (IOException | TimelimitExceededException e) {
                Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();

                if(Configuration.isDebugging()) {
                    e.printStackTrace();
                }
            }

            try {
                PreCheck.selectAndExecute();
                System.out.println(String.format("The Lower bound is %s", Main.factory.getGraphRepository().getLowerBound()));
                System.out.println(String.format("The Upper bound is %s", Main.factory.getGraphRepository().getUpperBound()));

                if (Main.factory.getGraphRepository().getChromaticNumber() != null) {
                    System.out.println(String.format("The Chromatic number is %s", Main.factory.getGraphRepository().getChromaticNumber()));
                }

            } catch (Exception e) {
                Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();

                if(Configuration.isDebugging()) {
                    e.printStackTrace();
                }
            }
            Main.factory.getGraphRepository().getWatch().stop();
            System.out.println(String.format("Graph %s took %s ms to execute", inputfile, Main.factory.getGraphRepository().getWatch().getTimeInMillis()));
        }
    }
}
