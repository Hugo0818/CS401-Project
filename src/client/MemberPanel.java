package client;

import javax.swing.*;
import java.awt.*;

public class MemberPanel extends JPanel {
	public MemberPanel(GUIManager gui, Client client) {

	    setLayout(new BorderLayout(20, 20));
	    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    
	    // Title
	    JLabel title = new JLabel("Member Dashboard", SwingConstants.CENTER);
	    title.setFont(new Font("Arial", Font.BOLD, 24));
	    add(title, BorderLayout.NORTH);
	
	    // Buttons
	    JButton searchButton = new JButton("Search Catalog");
	    JButton logoutButton = new JButton("Logout");
	
	    // Button panel
	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new GridLayout(2, 1, 15, 15));
	    centerPanel.add(searchButton);
	    centerPanel.add(logoutButton);
	
	    add(centerPanel, BorderLayout.CENTER);
	
	    // Action Listeners
	    searchButton.addActionListener(e -> gui.makeSearchMenu());  // Open Search Menu
	    logoutButton.addActionListener(e -> gui.showLoginScreen()); // Logout
	}
}
