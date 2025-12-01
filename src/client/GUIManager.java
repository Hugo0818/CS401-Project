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

        // Login action
        Runnable performLogin = () -> {
            String uid = uidField.getText().trim();
            String pw = new String(passField.getPassword());
            boolean isStaff = staffCB.isSelected();
            if (uid.isEmpty() || pw.isEmpty()) {
                showError("Enter credentials");
                return;
            }
            System.out.println("[CLIENT] Sending login message for: " + uid);
            LoginInfo li = new LoginInfo(uid, pw, isStaff);
            client.sendMessage(new Message(MessageType.LOGIN_ATTEMPT, li));
        };

        loginBtn.addActionListener(e -> performLogin.run());
        
        // Add Enter key support
        uidField.addActionListener(e -> performLogin.run());
        passField.addActionListener(e -> performLogin.run());

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
            client.sendMessage(new Message(MessageType.SIGNUP_ATTEMPT, li));

            showInfo("Signup sent...");
            showLoginScreen();
        });

        backBtn.addActionListener(e -> showLoginScreen());

        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.setVisible(true);
    }

    public void showStaffDashboard() {
        mainFrame.setSize(900, 600);
        mainFrame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Left sidebar menu
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 45, 48));
        sidebar.setPreferredSize(new Dimension(200, 600));
        
        // Title
        JLabel title = new JLabel("Staff Portal");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.add(title);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Content area
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Select an option from the menu", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.GRAY);
        contentArea.add(welcomeLabel, BorderLayout.CENTER);
        
        // Menu buttons
        String[] menuItems = {"Checkout", "Check In", "Browse Catalog", "Manage Members", "View Logs", "Logout"};
        ButtonGroup buttonGroup = new ButtonGroup();
        
        for (String item : menuItems) {
            JToggleButton btn = createMenuButton(item);
            buttonGroup.add(btn);
            sidebar.add(btn);
            
            btn.addActionListener(e -> {
                contentArea.removeAll();
                switch (item) {
                    case "Checkout" -> showCheckoutPanel(contentArea);
                    case "Check In" -> showCheckinPanel(contentArea);
                    case "Browse Catalog" -> showBrowseCatalogPanel(contentArea);
                    case "Manage Members" -> showManageMembersPanel(contentArea);
                    case "View Logs" -> showLogsPanel(contentArea);
                    case "Logout" -> {
                        client.sendMessage(new Message(MessageType.LOGOUT_ATTEMPT, null));
                        return;
                    }
                }
                contentArea.revalidate();
                contentArea.repaint();
            });
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentArea, BorderLayout.CENTER);
        
        mainFrame.setContentPane(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showMemberDashboard() {
        mainFrame.setSize(900, 600);
        mainFrame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Left sidebar menu
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 60, 90));
        sidebar.setPreferredSize(new Dimension(200, 600));
        
        // Title
        JLabel title = new JLabel("Member Portal");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.add(title);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Content area
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Select an option from the menu", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.GRAY);
        contentArea.add(welcomeLabel, BorderLayout.CENTER);
        
        // Menu buttons
        String[] menuItems = {"Browse Catalog", "My Checkouts", "Checkout History", "Logout"};
        ButtonGroup buttonGroup = new ButtonGroup();
        
        for (String item : menuItems) {
            JToggleButton btn = createMenuButton(item);
            buttonGroup.add(btn);
            sidebar.add(btn);
            
            btn.addActionListener(e -> {
                contentArea.removeAll();
                switch (item) {
                    case "Browse Catalog" -> showBrowseCatalogPanel(contentArea);
                    case "My Checkouts" -> showMyCheckoutsPanel(contentArea);
                    case "Checkout History" -> showCheckoutHistoryPanel(contentArea);
                    case "Logout" -> {
                        client.sendMessage(new Message(MessageType.LOGOUT_ATTEMPT, null));
                        return;
                    }
                }
                contentArea.revalidate();
                contentArea.repaint();
            });
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentArea, BorderLayout.CENTER);
        
        mainFrame.setContentPane(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    private JToggleButton createMenuButton(String text) {
        JToggleButton btn = new JToggleButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60, 60, 63));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover and selection effects
        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(new Color(0, 120, 215));
            } else if (btn.getModel().isRollover()) {
                btn.setBackground(new Color(80, 80, 83));
            } else {
                btn.setBackground(new Color(60, 60, 63));
            }
        });
        
        return btn;
    }
    
    private void showCheckoutPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Checkout Resources");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search Catalog:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Results area
        JTextArea resultsArea = new JTextArea(15, 50);
        resultsArea.setEditable(false);
        resultsArea.setText("Enter a search term and click Search to browse the catalog.");
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentArea.add(centerPanel, BorderLayout.CENTER);
        
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                resultsArea.setText("Searching for: " + query + "\n\nResults will appear here...");
                client.sendMessage(new Message(MessageType.CATALOG_SEARCH_REQ, query));
            }
        });
        
        searchField.addActionListener(e -> searchBtn.doClick());
    }
    
    private void showCheckinPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Check In Resources");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        JLabel infoLabel = new JLabel("Check in functionality coming soon...");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(infoLabel, BorderLayout.CENTER);
        
        contentArea.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void showBrowseCatalogPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Browse Catalog");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Results area
        JTextArea resultsArea = new JTextArea(15, 50);
        resultsArea.setEditable(false);
        resultsArea.setText("Enter a search term to browse available resources.");
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentArea.add(centerPanel, BorderLayout.CENTER);
        
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                resultsArea.setText("Searching for: " + query + "\n\nResults will appear here...");
                client.sendMessage(new Message(MessageType.CATALOG_SEARCH_REQ, query));
            }
        });
        
        searchField.addActionListener(e -> searchBtn.doClick());
    }
    
    private void showManageMembersPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Manage Members");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search Members:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        JTextArea resultsArea = new JTextArea(15, 50);
        resultsArea.setEditable(false);
        resultsArea.setText("Search for members by name or UID.");
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentArea.add(centerPanel, BorderLayout.CENTER);
        
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            resultsArea.setText("Searching for members: " + query + "\n\nResults will appear here...");
            client.sendMessage(new Message(MessageType.MEMBER_SEARCH_REQ, query));
        });
        
        searchField.addActionListener(e -> searchBtn.doClick());
    }
    
    private void showLogsPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("System Logs");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea logsArea = new JTextArea(15, 50);
        logsArea.setEditable(false);
        logsArea.setText("Recent system activity logs will appear here...");
        JScrollPane scrollPane = new JScrollPane(logsArea);
        
        contentArea.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void showMyCheckoutsPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("My Current Checkouts");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea checkoutsArea = new JTextArea(15, 50);
        checkoutsArea.setEditable(false);
        checkoutsArea.setText("Your currently checked out items will appear here...");
        JScrollPane scrollPane = new JScrollPane(checkoutsArea);
        
        contentArea.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void showCheckoutHistoryPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Checkout History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea historyArea = new JTextArea(15, 50);
        historyArea.setEditable(false);
        historyArea.setText("Your checkout history will appear here...");
        JScrollPane scrollPane = new JScrollPane(historyArea);
        
        contentArea.add(scrollPane, BorderLayout.CENTER);
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
