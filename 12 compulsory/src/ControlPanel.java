import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ControlPanel extends JPanel implements ActionListener {
    final MainFrame frame;
    JButton exitBtn = new JButton("Exit");
    JButton addComponent = new JButton("Add component");

    static final String[] component = {"javax.swing.JButton", "javax.swing.JTextField", "javax.swing.JPanel", "javax.swing.JLabel", "javax.swing.JCheckBox"};
    JComboBox componentText;
    JTextField textField;

    JTextArea log = new JTextArea(5, 10);
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
        add(exitBtn);
        add(logScrollPane, BorderLayout.CENTER);
        exitBtn.addActionListener(this);
        addComponent.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == exitBtn)
            System.exit(0);
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