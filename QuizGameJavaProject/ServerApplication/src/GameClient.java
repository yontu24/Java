import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private static BufferedReader in;
    private static BufferedReader keyboard;
    private static PrintWriter out;
    static String userName;

    public static void main(String[] args) throws IOException, InterruptedException {
        String SERVER_ADDRESS = "127.0.0.1";
        final int PORT = 8000;
        Socket socket = new Socket(SERVER_ADDRESS, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        keyboard = new BufferedReader(new InputStreamReader(System.in));

        setPlayerName();

        // thread care se ocupa cu afisarea mesajelor trimise de catre server in client
        ServerConnection sc = new ServerConnection(socket);
        new Thread(sc).start();

        while (true) {
            System.out.print("> ");
            String request = keyboard.readLine();

            if (request.contains("exit") || sc.isStopped())
                break;

            out.println(request);
        }
        socket.close();
    }

    private static void setPlayerName() throws IOException, InterruptedException {
        final String pattern = "^[\\d\\w_]*$";  // doar (litere|_|cifre)
        System.out.print("Enter your name: ");
        while (true) {
            String input = keyboard.readLine();
            if (!input.matches(pattern) || input.isEmpty()) {
                System.out.println("Your name contains invalid characters. Try another name.");
            } else {
                out.println("CHECK_FOR_NAME:" + input);
                userName = input;
                input = in.readLine();

                if (input.equals("ALREADY_EXISTS")) {
                    System.out.println("Your name is possessed by a connected player. Try another name.");
                }
                else if (input.equals("IS_READY")){
                    System.out.println("Your name is valid. Welcome '" + userName + "'. Press enter: ");
                    break;
                }
            }
        }
    }
}



/*
                } else if (game.isAlreadyIn(name)) {
                    out.println("There is another player who possessed this name. Try another name.");
                    out.flush();
                } else {
                    Player player = new Player(name);
                    player.updatePlayerStatus(Player.Status.IsPlaying);
                    game.addPlayer(player);

                    out.println("You joined the game! Have fun.");
                    out.flush();
                    isValidCmd = true;
                }
 */