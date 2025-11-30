package client;

import library.*;
import javax.swing.*;
import java.awt.*;

/**
 * Minimal GUIManager: creates login and signup panels and reacts to client callbacks.
 * GUIManager is constructed by Client and given the client instance.
 */
public class GUIManager {
    private final Client client;
    private final JFrame mainFrame;

    public GUIManager(Client client) {
        this.client = client;
        mainFrame = new JFrame("Library System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(640, 480);
        mainFrame.setLocationRelativeTo(null);
    }

    public void showLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);

        JLabel title = new JLabel("Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(title, c);

        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("Staff?"), c);
        JCheckBox staffCB = new JCheckBox();
        c.gridx = 1; panel.add(staffCB, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("UID / Name:"), c);
        JTextField uidField = new JTextField(20);
        c.gridx = 1; panel.add(uidField, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Password:"), c);
        JPasswordField passField = new JPasswordField(20);
        c.gridx = 1; panel.add(passField, c);

        JButton loginBtn = new JButton("Login");
        c.gridy++; c.gridx = 0; panel.add(loginBtn, c);
        JButton signupBtn = new JButton("Sign up");
        c.gridx = 1; panel.add(signupBtn, c);

        JLabel info = new JLabel(" ");
        c.gridy++; c.gridx = 0; c.gridwidth = 2; panel.add(info, c);

        loginBtn.addActionListener(e -> {
            String uid = uidField.getText().trim();
            String pw = new String(passField.getPassword());
            boolean isStaff = staffCB.isSelected();
            if (uid.isEmpty() || pw.isEmpty()) {
                showError("Enter credentials");
                return;
            }
            LoginInfo li = new LoginInfo(uid, pw, isStaff);
            client.sendMessage(new Message(MessageType.LOGIN_ATTEMPT, li));
            showInfo("Login sent...");
        });

        signupBtn.addActionListener(e -> showSignupScreen());

        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.setVisible(true);
    }

    public void showSignupScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0;

        panel.add(new JLabel("Create Account"), c);
        c.gridy++;
        panel.add(new JLabel("Staff?"), c);
        JCheckBox staffCB = new JCheckBox();
        c.gridx = 1; panel.add(staffCB, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Name:"), c);
        JTextField nameField = new JTextField(20);
        c.gridx = 1; panel.add(nameField, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Password:"), c);
        JPasswordField passField = new JPasswordField(20);
        c.gridx = 1; panel.add(passField, c);

        JButton createBtn = new JButton("Create");
        c.gridy++; c.gridx = 0; panel.add(createBtn, c);
        JButton backBtn = new JButton("Back");
        c.gridx = 1; panel.add(backBtn, c);

        JLabel info = new JLabel(" ");
        c.gridy++; c.gridx = 0; c.gridwidth = 2; panel.add(info, c);

        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String pw = new String(passField.getPassword());
            boolean isStaff = staffCB.isSelected();
            if (name.isEmpty() || pw.isEmpty()) { showError("Fill fields"); return; }
            
            LoginInfo li = new LoginInfo(name, pw, isStaff); // uidOrName holds name for signup

            showInfo("Signup sent...");
        });

        backBtn.addActionListener(e -> showLoginScreen());

        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.setVisible(true);
    }

    public void showStaffDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel("Staff Dashboard", SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 24));
        p.add(l, BorderLayout.NORTH);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> client.sendMessage(new Message(MessageType.LOGOUT_ATTEMPT, null)));
        p.add(logout, BorderLayout.SOUTH);
        mainFrame.setContentPane(p);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showMemberDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel("Member Dashboard", SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 24));
        p.add(l, BorderLayout.NORTH);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> client.sendMessage(new Message(MessageType.LOGOUT_ATTEMPT, null)));
        p.add(logout, BorderLayout.SOUTH);
        mainFrame.setContentPane(p);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Called by client listener when server sends catalog search results.
    public void handleCatalogSearchResults(Object payload) {
        // payload is expected to be ArrayList<Resource>
        // You can show results in a new window. For now show a dialog.
        SwingUtilities.invokeLater(() -> {
            if (payload instanceof java.util.List list) {
                showInfo("Search returned " + list.size() + " items.");
            } else showInfo("Search returned no items.");
        });
    }

    public void handleMemberSearchResults(Object payload) {
        SwingUtilities.invokeLater(() -> {
            if (payload instanceof java.util.List list) showInfo("Member search returned " + list.size());
            else showInfo("No results");
        });
    }

    public void showError(String s) { JOptionPane.showMessageDialog(mainFrame, s, "Error", JOptionPane.ERROR_MESSAGE); }
    public void showInfo(String s) { JOptionPane.showMessageDialog(mainFrame, s, "Info", JOptionPane.INFORMATION_MESSAGE); }
}
