import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ControlPanel extends JPanel implements ActionListener {
    final MainFrame frame;
    JButton saveBtn = new JButton("Save to XML File");
    JButton loadBtn = new JButton("Load from XML File");
    JButton exitBtn = new JButton("Exit");
    JButton addComponent = new JButton("Add component");

    static final String[] component = {"javax.swing.JButton", "javax.swing.JTextField", "javax.swing.JPanel", "javax.swing.JLabel", "javax.swing.JCheckBox"};
    JComboBox componentText;
    JTextField textField;

    JTextArea log = new JTextArea(5, 10);
    JFileChooser fc = new JFileChooser();
    JScrollPane logScrollPane = new JScrollPane(log);

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        componentText = new JComboBox(component);
        textField = new JTextField();

        add(componentText);
        add(addComponent);
        add(saveBtn);
        add(loadBtn);
        add(exitBtn);
        add(logScrollPane, BorderLayout.CENTER);
        saveBtn.addActionListener(this);
        loadBtn.addActionListener(this);
        exitBtn.addActionListener(this);
        addComponent.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        fc.setFileFilter(new FileFilter() {

            public String getDescription() {
                return "XML File (*.xml)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".xml");
                }
            }
        });

        if (actionEvent.getSource() == exitBtn)
            System.exit(0);
        else if (actionEvent.getSource() == saveBtn)
        {
            int returnVal = fc.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File filePath = new File(fc.getSelectedFile() + ".xml");

                try {
                    frame.designPanel.SaveToXML(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                log.append("Saving: " + filePath.getName() + "\n");
            }
            else {
                log.append("Save command cancelled by user.\n");
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
        else if(actionEvent.getSource() == loadBtn)
        {
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File filePath = new File(String.valueOf(fc.getSelectedFile()));
                frame.designPanel.loadFromXML(filePath);

                log.append("Loading: " + filePath.getName() + "\n");
            }
            else {
                log.append("Load command cancelled by user.\n");
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
        else if(actionEvent.getSource() == addComponent)
        {
            String selected = Objects.requireNonNull(componentText.getSelectedItem()).toString();
            textField.setText(selected);

            String textComp = textField.getText();
            try {
                Class clazz = Class.forName(textComp);
                JComponent jComponent = (JComponent) clazz.getConstructor().newInstance();
                frame.designPanel.addAtRandomLocation(jComponent);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
            log.append("Component added.\n");
        }
    }
}