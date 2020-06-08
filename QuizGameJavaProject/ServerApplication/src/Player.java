import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    private String name;
    private int points;
    private int gamesWon;
    private Status status = Status.IsLogging;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    enum Status {
        IsLogging,
        IsPlaying,
        HasFinishedGame
    };

    public int getPoints() {
        return points;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
}
