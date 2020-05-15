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

    private void drawShape(int x, int y)
    {
        /*
        // int radius = (int)(Math.random() * 100); //generate a random number
        int radius = frame.configPanel.getShapeSize();
        int sides = frame.configPanel.sidesNumber();

        String colorString = frame.configPanel.getShapeColor();
        Color color;
        if(colorString.equals("Black"))
        {
            color = new Color(0, 0, 0);
        }
        else
        {
            int red = (int)(Math.random()*256);
            int green = (int)(Math.random()*256);
            int blue = (int)(Math.random()*256);
            color = new Color(red, green, blue);
        }

        String shapeString = frame.configPanel.getShape();
        Shape shape;
        if(shapeString.equals("Regular Polygon"))
            shape = new RegularPolygon(x, y, radius, sides);
        else
            */




        /*
        Shape shape = new RegularPolygon(x, y, 100, 4);
        Color color;
        int red = (int)(Math.random()*256);
        int green = (int)(Math.random()*256);
        int blue = (int)(Math.random()*256);
        color = new Color(red, green, blue);
        shapes.add(shape);

        graphics.setColor(color);
        graphics.fill(shape);
         */
    }
}