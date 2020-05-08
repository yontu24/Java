import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private GameServer server;
    private final Game game;
    private PrintWriter out;
    private int players = 0;

    public ClientThread(Socket socket, GameServer serverSocket, Game game) {
        this.socket = socket;
        this.server = serverSocket;
        this.game = game;
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        String request;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Welcome!!");
            out.flush();
            boolean winner = false;

            while (true) {
                request = in.readLine();
                if (request == null)
                    continue;
                if (request.contains("stop")) {
                    server.stopServer();
                    break;
                } else {    // create or join or invalid cmd
                    boolean isValidCmd = false;
                    players++;
                    Player player = new Player();

                    while (!isValidCmd) {
                        switch (request) {
                            case "create": {
                                if (game.player1 == null) {
                                    game.player1 = player;
                                    game.player1.setTurn(true);
                                    server.addFormat("C", "Create game", true);
                                    out.println("U have just create a new game! Wait another player.");
                                    out.flush();
                                    isValidCmd = true;
                                } else {
                                    out.println("Game is running. Write join");
                                    out.flush();
                                }
                                break;
                            }
                            case "join": {
                                if (game.player1 == null) {
                                    out.println("There is no game. Firstly, create one!");
                                    out.flush();
                                } else {
                                    game.player2 = player;
                                    game.player2.setTurn(false);
                                    out.println("U are the second player!");
                                    server.addFormat("C", "Join game", true);
                                    out.flush();
                                    isValidCmd = true;
                                }
                                break;
                            }
                            default: {
                                out.println("Available commands: create, join, exit");
                                out.flush();
                            }
                        }
                        if (!isValidCmd) request = in.readLine();
                    }

                    while (!winner) {
                        request = in.readLine();

                        // wait another player to join
                        if (game.player2 == null) {
                            out.println("Wait ur opponent...");
                            out.flush();
                        } else {
                            if (request.contains("move")) {
                                switch (game.addPiece(player, request)) {
                                    case -1: {
                                        out.println("Invalide location.");
                                        server.addFormat("C", "Invalide locations", true);
                                        out.flush();
                                        break;
                                    }
                                    case 0: {
                                        out.println("Already filled. Try again.");
                                        server.addFormat("C", "Already filled", true);
                                        out.flush();
                                        break;
                                    }
                                    case 1: {
                                        if(player.getTurn() == game.player1.getTurn()) {
                                            out.println("WHITE: Added value to location " + request.substring(5));
                                            out.flush();
                                            server.addFormat("AW", request.substring(5), true);
                                        } else {
                                            out.println("BLACK: Added value to location " + request.substring(5));
                                            out.flush();
                                            server.addFormat("AB", request.substring(5), true);
                                        }

                                        break;
                                    }
                                    case 2: {
                                        out.println("Not ur turn.");
                                        out.flush();
                                    }
                                    case 3: {
                                        winner = true;
                                        out.println((player.getTurn() == game.player1.getTurn() ? "WHITE" : "BLACK") + " WON !!!");
                                        server.addFormat("C", (player.getTurn() == game.player1.getTurn() ? "WHITE" : "BLACK"), true);
                                        out.flush();
                                    }
                                }
                            } else if (request.contains("display")) {
                                out.println(game.getBoard().displayBoardToPlayer());
                            } else {
                                out.println("Available commands: move x y, display");
                                out.flush();
                            }
                        }
                    }

                    try (PrintWriter out = new PrintWriter("sgf.txt")) {
                        out.println(server.getFormat().getFormat());
                        System.out.println("Status of the game saved in local directory.");
                    }

                    try{
                        server.getFormat().uploadFile();
                    } catch (JSchException | SftpException e) {
                        e.printStackTrace();
                    }

                    // daca s-a ajuns aici, jocul intre cei doi jucatori s-a terminat
                    server.stopServer();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}