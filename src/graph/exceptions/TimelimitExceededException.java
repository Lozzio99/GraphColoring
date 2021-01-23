package graph.exceptions;


import graph.Main;

import java.sql.Time;

public class TimelimitExceededException extends Exception
{
    private int value;

    public TimelimitExceededException() {
        super("Timelimit Exceeded");
    }

    public TimelimitExceededException(int value) {
        super("Timelimit Exceeded");
        this.value = value;
    }

    public TimelimitExceededException(String message, int value)
    {
        super(message);
        this.value = value;
    }
    public TimelimitExceededException(int value, double timing)
    {
        this(value);
        if (Main.factory.getGraphRepository().getBest_time()== null || timing< Main.factory.getGraphRepository().getBest_time())
            Main.factory.getGraphRepository().setBest_time(timing);
    }
    public TimelimitExceededException(String message) {
        super(message);
    }

    public int getValue() {
        return value;
    }
}
