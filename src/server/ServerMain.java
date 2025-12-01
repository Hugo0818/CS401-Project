package server;
import static util.DebugUtil.getCallerInfo;


import library.LibraryFacade;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("[DEBUG] " + getCallerInfo() + " Starting Library Server...");
        
        // Load configuration from config.properties
        String host = "127.0.0.1"; // Default to all interfaces
        int port = 12345; // Default
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/config.properties");
            props.load(fis);
            host = props.getProperty("HOST", "127.0.0.1");
            port = Integer.parseInt(props.getProperty("PORT", "12345"));
            fis.close();
            System.out.println("[DEBUG] " + getCallerInfo() + " Configuration loaded from config.properties");
        } catch (IOException e) {
            System.out.println("Could not load config.properties, using defaults: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid PORT in config.properties, using default: 8080");
        }
        
        // Command line arguments override config file
        if (args.length > 0) {
            host = args[0];
            System.out.println("[DEBUG] " + getCallerInfo() + " Using host from command line: " + host);
        }
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
                System.out.println("[DEBUG] " + getCallerInfo() + " Using port from command line: " + port);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port argument. Using port from config: " + port);
            }
        }
        
        // Initialize LibraryFacade
        final LibraryFacade facade;
        try {
            facade = new LibraryFacade("data/library.ser");
        } catch (Exception e) {
            System.err.println("Error loading library data: " + e.getMessage());
            System.exit(1);
            return; // Required after System.exit() for compiler
        }
        
        LibraryServer server = new LibraryServer(host, port, facade);
        
        // Register shutdown hook to ensure cleanup on termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[SHUTDOWN] Shutting down server gracefully...");
            server.stopServer();
            facade.saveChanges();
            System.out.println("[SHUTDOWN] Server stopped and data saved.");
        }, "ShutdownHook"));
        
        server.startServer();
    }
}
