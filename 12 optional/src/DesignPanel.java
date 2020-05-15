import javax.swing.*;
import java.awt.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

public class DesignPanel extends JPanel {
    MainFrame frame;
    final static int W = 1000, H = 500;
    ArrayList<JComponent> components = new ArrayList<>();

    public DesignPanel(MainFrame frame) {
        this.frame = frame;
        createOffscreenImage();
    }

    public void addAtRandomLocation(JComponent comp) {
        int x = (int) ((W - 1) * Math.random());
        int y = (int) ((H - 1) * Math.random());
        int w = comp.getPreferredSize().width;
        int h = comp.getPreferredSize().height;
        comp.setBounds(x, y, w, h);
        comp.setToolTipText(comp.getClass().getName());
        comp.setVisible(true);
        add(comp);
        components.add(comp);
        frame.repaint();
    }

    private void createOffscreenImage() {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new FlowLayout());
    }

    public void loadFromXML(File filePath) {
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)));
            while(true) {
                Object c = decoder.readObject();
                if(c != null)
                    addAtRandomLocation((JComponent) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void SaveToXML(File filePath) throws FileNotFoundException {
        XMLEncoder xmlencoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath)));

        for (JComponent component : components)
            xmlencoder.writeObject(component);

        xmlencoder.close();
    }
}