package client;

import library.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Minimal GUIManager: creates login and signup panels and reacts to client callbacks.
 * GUIManager is constructed by Client and given the client instance.
 */
public class GUIManager {
    private final Client client;
    private final JFrame mainFrame;
    
//<<<<<<< HEAD
    private DefaultListModel<Member> resultsAreaModel;
    private JList<Member> resultsArea;
//=======
    // For managing search results display
    private JTable currentResultsTable;
    private javax.swing.table.DefaultTableModel currentTableModel;
    private JTextArea currentDetailsArea;
    private java.util.List<Resource> currentSearchResults;
    private String lastSearchQuery;
    private String currentMemberUid;

    public GUIManager(Client client) {
        this.client = client;
        mainFrame = new JFrame("Library System");
        //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setSize(640, 480);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		//disconnects the client
        		client.sendMessage(new Message(MessageType.W_CLOSED, null));
        		mainFrame.dispose();	
        	}
        });
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
        String[] menuItems = {"Checkout", "Check In", "Browse Catalog", "Add Resource", "Remove Resource", "Manage Members", "View Logs", "Logout"};
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
                    case "Add Resource" -> showAddResourcePanel(contentArea);
                    case "Remove Resource" -> showRemoveResourcePanel(contentArea);
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
        
        // Top panel with member UID and search
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberPanel.add(new JLabel("Member UID:"));
        JTextField memberUidField = new JTextField(15);
        memberPanel.add(memberUidField);
        topPanel.add(memberPanel);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Catalog:"));
        JTextField searchField = new JTextField(30);
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        topPanel.add(searchPanel);
        
        contentArea.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with split pane: results on left, details on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Left: Results table
        String[] columnNames = {"Resource Name", "Type"};
        currentTableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        currentResultsTable = new JTable(currentTableModel);
        currentResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(currentResultsTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Search Results"));
        splitPane.setLeftComponent(tableScroll);
        
        // Right: Details area
        currentDetailsArea = new JTextArea();
        currentDetailsArea.setEditable(false);
        currentDetailsArea.setText("Select a resource to view details.");
        currentDetailsArea.setLineWrap(true);
        currentDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(currentDetailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Resource Details"));
        splitPane.setRightComponent(detailsScroll);
        
        contentArea.add(splitPane, BorderLayout.CENTER);
        
        // Bottom panel with checkout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton checkoutBtn = new JButton("Checkout Selected Resource");
        checkoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(checkoutBtn);
        contentArea.add(bottomPanel, BorderLayout.SOUTH);
        
        // Table selection listener to show details
        currentResultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = currentResultsTable.getSelectedRow();
                if (selectedRow >= 0 && currentSearchResults != null && selectedRow < currentSearchResults.size()) {
                    Resource resource = currentSearchResults.get(selectedRow);
                    currentDetailsArea.setText(resource.getDetails() + 
                        "\nAvailable: " + (resource.isAvailable() ? "Yes" : "No"));
                }
            }
        });
        
        // Search button action
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                lastSearchQuery = query;
                currentTableModel.setRowCount(0);
                currentDetailsArea.setText("Searching...");
                client.sendMessage(new Message(MessageType.CATALOG_SEARCH_REQ, query));
            }
        });
        
        searchField.addActionListener(e -> searchBtn.doClick());
        
        // Checkout button action
        checkoutBtn.addActionListener(e -> {
            String memberUid = memberUidField.getText().trim();
            int selectedRow = currentResultsTable.getSelectedRow();
            
            if (memberUid.isEmpty()) {
                showError("Please enter a Member UID");
                return;
            }
            
            if (selectedRow < 0) {
                showError("Please select a resource to checkout");
                return;
            }
            
            if (currentSearchResults != null && selectedRow < currentSearchResults.size()) {
                Resource selectedResource = currentSearchResults.get(selectedRow);
                
                if (!selectedResource.isAvailable()) {
                    showError("This resource is not available for checkout");
                    return;
                }
                
                // Send checkout request with member UID and resource
                java.util.Map<String, Object> checkoutData = new java.util.HashMap<>();
                checkoutData.put("memberUid", memberUid);
                checkoutData.put("resource", selectedResource);
                
                client.setLastCheckoutResource(selectedResource);
                client.sendMessage(new Message(MessageType.CHECK_OUT_REQ, checkoutData));
            }
        });
    }
    
    private void showCheckinPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Check In Resources");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(titleLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Member UID:"));
        JTextField memberUidField = new JTextField(15);
        topPanel.add(memberUidField);
        JButton searchMemberBtn = new JButton("Find Borrowed Items");
        topPanel.add(searchMemberBtn);
        
        contentArea.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with split pane: results on left, details on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Left: Results table
        String[] columnNames = {"Resource Name", "Type"};
        currentTableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        currentResultsTable = new JTable(currentTableModel);
        currentResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(currentResultsTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Currently Borrowed"));
        splitPane.setLeftComponent(tableScroll);
        
        // Right: Details area
        currentDetailsArea = new JTextArea();
        currentDetailsArea.setEditable(false);
        currentDetailsArea.setText("Enter a Member UID and click 'Find Borrowed Items' to see their checked out resources.");
        currentDetailsArea.setLineWrap(true);
        currentDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(currentDetailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Resource Details"));
        splitPane.setRightComponent(detailsScroll);
        
        contentArea.add(splitPane, BorderLayout.CENTER);
        
        // Bottom panel with return button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton returnBtn = new JButton("Return Selected Resource");
        returnBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(returnBtn);
        contentArea.add(bottomPanel, BorderLayout.SOUTH);
        
        // Table selection listener to show details
        currentResultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = currentResultsTable.getSelectedRow();
                if (selectedRow >= 0 && currentSearchResults != null && selectedRow < currentSearchResults.size()) {
                    Resource resource = currentSearchResults.get(selectedRow);
                    currentDetailsArea.setText(resource.getDetails());
                }
            }
        });
        
        // Search member button action
        searchMemberBtn.addActionListener(e -> {
            String memberUid = memberUidField.getText().trim();
            if (memberUid.isEmpty()) {
                showError("Please enter a Member UID");
                return;
            }
            
            currentMemberUid = memberUid;
            currentTableModel.setRowCount(0);
            currentDetailsArea.setText("Loading borrowed resources...");
            client.sendMessage(new Message(MessageType.MEMBER_BORROWED_REQ, memberUid));
        });
        
        memberUidField.addActionListener(e -> searchMemberBtn.doClick());
        
        // Return button action
        returnBtn.addActionListener(e -> {
            int selectedRow = currentResultsTable.getSelectedRow();
            
            if (currentMemberUid == null || currentMemberUid.isEmpty()) {
                showError("Please search for a member first");
                return;
            }
            
            if (selectedRow < 0) {
                showError("Please select a resource to return");
                return;
            }
            
            if (currentSearchResults != null && selectedRow < currentSearchResults.size()) {
                Resource selectedResource = currentSearchResults.get(selectedRow);
                
                // Send check-in request with member UID and resource
                java.util.Map<String, Object> checkinData = new java.util.HashMap<>();
                checkinData.put("memberUid", currentMemberUid);
                checkinData.put("resource", selectedResource);
                
                client.setLastCheckinResource(selectedResource);
                client.sendMessage(new Message(MessageType.CHECK_IN_REQ, checkinData));
            }
        });
    }
    
    private void showBrowseCatalogPanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Browse Catalog");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Catalog:"));
        JTextField searchField = new JTextField(30);
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        
        contentArea.add(searchPanel, BorderLayout.NORTH);
        
        // Center panel with split pane: results on left, details on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Left: Results table
        String[] columnNames = {"Resource Name", "Type"};
        currentTableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        currentResultsTable = new JTable(currentTableModel);
        currentResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(currentResultsTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Search Results"));
        splitPane.setLeftComponent(tableScroll);
        
        // Right: Details area
        currentDetailsArea = new JTextArea();
        currentDetailsArea.setEditable(false);
        currentDetailsArea.setText("Select a resource to view details.");
        currentDetailsArea.setLineWrap(true);
        currentDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(currentDetailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Resource Details"));
        splitPane.setRightComponent(detailsScroll);
        
        contentArea.add(splitPane, BorderLayout.CENTER);
        
        // Table selection listener to show details
        currentResultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = currentResultsTable.getSelectedRow();
                if (selectedRow >= 0 && currentSearchResults != null && selectedRow < currentSearchResults.size()) {
                    Resource resource = currentSearchResults.get(selectedRow);
                    currentDetailsArea.setText(resource.getDetails() + 
                        "\nAvailable: " + (resource.isAvailable() ? "Yes" : "No"));
                }
            }
        });
        
        // Search button action
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                lastSearchQuery = query;
                currentTableModel.setRowCount(0);
                currentDetailsArea.setText("Searching...");
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
        
        resultsAreaModel = new DefaultListModel<>();
        resultsArea = new JList<>(resultsAreaModel);
        resultsArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentArea.add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton removeBtn = new JButton("Remove Selected Member");
        removeBtn.setEnabled(false);
        bottomPanel.add(removeBtn);
        contentArea.add(bottomPanel, BorderLayout.SOUTH);
        
        resultsArea.addListSelectionListener(e -> {
            removeBtn.setEnabled(!resultsArea.isSelectionEmpty());
        });
        
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            
            resultsAreaModel.clear();
            
            if (query.isEmpty()) {
                return;
            }
            client.sendMessage(new Message(MessageType.MEMBER_SEARCH_REQ, query));
        });
        
        removeBtn.addActionListener(e -> { 
            Member selected = resultsArea.getSelectedValue();
            if (selected == null) {
                showError("No member selected.");
                return;
            }

            String uid = selected.getUID();
            int numID = Integer.parseInt(uid.substring(1));

            client.sendMessage(new Message(MessageType.REMOVE_MEMBER_REQ, numID));
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
    public void refreshCurrentSearch() {
        if (lastSearchQuery != null && !lastSearchQuery.isEmpty()) {
            client.sendMessage(new Message(MessageType.CATALOG_SEARCH_REQ, lastSearchQuery));
        }
    }
    
    public void handleCatalogSearchResults(Object payload) {
        SwingUtilities.invokeLater(() -> {
            if (payload instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<Resource> results = (java.util.List<Resource>) payload;
                currentSearchResults = results;
                
                System.out.println("[GUIManager] Received catalog search results:");
                for (Resource r : results) {
                    System.out.println("  - " + r.getDisplayName() + " - isAvailable: " + r.isAvailable());
                }
                
                if (currentTableModel != null && currentResultsTable != null) {
                    // Clear existing rows
                    currentTableModel.setRowCount(0);
                    
                    // Add new rows
                    for (Resource resource : results) {
                        String resourceType = getResourceType(resource);
                        currentTableModel.addRow(new Object[]{
                            resource.getDisplayName(),
                            resourceType
                        });
                    }
                    
                    if (currentDetailsArea != null) {
                        currentDetailsArea.setText("Found " + results.size() + " result(s). Select one to view details.");
                    }
                } else {
                    // Fallback if table not initialized
                    showInfo("Search returned " + results.size() + " items.");
                }
            } else {
                if (currentDetailsArea != null) {
                    currentDetailsArea.setText("No results found.");
                } else {
                    showInfo("Search returned no items.");
                }
            }
        });
    }
    
    private String getResourceType(Resource resource) {
        if (resource instanceof Book) {
            return "Book";
        } else if (resource instanceof Movie) {
            return "Movie";
        } else if (resource instanceof CD) {
            return "CD";
        } else if (resource instanceof GenericResource) {
            return "Other";
        } else {
            return "Unknown";
        }
    }

    public void handleMemberSearchResults(Object payload) {
        SwingUtilities.invokeLater(() -> {
            if (!(payload instanceof java.util.List<?> list)) {
                showInfo("No results");
                return;
            }

            resultsAreaModel.clear();

            for (Object o : list) {
                if (o instanceof Member m) {
                	resultsAreaModel.addElement(m);
                }
            }

            showInfo("Found " + resultsAreaModel.size() + " member(s).");
        });
    }
    
    public void handleMemberBorrowedResults(Object payload) {
        SwingUtilities.invokeLater(() -> {
            if (payload instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<Resource> results = (java.util.List<Resource>) payload;
                currentSearchResults = results;
                
                if (currentTableModel != null && currentResultsTable != null) {
                    // Clear existing rows
                    currentTableModel.setRowCount(0);
                    
                    // Add new rows
                    for (Resource resource : results) {
                        String resourceType = getResourceType(resource);
                        currentTableModel.addRow(new Object[]{
                            resource.getDisplayName(),
                            resourceType
                        });
                    }
                    
                    if (currentDetailsArea != null) {
                        currentDetailsArea.setText("Found " + results.size() + " borrowed item(s). Select one to view details.");
                    }
                } else {
                    showInfo("Member has " + results.size() + " borrowed items.");
                }
            } else {
                if (currentDetailsArea != null) {
                    currentDetailsArea.setText("This member has no borrowed resources.");
                } else {
                    showInfo("No borrowed items found.");
                }
            }
        });
    }
    
    public void refreshMemberBorrowed() {
        if (currentMemberUid != null && !currentMemberUid.isEmpty()) {
            client.sendMessage(new Message(MessageType.MEMBER_BORROWED_REQ, currentMemberUid));
        }
    }
    
    public void updateResourceAvailability(Resource resource, boolean available) {
        if (currentSearchResults != null) {
            for (int i = 0; i < currentSearchResults.size(); i++) {
                Resource r = currentSearchResults.get(i);
                if (r.getDisplayName().equals(resource.getDisplayName()) &&
                    r.getDetails().equals(resource.getDetails())) {
                    r.setCheckedOut(available);
                    System.out.println("[GUIManager] Locally updated " + r.getDisplayName() + " availability to: " + available);
                    
                    // If checking in (making available), remove from borrowed list
                    if (available && currentTableModel != null && currentResultsTable != null) {
                        currentSearchResults.remove(i);
                        currentTableModel.removeRow(i);
                        if (currentDetailsArea != null) {
                            if (currentSearchResults.isEmpty()) {
                                currentDetailsArea.setText("No borrowed resources.");
                            } else {
                                currentDetailsArea.setText("Select a resource to view details.");
                            }
                        }
                        System.out.println("[GUIManager] Removed resource from borrowed list display");
                    } else {
                        // For checkout, just refresh the details if selected
                        int selectedRow = currentResultsTable.getSelectedRow();
                        if (selectedRow >= 0 && selectedRow < currentSearchResults.size() &&
                            currentSearchResults.get(selectedRow) == r && currentDetailsArea != null) {
                            currentDetailsArea.setText(r.getDetails() + 
                                "\nAvailable: " + (r.isAvailable() ? "Yes" : "No"));
                        }
                    }
                    break;
                }
            }
        }
    }
    public void handleRemoveMember(Object payload) {
        SwingUtilities.invokeLater(() -> {
            if (payload instanceof String msg) {
                showInfo(msg);
            }

            // If a member is currently selected, remove it from the list model
            Member selected = resultsArea.getSelectedValue();
            if (selected != null) {
                resultsAreaModel.removeElement(selected);
            }
        });
    }
    
    public void showError(String s) { JOptionPane.showMessageDialog(mainFrame, s, "Error", JOptionPane.ERROR_MESSAGE); }
    public void showInfo(String s) { JOptionPane.showMessageDialog(mainFrame, s, "Info", JOptionPane.INFORMATION_MESSAGE); }
    
    private void showAddResourcePanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Add New Resource");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Resource type selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Resource Type:"));
        
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Book", "Movie", "CD", "Other"});
        typePanel.add(typeCombo);
        
        mainPanel.add(typePanel, BorderLayout.NORTH);
        
        // Fields panel (dynamic based on type)
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        JScrollPane fieldsScroll = new JScrollPane(fieldsPanel);
        fieldsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        mainPanel.add(fieldsScroll, BorderLayout.CENTER);
        
        // Create button
        JButton createBtn = new JButton("Create Resource");
        createBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(createBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        contentArea.add(mainPanel, BorderLayout.CENTER);
        
        // Update fields when type changes
        typeCombo.addActionListener(e -> {
            fieldsPanel.removeAll();
            String selectedType = (String) typeCombo.getSelectedItem();
            
            switch (selectedType) {
                case "Book" -> createBookFields(fieldsPanel);
                case "Movie" -> createMovieFields(fieldsPanel);
                case "CD" -> createCDFields(fieldsPanel);
                case "Other" -> createGenericFields(fieldsPanel);
            }
            
            fieldsPanel.revalidate();
            fieldsPanel.repaint();
        });
        
        // Initialize with Book fields
        createBookFields(fieldsPanel);
        
        // Create button action
        createBtn.addActionListener(e -> {
            String selectedType = (String) typeCombo.getSelectedItem();
            Resource newResource = null;
            
            try {
                switch (selectedType) {
                    case "Book" -> newResource = createBookFromFields(fieldsPanel);
                    case "Movie" -> newResource = createMovieFromFields(fieldsPanel);
                    case "CD" -> newResource = createCDFromFields(fieldsPanel);
                    case "Other" -> newResource = createGenericFromFields(fieldsPanel);
                }
                
                if (newResource != null) {
                    // Send message to server to add resource
                    client.sendMessage(new Message(MessageType.ADD_RESOURCE_REQ, newResource));
                    showInfo("Resource created successfully!");
                    
                    // Clear fields
                    fieldsPanel.removeAll();
                    if (selectedType.equals("Book")) createBookFields(fieldsPanel);
                    else if (selectedType.equals("Movie")) createMovieFields(fieldsPanel);
                    else if (selectedType.equals("CD")) createCDFields(fieldsPanel);
                    else createGenericFields(fieldsPanel);
                    fieldsPanel.revalidate();
                    fieldsPanel.repaint();
                }
            } catch (Exception ex) {
                showError("Error creating resource: " + ex.getMessage());
            }
        });
    }
    
    private void createBookFields(JPanel panel) {
        addLabeledField(panel, "Title:", "");
        addLabeledField(panel, "Author:", "");
        addLabeledField(panel, "Publisher:", "");
        addLabeledField(panel, "ISBN:", "");
    }
    
    private void createMovieFields(JPanel panel) {
        addLabeledField(panel, "Title:", "");
        addLabeledField(panel, "Director:", "");
        addLabeledField(panel, "Runtime (mins):", "");
        addLabeledField(panel, "Rating:", "");
    }
    
    private void createCDFields(JPanel panel) {
        addLabeledField(panel, "Album Name:", "");
        addLabeledField(panel, "Artist:", "");
        addLabeledField(panel, "Number of Songs:", "");
    }
    
    private void createGenericFields(JPanel panel) {
        addLabeledField(panel, "Resource Name:", "");
        
        JLabel extraLabel = new JLabel("Additional Fields:");
        extraLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        extraLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(extraLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Container for dynamic field pairs
        JPanel dynamicFieldsContainer = new JPanel();
        dynamicFieldsContainer.setLayout(new BoxLayout(dynamicFieldsContainer, BoxLayout.Y_AXIS));
        dynamicFieldsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dynamicFieldsContainer);
        
        // Add first empty pair
        addDynamicFieldPair(dynamicFieldsContainer);
    }
    
    private void addDynamicFieldPair(JPanel container) {
        JPanel pairPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pairPanel.setMaximumSize(new Dimension(600, 35));
        pairPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField keyField = new JTextField(15);
        JTextField valueField = new JTextField(20);
        
        pairPanel.add(new JLabel("Field Name:"));
        pairPanel.add(keyField);
        pairPanel.add(new JLabel("Value:"));
        pairPanel.add(valueField);
        
        container.add(pairPanel);
        
        // Add new pair when either field gets focus and has content
        Runnable checkAndAdd = () -> {
            String key = keyField.getText().trim();
            String value = valueField.getText().trim();
            
            if (!key.isEmpty() || !value.isEmpty()) {
                // Check if this is the last pair
                int index = getComponentIndex(container, pairPanel);
                if (index == container.getComponentCount() - 1) {
                    addDynamicFieldPair(container);
                    container.revalidate();
                    container.repaint();
                }
            }
        };
        
        keyField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                checkAndAdd.run();
            }
        });
        
        valueField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                checkAndAdd.run();
            }
        });
    }
    
    private int getComponentIndex(JPanel container, JPanel component) {
        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == component) return i;
        }
        return -1;
    }
    
    private void addLabeledField(JPanel panel, String labelText, String defaultValue) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        fieldPanel.setMaximumSize(new Dimension(600, 35));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25));
        JTextField textField = new JTextField(defaultValue, 30);
        
        fieldPanel.add(label);
        fieldPanel.add(textField);
        panel.add(fieldPanel);
    }
    
    private Book createBookFromFields(JPanel panel) {
        Component[] components = panel.getComponents();
        String title = getFieldValue(components, 0);
        String author = getFieldValue(components, 1);
        String publisher = getFieldValue(components, 2);
        String isbn = getFieldValue(components, 3);
        
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        return new Book(title, author, publisher, isbn, true);
    }
    
    private Movie createMovieFromFields(JPanel panel) {
        Component[] components = panel.getComponents();
        String title = getFieldValue(components, 0);
        String director = getFieldValue(components, 1);
        String runtimeStr = getFieldValue(components, 2);
        String rating = getFieldValue(components, 3);
        
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        int runtime = 0;
        try {
            runtime = Integer.parseInt(runtimeStr);
        } catch (NumberFormatException e) {
            // Default to 0 if not provided or invalid
        }
        
        return new Movie(title, director, runtime, rating);
    }
    
    private CD createCDFromFields(JPanel panel) {
        Component[] components = panel.getComponents();
        String albumName = getFieldValue(components, 0);
        String artist = getFieldValue(components, 1);
        String numSongsStr = getFieldValue(components, 2);
        
        if (albumName.isEmpty()) {
            throw new IllegalArgumentException("Album name is required");
        }
        
        int numSongs = 0;
        try {
            numSongs = Integer.parseInt(numSongsStr);
        } catch (NumberFormatException e) {
            // Default to 0 if not provided or invalid
        }
        
        return new CD(albumName, artist, numSongs);
    }
    
    private GenericResource createGenericFromFields(JPanel panel) {
        Component[] components = panel.getComponents();
        String resourceName = getFieldValue(components, 0);
        
        if (resourceName.isEmpty()) {
            throw new IllegalArgumentException("Resource name is required");
        }
        
        // Find the dynamic fields container
        JPanel dynamicContainer = null;
        for (Component comp : components) {
            if (comp instanceof JPanel p && p.getLayout() instanceof BoxLayout) {
                dynamicContainer = p;
                break;
            }
        }
        
        java.util.Map<String, String> extraDetails = new java.util.HashMap<>();
        
        if (dynamicContainer != null) {
            Component[] pairs = dynamicContainer.getComponents();
            for (Component comp : pairs) {
                if (comp instanceof JPanel pairPanel) {
                    Component[] pairComps = pairPanel.getComponents();
                    String key = "";
                    String value = "";
                    
                    for (Component c : pairComps) {
                        if (c instanceof JTextField tf) {
                            if (key.isEmpty()) {
                                key = tf.getText().trim();
                            } else {
                                value = tf.getText().trim();
                            }
                        }
                    }
                    
                    if (!key.isEmpty() && !value.isEmpty()) {
                        extraDetails.put(key, value);
                    }
                }
            }
        }
        
        return new GenericResource(resourceName, extraDetails);
    }
    
    private String getFieldValue(Component[] components, int fieldIndex) {
        int currentField = 0;
        for (Component comp : components) {
            if (comp instanceof JPanel panel) {
                for (Component c : panel.getComponents()) {
                    if (c instanceof JTextField tf) {
                        if (currentField == fieldIndex) {
                            return tf.getText().trim();
                        }
                        currentField++;
                        break;
                    }
                }
            }
        }
        return "";
    }
    
    private void showRemoveResourcePanel(JPanel contentArea) {
        contentArea.setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Remove Resource");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentArea.add(titleLabel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Catalog:"));
        JTextField searchField = new JTextField(30);
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        
        contentArea.add(searchPanel, BorderLayout.NORTH);
        
        // Center panel with split pane: results on left, details on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Left: Results table
        String[] columnNames = {"Resource Name", "Type"};
        currentTableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        currentResultsTable = new JTable(currentTableModel);
        currentResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(currentResultsTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Search Results"));
        splitPane.setLeftComponent(tableScroll);
        
        // Right: Details area
        currentDetailsArea = new JTextArea();
        currentDetailsArea.setEditable(false);
        currentDetailsArea.setText("Select a resource to view details.");
        currentDetailsArea.setLineWrap(true);
        currentDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(currentDetailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Resource Details"));
        splitPane.setRightComponent(detailsScroll);
        
        contentArea.add(splitPane, BorderLayout.CENTER);
        
        // Bottom panel with remove button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton removeBtn = new JButton("Remove Selected Resource");
        removeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        removeBtn.setBackground(new Color(200, 50, 50));
        removeBtn.setForeground(Color.WHITE);
        bottomPanel.add(removeBtn);
        contentArea.add(bottomPanel, BorderLayout.SOUTH);
        
        // Table selection listener to show details
        currentResultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = currentResultsTable.getSelectedRow();
                if (selectedRow >= 0 && currentSearchResults != null && selectedRow < currentSearchResults.size()) {
                    Resource resource = currentSearchResults.get(selectedRow);
                    currentDetailsArea.setText(resource.getDetails() + 
                        "\nAvailable: " + (resource.isAvailable() ? "Yes" : "No"));
                }
            }
        });
        
        // Search button action
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                lastSearchQuery = query;
                currentTableModel.setRowCount(0);
                currentDetailsArea.setText("Searching...");
                client.sendMessage(new Message(MessageType.CATALOG_SEARCH_REQ, query));
            }
        });
        
        searchField.addActionListener(e -> searchBtn.doClick());
        
        // Remove button action
        removeBtn.addActionListener(e -> {
            int selectedRow = currentResultsTable.getSelectedRow();
            
            if (selectedRow < 0) {
                showError("Please select a resource to remove");
                return;
            }
            
            if (currentSearchResults != null && selectedRow < currentSearchResults.size()) {
                Resource selectedResource = currentSearchResults.get(selectedRow);
                
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Are you sure you want to remove: " + selectedResource.getDisplayName() + "?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    client.sendMessage(new Message(MessageType.REMOVE_RESOURCE_REQ, selectedResource));
                }
            }
        });
    }
}
