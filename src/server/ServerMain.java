package server;


import library.LibraryFacade;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Starting Library Server...");
        
        // Load configuration from config.properties
        String host = "0.0.0.0"; // Default to all interfaces
        int port = 8080; // Default
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/config.properties");
            props.load(fis);
            host = props.getProperty("HOST", "0.0.0.0");
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
            host = args[0];
            System.out.println("Using host from command line: " + host);
        }
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
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
        
        LibraryServer server = new LibraryServer(host, port, facade);
        server.startServer();
    }
}
