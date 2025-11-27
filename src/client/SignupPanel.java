package client;

import javax.swing.*;
import java.awt.*;

public class SignupPanel extends JPanel {
    public SignupPanel(GUIManager gui, Client client) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel typeLabel = new JLabel("Register As:");
        String[] types = {"Member", "Staff"};
        JComboBox<String> typeSelect = new JComboBox<>(types);

        JTextField nameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton createBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back to Login");

        c.gridy = 0; add(title, c);
        c.gridy = 1; add(typeLabel, c);
        c.gridy = 2; add(typeSelect, c);
        c.gridy = 3; add(new JLabel("Name:"), c);
        c.gridy = 4; add(nameField, c);
        c.gridy = 5; add(new JLabel("Password:"), c);
        c.gridy = 6; add(passwordField, c);
        c.gridy = 7; add(createBtn, c);
        c.gridy = 8; add(backBtn, c);

        createBtn.addActionListener(e -> {
            String name = nameField.getText();
            String pw = new String(passwordField.getPassword());
            String type = (String) typeSelect.getSelectedItem();

            // TODO: send signup message to server
            JOptionPane.showMessageDialog(null, "Account created!");
            

            gui.showLoginScreen();
        });

        backBtn.addActionListener(e -> gui.showLoginScreen());
    }
}
