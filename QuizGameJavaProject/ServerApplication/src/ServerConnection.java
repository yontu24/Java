import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private BufferedReader in;
    private boolean stopped = false;

    public ServerConnection(Socket server) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }

    public boolean isStopped() {
        return stopped;
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                String serverResponse = in.readLine();
                if (serverResponse == null) break;

                switch (serverResponse) {
                    case "LEFT_GAME": {
                        System.out.println("You have left the game. Press any key...");
                        stopped = true;
                        break;
                    }
                    case "IS_NAME_VALID": {
                        System.out.println("Your name is valid.");
                        break;
                    }
                    default: {
                        System.out.println(serverResponse);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}