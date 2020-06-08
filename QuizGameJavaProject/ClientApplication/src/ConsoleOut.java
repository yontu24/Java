import javax.swing.*;
import java.awt.*;

public class ConsoleOut extends JPanel
{
    final MainFrame frame;
    final static int W = 500, H = 300;
    JTextArea displayConsoleOutput = new JTextArea(15, 40);
    JScrollPane logScrollPane = new JScrollPane(displayConsoleOutput);

    public ConsoleOut(MainFrame frame) {
        this.frame = frame;
        createConsole();
    }

    private void createConsole() {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());

        displayConsoleOutput.setEditable(false);
        displayConsoleOutput.setBackground(Color.GRAY);
        logScrollPane = new JScrollPane(displayConsoleOutput);

        add(logScrollPane);
    }
}