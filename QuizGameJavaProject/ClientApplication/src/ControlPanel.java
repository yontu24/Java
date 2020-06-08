import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ControlPanel extends JPanel implements ActionListener {
    final MainFrame frame;
    JButton exitBtn;
    JButton sendMessage;
    JComboBox<String> difficulty;
    JTextField entry;
    JTextArea questionOutput;
    JLabel hint;

    ArrayList<JRadioButton> buttons = new ArrayList<>();

    final String consoleTag = "[Quiz Bot]";
    final String[] difficultyString = {"Choose game difficulty:", "a. Choose Answer", "b. Type full Answer"};
    final String pattern = "^[\\d\\w_]*$";  // doar (litere|_|cifre)

    private static GameClient.PlayerState playerState = GameClient.PlayerState.TYPING_NAME;
    int answerId = 0;

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    GameClient.sendMessageToServer("EXIT", false);
                    System.exit(0);
                }
            }
        });

        questionOutput = new JTextArea(4, 40);
        questionOutput.setVisible(false);
        questionOutput.setEditable(false);
        exitBtn = new JButton("Exit");
        sendMessage = new JButton("Send");
        entry = new JTextField();
        entry.setPreferredSize(new Dimension(100, 20));
        hint = new JLabel("Enter your name:");
        difficulty = new JComboBox<>(difficultyString);

        setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.weightx = 5.0;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridwidth = GridBagConstraints.REMAINDER;
        add(questionOutput, constraint);
        constraint.weightx = 2.0;

        GridBagConstraints center = new GridBagConstraints();
        center.fill = GridBagConstraints.VERTICAL;
        center.gridwidth = GridBagConstraints.REMAINDER;

        ButtonGroup group = new ButtonGroup();
        JRadioButton checkBox;
        for (int i = 1; i <= 5; i++) {
            checkBox = new JRadioButton();
            checkBox.setName("CheckBox" + i);
            checkBox.addActionListener(this);
            buttons.add(checkBox);

            checkBox.setVisible(false);
            add(checkBox, constraint);
            checkBox.addActionListener(this);
            group.add(checkBox);
        }

        // afisam componentele doar pentru introducerea numelui, de aceea nu vom afisa dificultatea
        if (playerState == GameClient.PlayerState.TYPING_NAME)
            difficulty.setVisible(false);

        add(difficulty);
        add(hint, center);
        add(entry, center);
        add(sendMessage, center);
        add(exitBtn, center);
        exitBtn.addActionListener(this);
        sendMessage.addActionListener(this);
        difficulty.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == exitBtn) {
            GameClient.sendMessageToServer("EXIT", false);
            System.exit(0);
        } else if (actionEvent.getSource() == difficulty) {
            String selected = Objects.requireNonNull(difficulty.getSelectedItem()).toString();
            displayMessage("Selected Difficulty: " + selected);

            // easy
            if (selected.equals(difficultyString[1])) {
                playerState = GameClient.PlayerState.DIFFICULTY_EASY;
                difficulty.setVisible(false);
                sendMessage.setVisible(true);
                questionOutput.setVisible(true);
                answerId = 0;
            }
            // hard
            else if (selected.equals(difficultyString[2])) {
                playerState = GameClient.PlayerState.DIFFICULTY_HARD;
                difficulty.setVisible(false);
                entry.setVisible(true);
                hint.setVisible(true);
                sendMessage.setVisible(true);
                questionOutput.setVisible(true);
            }

            for (JRadioButton button : buttons)
                button.setVisible(false);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (actionEvent.getSource() == buttons.get(0)) {
            answerId = 0;
        }
        else if (actionEvent.getSource() == buttons.get(1)) {
            answerId = 1;
        }
        else if (actionEvent.getSource() == buttons.get(2)) {
            answerId = 2;
        }
        else if (actionEvent.getSource() == buttons.get(3)) {
            answerId = 3;
        }
        else if (actionEvent.getSource() == buttons.get(4)) {
            answerId = 4;
        }
        else if (actionEvent.getSource() == sendMessage) {
            String answer = entry.getText();

            switch (playerState) {
                case TYPING_NAME: {
                    if (answer.isEmpty()) {
                        displayMessage("Your name is empty. Type again.");
                        return;
                    } else if (!answer.matches(pattern)) {
                        displayMessage("Your name contains invalid symbols. Type again.");
                        return;
                    } else {
                        GameClient.sendMessageToServer("CHECK:" + answer, false);
                        GameClient.userName = answer;

                        try {
                            answer = GameClient.getServerInput();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (answer.equals("ALREADY_EXISTS")) {
                            displayMessage("Your name is possessed by a connected player. Try another name.");
                        } else if (answer.equals("IS_READY")) {
                            entry.setText("");
                            displayMessage("Welcome " + GameClient.userName + "!");
                            hint.setText("Enter your answer: ");
                            playerState = GameClient.PlayerState.WAITING_FOR_PLAYERS;
                            entry.setVisible(false);
                            hint.setVisible(false);
                            sendMessage.setText("Refresh");
                            difficulty.setVisible(false);
                            questionOutput.setVisible(false);
                        } else {
                            displayMessage("Might be an error here... " + answer);
                        }
                        break;
                    }
                }
                case WAITING_FOR_PLAYERS: {
                    GameClient.sendMessageToServer("COUNT_CONNECTED", false);
                    try {
                        answer = GameClient.getServerInput();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (answer.contains("PENDING:")) {
                        displayMessage("You must wait another " + answer.substring(8) + " players.");
                    }
                    else if (answer.equals("GET_READY")) {
                        displayMessage("The will start soon. Choose your difficulty. Earn points and be the best !!!");
                        displayMessage("All the progress will be saved next time when you will be back.");
                        displayMessage("If you choose easyer way, you will be able to earn only one point, if your answer is correct, otherwise 0.");
                        displayMessage("If you choose harder way, you will be able to earn 2 points, if your answer is correct, otherwise 0.");
                        difficulty.setVisible(true);
                        sendMessage.setText("Answer");

                        // thread care se ocupa cu afisarea mesajelor trimise de catre server in client
                        ServerConnection sc = new ServerConnection(this);
                        new Thread(sc).start();
                    }
                    break;
                }
                case DIFFICULTY_EASY: {
                    if (answerId == -1) {
                        displayMessage("Select an answer, then you can send it to the server.");
                        break;
                    }

                    char answerCharId = (char)(answerId + 65);
                    displayMessage("> YOUR ANSWER: " + answerCharId);
                    buttons.get(answerId).setSelected(false);
                    GameClient.sendMessageToServer(Character.toString(answerCharId), true);
                    answerId = -1;
                    break;
                }
                case DIFFICULTY_HARD: {
                    if (answer.isEmpty()) {
                        displayMessage("Type an answer, then you can send it to the server");
                        break;
                    }

                    displayMessage(answer);
                    GameClient.sendMessageToServer(answer, true);
                    entry.setPreferredSize(new Dimension(100, 20));
                    entry.setText("");
                    break;
                }
            }
        }
    }

    public static GameClient.PlayerState getPlayerState() {
        return playerState;
    }

    public void displayMessage(String message) {
        frame.console.displayConsoleOutput.append(consoleTag + " " + message + "\n");
    }
}