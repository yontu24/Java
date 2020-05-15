import javax.swing.*;
import java.awt.*;

public class DesignPanel extends JPanel
{
    final MainFrame frame;
    final static int W = 1000, H = 500;

    public DesignPanel(MainFrame frame)
    {
        this.frame = frame;
        createOffscreenImage();
    }

    public void addAtRandomLocation(JComponent comp) {
        int x = (int)((W - 1) * Math.random());
        int y = (int)((H - 1) * Math.random());
        int w = comp.getPreferredSize().width;
        int h = comp.getPreferredSize().height;
        comp.setBounds(x, y, w, h);
        comp.setToolTipText(comp.getClass().getName());
        comp.setVisible(true);
        add(comp);
        frame.repaint();
    }

    private void createOffscreenImage()
    {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new FlowLayout());
    }
}