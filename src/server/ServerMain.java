package server;

import library.LibraryFacade;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Starting Library Server...");
        
        // Load configuration from config.properties
        int port = 8080; // Default
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/config.properties");
            props.load(fis);
            port = Integer.parseInt(props.getProperty("PORT", "8080"));
            fis.close();
            System.out.println("Configuration loaded from config.properties");
        } catch (IOException e) {
            System.out.println("Could not load config.properties, using defaults: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid PORT in config.properties, using default: 8080");
        }
        
        // Command line arguments override config file
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                System.out.println("Using port from command line: " + port);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port argument. Using port from config: " + port);
            }
        }
        
        // Initialize LibraryFacade
        LibraryFacade facade = null;
        try {
            facade = new LibraryFacade("data/library.ser");
        } catch (Exception e) {
            System.err.println("Error loading library data: " + e.getMessage());
            System.exit(1);
        }
        
        LibraryServer server = new LibraryServer(port, facade);
        server.startServer();
    }
}
