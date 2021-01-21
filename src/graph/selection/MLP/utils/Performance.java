package graph.selection.MLP.utils;

import graph.enums.Algorithm;

public class Performance
{
    public Double TIME_SCORE;
    public Integer CHROMATIC_NUMBER;
    public Algorithm BEST_ALGORITHM;

    public Performance (Double time_score,Integer chromatic_number, Algorithm algorithm)
    {
        this.TIME_SCORE = time_score;
        this.CHROMATIC_NUMBER = chromatic_number;
        this.BEST_ALGORITHM = algorithm;
    }
    public Double getTimeScore()
    {
        return this.TIME_SCORE;
    }
    public Integer getChromaticNumber ()
    {
        return this.CHROMATIC_NUMBER;
    }
    public Algorithm algorithm()
    {
        return this.BEST_ALGORITHM;
    }
}
