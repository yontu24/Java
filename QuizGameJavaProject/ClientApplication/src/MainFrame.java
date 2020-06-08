import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    ControlPanel controlPanel;
    ConsoleOut console;

    public MainFrame() {
        super("QUIZ GAME v1.0");
        init();
        this.pack();
    }

    private void init() {
        console = new ConsoleOut(this);
        controlPanel = new ControlPanel(this);
        controlPanel.setPreferredSize(new Dimension(20, 200));

        add(controlPanel, BorderLayout.NORTH);
        add(console, BorderLayout.CENTER);
    }
}