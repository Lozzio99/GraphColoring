package graph.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class StopWatch {
    public static final long TIMELIMIT = 12000;
    public static final long TRAINING_TIME = 5 * TIMELIMIT;   //5 algorithms tested
    public static final long LOWER_BOUND_TIMELIMIT = 10000;
    public static final long UPPER_BOUND_TIMELIMIT = 10000;
    public static final long IMPORT_TIMELIMIT = 30000;
    public static final long CHROMATIC_NUMBER_TIMELIMIT = 70000;

    private Instant start;
    private Instant stop;
    private Instant deadline;
    private Instant tempDeadline;

    public void start(Long plusDeadline) {
        if (plusDeadline == null)
            return;
        this.start = Instant.now();

        if (!(plusDeadline == null)) {
            this.deadline = this.start.plus(plusDeadline, ChronoUnit.MILLIS);
        }
    }

    public void setIntermediateDeadline(Long plusDeadline) {
        if (!(plusDeadline == null)) {
            this.tempDeadline = Instant.now().plus(plusDeadline, ChronoUnit.MILLIS);
        }
    }

    public void terminiateIntermediateDeadline() {
        this.tempDeadline = null;
    }

    public boolean isExceeded() {
        if (this.deadline == null) {
            return false;
        }

        if (Duration.between(Instant.now(), deadline).isNegative()) {
            this.stop();
            System.out.println("Exceeded");
            return true;
        }

        if (this.tempDeadline != null) {
            if (Duration.between(Instant.now(), tempDeadline).isNegative()) {
                this.tempDeadline = null;
                System.out.println("Temporary Exceeded");
                return true;
            }
        }

        return false;
    }
    public void stop() {
        this.stop = Instant.now();
    }

    public Duration getTime() {
        if (start == null || stop == null) {
            return null;
        }

        return Duration.between(start, stop);
    }

    public long getTimeInMillis() {
        return getTime().toMillis();
    }

    public long getTimeInSeconds() {
        if (getTime() == null) {
            return -1;
        }

        return getTime().toSeconds();
    }

    public long getTimeInMinutes() {
        return getTime().toMinutes();
    }

    public long getTimeInNanoSeconds() {
        return getTime().toNanos();
    }
}
