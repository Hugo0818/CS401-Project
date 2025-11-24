package client;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(GUIManager gui, Client client) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel typeLabel = new JLabel("Select User Type:");
        String[] types = {"Member", "Staff"};
        JComboBox<String> typeSelect = new JComboBox<>(types);

        JTextField uidField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");

        // Add components
        c.gridy = 0; add(title, c);
        c.gridy = 1; add(typeLabel, c);
        c.gridy = 2; add(typeSelect, c);
        c.gridy = 3; add(new JLabel("User ID:"), c);
        c.gridy = 4; add(uidField, c);
        c.gridy = 5; add(new JLabel("Password:"), c);
        c.gridy = 6; add(passwordField, c);
        c.gridy = 7; add(loginBtn, c);
        c.gridy = 8; add(signupBtn, c);

        loginBtn.addActionListener(e -> {
            String uid = uidField.getText();
            String pw = new String(passwordField.getPassword());
            String type = (String) typeSelect.getSelectedItem();

            // TODO: send login message to server
            JOptionPane.showMessageDialog(null, "Login attempt: " + type);

            if (type.equals("Staff")) gui.showStaffDashboard();
            else gui.showMemberDashboard();
        });

        signupBtn.addActionListener(e -> gui.showSignupScreen());
    }
}

