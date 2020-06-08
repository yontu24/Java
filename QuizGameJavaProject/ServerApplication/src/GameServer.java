import java.io.IOException;
import java.net.ServerSocket;

public class GameServer
{
    private static final int PORT = 8000;
    private volatile boolean stop = false;
    private static final int secondUnits = 1000;
    private static final int time_out = 100;    // secunde
    private static ServerSocket serverSocket = null;
    private static int numberOfPlayers = 0;
    private static Game game;
    public static int MIN_PLAYERS = 1;

    public GameServer() {
        System.out.println("Server is running...");
        game = new Game();
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(time_out*secondUnits);
            acceptClients();

            System.out.println("Server stopped by setting stop true");
            serverSocket.close();
        }
        catch (IOException e) {
            System.err.println("Oooops... " + e);
        }
    }

    public void acceptClients() throws IOException {
        while (!stop) {
            ClientThread client = new ClientThread(serverSocket.accept(), this, game);
            client.start();
            game.addClient(client);
            System.out.println(client.getSocket() + " has joined (" + ++numberOfPlayers + ")");
        }
    }

    public void stopServer() {
        stop = true;
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
    }
}
