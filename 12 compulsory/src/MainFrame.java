import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    ControlPanel controlPanel;
    DesignPanel designPanel;

    public MainFrame() {
        super("My Drawing Application");
        init();
        this.pack();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //create the components
        designPanel = new DesignPanel(this);
        controlPanel = new ControlPanel(this);

        //arrange the components in the container (frame)
        //JFrame uses a BorderLayout by default
        add(designPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
       new MainFrame().setVisible(true);
    }
}