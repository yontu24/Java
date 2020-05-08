import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.net.ServerSocket;

public class GameServer
{
    private static final int PORT = 8100;
    private volatile boolean stop = false;
    private static final int second = 1000;
    private int numberOfPlayers = 0;
    private SGF format = new SGF();

    public void addFormat(String rule, String comment, boolean addNewLine) {
        format.addFormat(rule, comment, addNewLine);
    }

    public SGF getFormat() {
        return format;
    }

    public GameServer() throws JSchException {
        System.out.println("Server is running...");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(10*second);
            format.addFormat("GN", "Gomoku (Five in a row)", true);
            format.addFormat("KM", "5 v 5", true);
            format.addFormat("DT", java.time.LocalDate.now().toString(), true);
            format.addFormat("TM", "No time limit", true);
            format.addFormat("RU", "Japanese", true);
            format.addFormat("PW", "White", true);
            format.addFormat("PB", "Black", true);

            Board board = new Board();
            Game game = new Game(board);

            System.out.println(format.getFormat());

            while (!stop) {
                /*
                Player p = new Player(this, serverSocket.accept(), game, "WHITE");
                p.start();
                game.addPlayer(p);
                System.out.println(p.getSocket() + " has joined.");
                */

                ClientThread client = new ClientThread(serverSocket.accept(), this, game);
                client.start();
                System.out.println(client.getSocket() + " has joined (" + ++numberOfPlayers + ")");
            }
            System.out.println("Server stopped by setting stop true");
            serverSocket.close();
        }
        catch (IOException e) {
            System.err.println("Oooops... " + e);
        }
    }

    public void stopServer() {
        stop = true;
    }

    public static void main(String[] args) throws JSchException {
        GameServer server = new GameServer();
    }
}
