import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private GameServer server;
    private Game game;
    private Player player = new Player("unnamed");
    private PrintWriter out;
    private BufferedReader in;

    public ClientThread(Socket socket, GameServer serverSocket, Game game) {
        this.socket = socket;
        this.server = serverSocket;
        this.game = game;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String request;
            do {
                request = in.readLine();
                if (request.contains("CHECK:")) {
                    String name = request.substring(6);
                    if (game.isPlayerAlreadyConnectedBy(name)) {
                        sendMessage("ALREADY_EXISTS");
                    } else {
                        player = new Player(name);
                        player.updateStatus(Player.Status.IsPlaying);
                        game.addPlayer(player);
                        sendMessage("IS_READY");
                    }
                } else if (request.contains("STOP")) {
                    server.stopServer();
                    sendMessage("Server has stopped after performing stop cmd.");
                    break;
                } else if (request.equals("COUNT_CONNECTED")) {
                    int totalConnection = game.getClientThreadList().size();
                    if (GameServer.MIN_PLAYERS <= totalConnection) {
                        sendMessage("GET_READY");
                        enterGame();
                        request = "EXIT";
                    } else {
                        sendMessage("PENDING:" + (GameServer.MIN_PLAYERS - totalConnection));
                        System.out.println("pending:" + (GameServer.MIN_PLAYERS - totalConnection));
                    }
                } else {
                    sendMessage("Server says: Wrong request...");
                }
            } while (!request.equals("EXIT"));

            sendMessage("FINISH:" + player.getPoints());

            game.broadcast("Player " + player.getName() + " has quit.", this);
            game.removeClient(this);
            game.removePlayer(player);
            out.close();
            System.out.println("Player " + player.getName() + " has quit.");

        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void enterGame() throws IOException {
        /*
        String request = in.readLine();
        sendMessage("START_GAME");
        */

        String request;
        int quizID;
        for (quizID = 0; quizID < game.totalQuizzes(); quizID++) {
            request = in.readLine();
            System.out.println("Am primit: " + request);
            if (request.contains("DIFFICULTY_EASY:")) {
                if (game.isRightAnswer(quizID, 1, request.substring(16))) {
                    player.setPoints(player.getPoints() + 1);
                }
            } else if (request.contains("DIFFICULTY_HARD:")) {
                if (game.isRightAnswer(quizID, 2, request.substring(16))) {
                    player.setPoints(player.getPoints() + 2);
                }
            }

            game.broadcast(game.getQuiz(quizID), null);
        }
    }

    public void sendMessage(String message) {
        getOut().println(message);
        getOut().flush();
    }

    public Player getPlayer() {
        return player;
    }
}