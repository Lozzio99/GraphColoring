package graph.selection;

import graph.Configuration;
import graph.Main;
import graph.algorithms.*;
import graph.enums.Algorithm;
import graph.enums.GraphType;
import graph.algorithms.BackTracking.BTChromaticNumber;
import graph.enums.UpperBound;
import graph.selection.MLP.utils.Performance;
import graph.enums.LowerBound;
import graph.selection.MLP.utils.PersonalTrainer;
import graph.selection.MLP.utils.Prediction;

public class PreCheck
{

    public static void selectAndExecute() throws Exception
    {
        //TODO: check for sub-graphs and possibly run this process for all sub-graphs

        // training mode provides all graphs as instances
        if (!isGraphType() || Configuration.TRAINING_MODE_ENABLED)
        {
            if (Main.factory.getGraphRepository().getUpperBoundAlgorithm().equals(UpperBound.GREEDY))
            {
                Greedy.calculate();
            } else if (Main.factory.getGraphRepository().getUpperBoundAlgorithm().equals(UpperBound.WELSH_POWELL)) {
                WelshPowell.calculate();
            }

            if (Main.factory.getGraphRepository().getLowerBoundAlgorithm().equals(LowerBound.TOMITA)) {
                Tomita.calculate();
            }

            // Evaluating other features for MLP/BN
            Main.factory.getGraphRepository().features().evaluateFeatures();

            if (Configuration.VERBOSE && Configuration.TRAINING_MODE_ENABLED)
                System.out.println(" COLORING ANALYSIS ");


            // if upperBound is equal to lower -> take time of the greedy algorithm
            // training mode provides all graphs as instances
            if (!Main.factory.getGraphRepository().getUpperBound().equals(Main.factory.getGraphRepository().getLowerBound()) || Configuration.TRAINING_MODE_ENABLED)
            {
                if (Configuration.VERBOSE)
                {
                    System.out.println(String.format("The Lower bound is %s", Main.factory.getGraphRepository().getLowerBound()));
                    System.out.println(String.format("The Upper bound is %s", Main.factory.getGraphRepository().getUpperBound()));
                }


                // each graph (features) is linked with a @Performance
                // training mode evaluates all algorithms sequentially
                // stores for each instance the @BestChromatic number achieved,
                // in case of a tie, the @BestTime algorithm is set as @BestAlgorithm
                if (Configuration.TRAINING_MODE_ENABLED)
                {

                    Main.factory.getGraphRepository().getWatch().start(Configuration.TRAINING_TIMELIMIT);
                    for (int i = 0; i< Configuration.TRAINING_INSTANCES; i++)
                    {
                        try {
                            Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_TIMELIMIT);
                            DSatur.calculate();
                        }catch (Exception  e) {
                            Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
                            if(Configuration.isDebugging()) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_TIMELIMIT);
                            Greedy.calculate();
                        }catch (Exception  e) {
                            Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
                            if(Configuration.isDebugging()) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_TIMELIMIT);
                            BTChromaticNumber.calculate();
                        }catch (Exception  e) {
                            Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
                            if(Configuration.isDebugging()) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_TIMELIMIT);
                            new GeneticAlgorithm();
                        }catch (Exception  e) {
                            Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
                            if(Configuration.isDebugging()) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Main.factory.getGraphRepository().getWatch().setIntermediateDeadline(Configuration.DEFAULT_TIMELIMIT);
                            RecursiveLargeFirst.calculate();
                        }catch (Exception  e) {
                            Main.factory.getGraphRepository().getWatch().terminiateIntermediateDeadline();
                            if(Configuration.isDebugging()) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //add the performance once all iterations have been checked
                    Main.factory.getGraphRepository().features().checkMax();
                    addBestPerformance();
                }
                else   //training mode disabled
                {
                    double [][] algorithm_guessed = new Prediction().prediction(PersonalTrainer.normalization(Main.factory.getGraphRepository().getGraphFeatures())).getMatrix();
                    if (algorithm_guessed[0][0] > 0.5)
                        Main.factory.getGraphRepository().setChosenAlgorithm(Algorithm.GREEDY);
                    else if (algorithm_guessed[1][0] > 0.5)
                        Main.factory.getGraphRepository().setChosenAlgorithm(Algorithm.GA);
                    else if (algorithm_guessed[2][0] > 0.5)
                        Main.factory.getGraphRepository().setChosenAlgorithm(Algorithm.RLF);
                    else if (algorithm_guessed[3][0] > 0.5)
                        Main.factory.getGraphRepository().setChosenAlgorithm(Algorithm.DSATUR);
                    else
                        Main.factory.getGraphRepository().setChosenAlgorithm(Algorithm.BACKTRACKING);
                    if (Configuration.VERBOSE)
                    {
                        System.out.println("MLP has classified : "+ Main.factory.getGraphRepository().getChosenAlgorithm().toString());
                    }

                    if ( Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.DSATUR)) {
                        DSatur.calculate();
                    } else if ( Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.GREEDY)) {
                        Greedy.calculate();
                    } else if ( Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.RLF)) {
                        RecursiveLargeFirst.calculate();
                    } else if ( Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.GA)) {
                        new GeneticAlgorithm();
                    } else {
                        BTChromaticNumber.calculate();
                    }
                }
            }
            else  //training mode disabled and upper equals lower
            {
                Main.factory.getGraphRepository().setChromaticNumber(Main.factory.getGraphRepository().getUpperBound());
            }
        }
    }
    private static void addBestPerformance()
    {
        if (Configuration.VERBOSE)
        {
            System.out.println(" Best Algorithm : ");
            System.out.print(Main.factory.getGraphRepository().getBestAlgorithm());
            System.out.print("   >>   " + Main.factory.getGraphRepository().getBest_chromatic());
            System.out.println("    -> "+Main.factory.getGraphRepository().getBest_time());
        }
        if (Main.factory.getGraphRepository().getChromaticNumber()==null || Main.factory.getGraphRepository().getBest_chromatic()<Main.factory.getGraphRepository().getChromaticNumber())
            Main.factory.getGraphRepository().setChromaticNumber(Main.factory.getGraphRepository().getBest_chromatic());

        Main.factory.getGraphRepository().getTrainer().addLinkedPerformance(Main.factory.getGraphRepository().getGraphFeatures(),new Performance(Main.factory.getGraphRepository().getBest_time(), Main.factory.getGraphRepository().getBest_chromatic(),Main.factory.getGraphRepository().getBestAlgorithm()));
    }
    private static boolean isGraphType() {
        if (Main.factory.getGraphRepository().getGraphType() != null) {
            GraphType chosen = Main.factory.getGraphRepository().getGraphType();
            if (chosen.equals(GraphType.NULL_GRAPH)) {
                SpecialCases.calculateNullGraph();

                return true;
            } else if (chosen.equals(GraphType.COMPLETE_GRAPH)) {
                SpecialCases.calculateCompleteGraph();

                return true;
            } else if (chosen.equals(GraphType.STAR_GRAPH)) {
                SpecialCases.calculateStarGraph();

                return true;
            } else if (chosen.equals(GraphType.BIPARTITE_GRAPH)) {
                SpecialCases.calculateBipartiteGraph();

                return true;
            } else if (chosen.equals(GraphType.CYCLE_GRAPH)) {
                SpecialCases.calculateCycleGraph();

                return true;
            } else if (chosen.equals(GraphType.WHEEL_GRAPH)) {
                SpecialCases.calculateWheelGraph();

                return true;
            } else if (chosen.equals(GraphType.TREE_GRAPH)) {
                SpecialCases.calculateTreeGraph();

                return true;
            }
        }

        return false;
    }

}
