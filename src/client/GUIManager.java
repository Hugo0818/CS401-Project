package client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import library.Resource;
import library.Message;
import static util.DebugUtil.getCallerInfo;

public class GUIManager {
    private Client client;
    private JFrame mainFrame;
    private HashMap<String, JFrame> windows;

    public GUIManager() {
    	// default host and port if config read fails
        System.out.println("[DEBUG] " + getCallerInfo() + " GUIManager constructor called.");
        windows = new HashMap<>();
        
        String host = "localhost";
        int port = 8080;
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/config.properties");
            props.load(fis);
            host = props.getProperty("HOST");
            port = Integer.parseInt(props.getProperty("PORT"));
            fis.close();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load config.properties, using defaults: " + e.getMessage());
        }
        
        System.out.println("Initializing client connection to " + host + ":" + port);
        client = new Client(host, port);
        client.connectToServer();
        
        if (!client.isConnected()) {
            System.err.println("Failed to connect to server!");
        } else {
            System.out.println("Successfully connected to server!");
        }
        
        // Create and show the search menu instead of blank frame
//        JFrame searchMenu = makeSearchMenu();
//        windows.put("SearchMenu", searchMenu);
//        searchMenu.setVisible(true);
        mainFrame = new JFrame("Library Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        // Load login panel on startup
        showLoginScreen();

        mainFrame.setVisible(true);
    }
    
    public void setPanel(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showLoginScreen() {
        setPanel(new LoginPanel(this, client));
    }

    public void showSignupScreen() {
        setPanel(new SignupPanel(this, client));
    }

    public void showStaffDashboard() {
        setPanel(new StaffDashboardPanel());
    }

    public void showMemberDashboard() {
        setPanel(new MemberDashboardPanel());
    }
    
    //Blank Panel for Staff for testing
    public class StaffDashboardPanel extends JPanel {
        public StaffDashboardPanel() {
            add(new JLabel("Staff Dashboard"));
        }
    }
    
    //Blank Panel for Member for testing
    public class MemberDashboardPanel extends JPanel {
        public MemberDashboardPanel() {
            add(new JLabel("Member Dashboard"));
        }
    }

    /**
     * Creates the main search menu JFrame with search functionality.
     * Layout: Search bar at top, scrollable resource list on left,
     * details panel on right with edit/checkout buttons at bottom.
     * @return JFrame containing the search menu interface
     */
    public JFrame makeSearchMenu() {
        JFrame frame = new JFrame("Library Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search bar at top
        JTextField searchField = new JTextField();
        System.out.println("[DEBUG] " + getCallerInfo() + " Search field created.");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Split panel for list (left) and details (right)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Equal split
        
        // Left side: Scrollable list of resources
        DefaultListModel<Resource> listModel = new DefaultListModel<>();
        JList<Resource> resourceList = new JList<>(listModel);
        resourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resourceList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Resource) {
                    setText(((Resource) value).getDisplayName());
                }
                return this;
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(resourceList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Resources"));
        splitPane.setLeftComponent(listScrollPane);
        
        // Right side: Details panel with buttons
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        
        // Details text area (top right)
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Details"));
        rightPanel.add(detailsScrollPane, BorderLayout.CENTER);
        
        // Button panel (bottom right)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton editButton = new JButton("Edit");
        JButton checkoutButton = new JButton("Check Out");
        
        editButton.setPreferredSize(new Dimension(100, 30));
        checkoutButton.setPreferredSize(new Dimension(120, 30));
        
        buttonPanel.add(editButton);
        buttonPanel.add(checkoutButton);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        splitPane.setRightComponent(rightPanel);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        
        // Search executes on Enter key
        searchField.addActionListener(e -> {
            System.out.println("[DEBUG] " + getCallerInfo() + " Enter key pressed in search field.");
            String query = searchField.getText();
            System.out.println("[DEBUG] " + getCallerInfo() + " Search query: " + query);
            
            // Send "Catalog Search" message to server
            Message searchMessage = new Message("Catalog Search", query);
            client.sendMessage(searchMessage);
            
            // TODO: Wait for and process server response
            // For now, clear the list until server response handling is implemented
            listModel.clear();
        });
        
        // Add selection listener to update details panel
        resourceList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Resource selected = resourceList.getSelectedValue();
                if (selected != null) {
                    detailsArea.setText(selected.getDetails());
                } else {
                    detailsArea.setText("");
                }
            }
        });
        
        return frame;
    }
    // Methods for showing windows will be added here later
}
