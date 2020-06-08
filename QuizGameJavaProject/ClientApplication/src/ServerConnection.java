import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

public class ServerConnection implements Runnable {
    private BufferedReader in;
    private boolean stopped = false;
    private ControlPanel panel;

    public ServerConnection(ControlPanel panel) {
        this.in = GameClient.getIn();
        this.panel = panel;
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                String serverResponse = in.readLine();
                if (serverResponse == null) break;

                switch (serverResponse) {
                    case "LEFT_GAME": {
                        panel.displayMessage("You have left the game. Press any key...");
                        stopped = true;
                        break;
                    }
                    case "IS_NAME_VALID": {
                        panel.displayMessage("Your name is valid.");
                        break;
                    }
                    case "START_GAME": {
                        panel.displayMessage("Press 'Answer' to start the game!");
                    }
                    default: {
                        if (serverResponse.contains("FINISH:")) {
                            panel.questionOutput.setText("\tYour final score is " + serverResponse.substring(7) + " points.");

                            for (JRadioButton button : panel.buttons)
                                button.setVisible(false);
                        } else {
                            String[] token = serverResponse.split("@");

                            panel.questionOutput.setText("\tQuestion no. " + token[0] + ": " + token[1]);

                            for (JRadioButton button : panel.buttons)
                                button.setVisible(false);

                            for (int i = 0; i < token.length - 2; i++) {
                                panel.buttons.get(i).setVisible(true);
                                panel.buttons.get(i).setText((char) ('a' + i) + ". " + token[i + 2]);
                            }

                            //panel.displayMessage(serverResponse);
                        }
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