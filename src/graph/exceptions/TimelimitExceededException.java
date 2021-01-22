package graph.exceptions;


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

    public TimelimitExceededException(String message) {
        super(message);
    }

    public int getValue() {
        return value;
    }
}
