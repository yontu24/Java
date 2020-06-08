import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<>();
    private List<ClientThread> clientThreadList = new ArrayList<>();
    private QuizHandler quizHandler = new QuizHandler();

    public int totalQuizzes() {
        return quizHandler.getQuizzes().size();
    }

    public boolean isRightAnswer(int quizID, int difficulty, String ra) {
        return quizHandler.isRightAnswer(quizID, difficulty, ra);
    }

    public String getQuiz(int quizID) {
        return quizHandler.packUpQuiz(quizID);
    }

    public void addClient(ClientThread client) {
        clientThreadList.add(client);
    }

    public List<ClientThread> getClientThreadList() {
        return clientThreadList;
    }

    public void removeClient(ClientThread client) {
        clientThreadList.stream().filter(c -> c == client).findFirst().ifPresent(cl -> clientThreadList.remove(cl));
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean isPlayerAlreadyConnectedBy(String name) {
        return players.stream().anyMatch(p -> p.getName().equals(name));
    }

    public void broadcast(String message, ClientThread excluded) {
        for (ClientThread client : clientThreadList) {
            if (excluded == client || client.getPlayer().getStatus() == Player.Status.IsLogging) {
                continue;
            }
            client.sendMessage(message);
        }
    }

    public void removePlayer(Player player)  {
        players.stream().filter(p -> p == player).findFirst().ifPresent(player1 -> players.remove(player1));
    }
}
