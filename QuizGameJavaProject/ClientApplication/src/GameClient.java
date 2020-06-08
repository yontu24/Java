import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private static BufferedReader in;
    private static PrintWriter out;
    static Socket socket = null;
    static String SERVER_ADDRESS = "127.0.0.1";
    static final int PORT = 8000;
    static String userName;

    enum PlayerState {
        TYPING_NAME,
        WAITING_FOR_PLAYERS,
        DIFFICULTY_EASY,
        DIFFICULTY_HARD
    };

    public static PrintWriter getOut() {
        return out;
    }

    public static BufferedReader getIn() {
        return in;
    }

    public GameClient() throws IOException {
        if (socket == null)
            socket = new Socket(SERVER_ADDRESS, PORT);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) throws IOException {

        GameClient client = new GameClient();
        new MainFrame().setVisible(true);
    }

    public static void sendMessageToServer(String message, boolean encode) {
        if (encode) {
            getOut().println(encodingMessage(message));
            return;
        }
        getOut().println(message);
    }

    public static String encodingMessage(String message) {
        return (ControlPanel.getPlayerState() + ":" + message);
    }

    public static String getServerInput() throws IOException {
        return getIn().readLine();
    }
}