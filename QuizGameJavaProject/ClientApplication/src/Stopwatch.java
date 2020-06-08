import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {

    private static int counter = 0;
    private Timer timer;
    final int TIME_TO_ANSWER = 10;

    public Stopwatch() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
            }
        };

        //create a new Timer
        timer = new Timer("MyTimer");
        //this line starts the timer at the same time its executed
        timer.scheduleAtFixedRate(timerTask, 30, 1000);
    }

    public int getCounter() {
        return counter;
    }

    public void closeStopwatch() {
        timer.cancel();
    }
}