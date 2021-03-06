package graph;

import graph.enums.Algorithm;
import graph.enums.LowerBound;
import graph.enums.UpperBound;
import graph.utils.StopWatch;

public class Configuration
{
    public static Integer TRAINING_INSTANCES = 1; //number of graphs processed
    public static Algorithm DEFAULT_ALGORITHM = Algorithm.BACKTRACKING;
    public static UpperBound DEFAULT_UPPER_BOUND = UpperBound.WELSH_POWELL;
    public static LowerBound DEFAULT_LOWER_BOUND = LowerBound.TOMITA;
    public static Long DEFAULT_TIMELIMIT = StopWatch.TIMELIMIT;
    public static Long TRAINING_TIMELIMIT = 5 * StopWatch.TRAINING_TIME * TRAINING_INSTANCES;
    public static Long DEFAULT_UPPER_BOUND_TIMELIMLT = StopWatch.UPPER_BOUND_TIMELIMIT;
    public static Long DEFAULT_LOWER_BOUND_TIMELIMIT = StopWatch.LOWER_BOUND_TIMELIMIT;
    public static Long DEFAULT_CHROMATIC_NUMBER_TIMELIMILT = StopWatch.CHROMATIC_NUMBER_TIMELIMIT;
    private final static boolean ENABLE_DEBUG = false;
    public static boolean VERBOSE = false;
    public final static boolean TRAINING_MODE_ENABLED = false;
    public final static String MODEL_PATH = System.getProperty("user.dir") + "/models";
    public final static boolean MLP_RESET = false;
    public final static boolean FEATURE_RESET = false;
    public final static Integer NUMBER_OF_FEATURES = 9;
    public final static Integer NUMBER_OF_HIDDEN =512;
    public final static Integer NUMBER_OF_OUTPUTS = 5 ;
    public final static Double LEARNING_CONSTANT = 0.005;
    public static boolean isDebugging() {
        return ENABLE_DEBUG;
    }
}
