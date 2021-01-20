package graph.algorithms;

import graph.Configuration;
import graph.Main;
import graph.enums.Algorithm;
import graph.exceptions.TimelimitExceededException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Random;

/**
 * https://github.com/soroushj/graph-coloring-genetic-algorithm
 */
public class GeneticAlgorithm {
    private final int maxGenerations;
    private final int populationSize;
    // fitness threshold for choosing a parent selection and mutation algorithm
    private final int fitnessThreshold;
    private Random rand;
    private int colors;
    public static Instant start ;

    public GeneticAlgorithm() throws TimelimitExceededException
    {
        this( 20000, 50, 4);
    }

    public GeneticAlgorithm(int maxGenerations, int populationSize, int fitnessThreshold) throws TimelimitExceededException
    {
        start = Instant.now();

        if ( maxGenerations < 1 || populationSize < 2) {
            throw new IllegalArgumentException();
        }
        this.maxGenerations = maxGenerations;
        this.populationSize = populationSize;
        this.fitnessThreshold = fitnessThreshold;
        this.rand = new Random();
       /* if ( Main.factory.getGraphRepository().getLowerBound()!= null)
            getSolution(Main.factory.getGraphRepository().getLowerBound());
        else */

        findSolution(Main.factory.getGraphRepository().getUpperBound());
    }
    public void findSolution(int upper) throws TimelimitExceededException {
        this.colors = upper;
        if (Configuration.VERBOSE)
            System.out.println("Genetic Algorithm :  ");
        for (int i = upper; i>=Main.factory.getGraphRepository().getLowerBound(); )
        {
            this.colors = getSolution(i);


            double end = Duration.between(start,Instant.now()).toNanos();


            if (Configuration.VERBOSE)
            {
                System.out.print("found a solution "+ this.colors);
                System.out.println("  "+end);
            }
            if (Configuration.TRAINING_MODE_ENABLED)
            {
                if (  Main.factory.getGraphRepository().getBest_chromatic() == null || this.colors < Main.factory.getGraphRepository().getBest_chromatic())
                {
                    Main.factory.getGraphRepository().setBest_chromatic(this.colors);
                    Main.factory.getGraphRepository().setBest_time(end);
                    Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.BACKTRACKING);
                }
                if (Main.factory.getGraphRepository().getBest_chromatic() == null || this.colors == Main.factory.getGraphRepository().getBest_chromatic())
                    if (Main.factory.getGraphRepository().getBest_time() == null || end < Main.factory.getGraphRepository().getBest_time()  )
                    {
                        Main.factory.getGraphRepository().setBest_time(end);
                        Main.factory.getGraphRepository().setBestAlgorithm(Algorithm.GA);
                    }
            }
            else if (Main.factory.getGraphRepository().getChosenAlgorithm().equals(Algorithm.GA))
            {
                Main.factory.getGraphRepository().setChromaticNumber(this.colors);
            }
            if (Main.factory.getGraphRepository().getWatch().isExceeded())
            {
                throw new TimelimitExceededException(this.colors);
            }
            i--;
        }

    }

    public int getSolution(int colors) throws TimelimitExceededException
    {
        this.colors = colors;
        Population population = new Population();
        while (population.bestFitness() != 0 && population.generation() < maxGenerations)
        {
            if (Main.factory.getGraphRepository().getWatch().isExceeded())
            {
                throw new TimelimitExceededException(this.colors);
            }
            population.nextGeneration();
        }
        if (population.bestFitness() == 0 &&  isValidColoring(population.bestIndividual(), population.bestFitness()))
        {
            return  this.colors;
        }
        if (isValidColoring(population.bestIndividual(), population.bestFitness()))
        {
            return this.colors;
        }
        else
        {
            return getSolution(colors);
        }
    }
    private boolean isValidColoring(int [] coloring, int fitness )
    {
        if (fitness!= 0)
            return false;
        for (int i = 0; i< Main.factory.getGraphRepository().getVertexCount(); i++)
        {
            for (int k = 0; k< Main.factory.getGraphRepository().getVertexCount(); k++)
            {
                if (i!=k && Main.factory.getGraphRepository().getAdjacentMatrix()[i][k]==1)
                {
                    if (coloring[i] == coloring[k])
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private class Population {
        private Individual[] population;
        private int generation = 0;

        public Population() {
            population = new Individual[populationSize];
            for (int i = 0; i < populationSize; i++) {
                population[i] = new Individual();
            }
            sort();
        }

        public void nextGeneration() {
            int halfSize = populationSize / 2;
            Individual children[] = new Individual[halfSize];
            for (int i = 0; i < halfSize; i++)
            {
                Parents parents = selectParents();
                Individual child = new Individual(parents);
                child.mutate();
                children[i] = child;
            }
            for (int i = 0; i < halfSize; i++) {
                population[populationSize - i - 1] = children[i];
            }
            sort();
            generation++;
        }

        public int[] bestIndividual() {
            return population[0].chromosome;
        }

        public int bestFitness()
        {
            return population[0].getFitness();
        }

        public int generation() {
            return generation;
        }

        private Parents selectParents()
        {
            return bestFitness() > fitnessThreshold ? selectParents1() : selectParents2();
        }

        private Parents selectParents1()
        {
            Individual tempParent1, tempParent2, parent1, parent2;
            tempParent1 = population[rand.nextInt(populationSize)];
            do {
                tempParent2 = population[rand.nextInt(populationSize)];
            } while (tempParent1 == tempParent2);
            parent1 = (tempParent1.getFitness() > tempParent2.getFitness() ? tempParent2 : tempParent1);
            do {
                tempParent1 = population[rand.nextInt(populationSize)];
                do {
                    tempParent2 = population[rand.nextInt(populationSize)];
                } while (tempParent1 == tempParent2);
                parent2 = (tempParent1.getFitness() > tempParent2.getFitness() ? tempParent2 : tempParent1);
            } while (parent1 == parent2);
            return new Parents(parent1, parent2);
        }

        private Parents selectParents2() {
            return new Parents(population[0], population[1]);
        }

        private void sort()
        {
            int n = populationSize;
            do {
                int newn = 0;
                for (int i = 1; i < n; i++) {
                    if (population[i - 1].getFitness() > population[i].getFitness()) {
                        Individual temp = population[i - 1];
                        population[i - 1] = population[i];
                        population[i] = temp;
                        newn = i;
                    }
                }
                n = newn;
            } while (n != 0);
        }

        private class Individual {
            // each element of chromosome represents a color
            private int[] chromosome;
            // fitness is defined as the number of 'bad' edges, i.e., edges connecting two
            // vertices with the same color
            private int fitness;

            // random individual
            public Individual()
            {
                this.chromosome = new int[Main.factory.getGraphRepository().getVertexCount()];
                for (int i = 0; i < Main.factory.getGraphRepository().getVertexCount(); i++) {
                    this.chromosome[i] = rand.nextInt(colors);
                }
                updateFitness();
            }

            // crossover
            public Individual(Parents parents)
            {
                this.chromosome = new int[Main.factory.getGraphRepository().getVertexCount()];
                int crosspoint = rand.nextInt(Main.factory.getGraphRepository().getVertexCount());
                int c;
                for (c = 0; c <= crosspoint; c++) {
                    this.chromosome[c] = parents.parent1.chromosome[c];
                }
                for (; c < Main.factory.getGraphRepository().getVertexCount(); c++) {
                    this.chromosome[c] = parents.parent2.chromosome[c];
                }
                updateFitness();
            }

            public void mutate()
            {
                if (bestFitness() > fitnessThreshold)
                {
                    mutate1();
                }
                else
                {
                    mutate2();
                }
            }

            private void mutate1()
            {
                for (int v = 0; v < Main.factory.getGraphRepository().getVertexCount(); v++)
                {
                    for (int w = 0; w< Main.factory.getGraphRepository().getVertexCount(); w++)
                    {
                        if (v!=w && Main.factory.getGraphRepository().getAdjacentMatrix()[v][w] == 1)
                        {
                            if (this.chromosome[v] == this.chromosome[w])
                            {
                                HashSet<Integer> validColors = new HashSet<>();
                                for (int c = 0; c < colors; c++)
                                {
                                    validColors.add(c);
                                }
                                for (int u = 0; u< Main.factory.getGraphRepository().getVertexCount(); u++)
                                {
                                    {
                                        if (u!=v && Main.factory.getGraphRepository().getAdjacentMatrix()[u][v]==1)
                                        {
                                            validColors.remove(this.chromosome[u]);
                                        }
                                    }
                                }
                                if (validColors.size() > 0)
                                {
                                    this.chromosome[v] = (int) validColors.toArray()[rand.nextInt(validColors.size())];
                                }
                                break;
                            }
                        }

                    }
                }
                updateFitness();
            }

            private void mutate2()
            {
                for (int v = 0; v < Main.factory.getGraphRepository().getVertexCount(); v++) {
                    for (int w = 0 ; w< Main.factory.getGraphRepository().getVertexCount(); w++)
                    {
                        if (w!= v && Main.factory.getGraphRepository().getAdjacentMatrix()[v][w] == 1)
                        {
                            if (this.chromosome[v] == this.chromosome[w])
                            {
                                this.chromosome[v] = rand.nextInt(colors);
                                break;
                            }
                        }
                    }
                }
                updateFitness();
            }

            private void updateFitness() {
                int f = 0;
                for (int v = 0; v < Main.factory.getGraphRepository().getVertexCount(); v++)
                {
                    for (int w = 0; w< Main.factory.getGraphRepository().getVertexCount(); w++)
                    {
                        if (w!= v && Main.factory.getGraphRepository().getAdjacentMatrix()[v][w] == 1)
                        {
                            if (this.chromosome[v] == this.chromosome[w])
                            {
                                f++;
                            }
                        }
                    }
                }
                this.fitness = f / 2;
            }
            public int getFitness()
            {
                return this.fitness;
            }
        }

        private class Parents
        {
            public final Individual parent1;
            public final Individual parent2;

            public Parents(Individual parent1, Individual parent2) {
                this.parent1 = parent1;
                this.parent2 = parent2;
            }
        }
    }

}
