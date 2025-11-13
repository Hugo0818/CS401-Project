package client;

import javax.swing.JFrame;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GUIManager {
    private Client client;
    private JFrame mainFrame;

    public GUIManager() {
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
        
        mainFrame = new JFrame("Library Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null); // Center on screen
        mainFrame.setVisible(true); // Make the window visible
    }

    // Methods for showing windows will be added here later
}
