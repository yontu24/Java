import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    public static void main(String[] args) throws IOException {
        String SERVERADDRESS = "127.0.0.1";
        final int PORT = 8100;
        Socket socket = new Socket(SERVERADDRESS, PORT);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //ServerConn sc = new ServerConn(socket);
        //new Thread(sc).start();

        while (true) {
            String serverResponse = in.readLine();
            System.out.println(serverResponse);

            System.out.print("> ");
            String request = keyboard.readLine();

            if (request.contains("exit"))// || sc.isStopped())
                break;

            out.println(request);
        }
        socket.close();
    }
}
