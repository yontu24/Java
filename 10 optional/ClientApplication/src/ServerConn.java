import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConn implements Runnable {
    private BufferedReader in;
    private boolean stopped = false;

    public ServerConn(Socket server) throws IOException {
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

                if (serverResponse.contains("stopped")) {
                    System.out.println("Server stopped. Press enter...");
                    stopped = true;
                } else {
                    System.out.println(serverResponse);
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