package server;

import library.LibraryFacade;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LibraryServer {
    private LibraryFacade libraryFacade;
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clientHandlers;
    private int clientCounter;
    
    public LibraryServer(int port, LibraryFacade libraryFacade) {
        this.port = port;
        this.libraryFacade = libraryFacade;
        this.clientHandlers = new ArrayList<>();
        this.clientCounter = 0;
    }
    
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            System.out.println("Library Server started on port " + port);
            System.out.println("Server address: " + serverSocket.getInetAddress());
            System.out.println("Listening on all interfaces (0.0.0.0:" + port + ")");
            System.out.println("Waiting for client connections...");
            
            // Continuously accept clients in a loop
            while (true) {
                acceptClient();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeServer();
        }
    }
    
    public void acceptClient() {
        try {
            Socket clientSocket = serverSocket.accept();
            clientCounter++;
            
            System.out.println("[Client #" + clientCounter + "] Connected");
            
            // Create handler and start new thread
            ClientHandler handler = new ClientHandler(clientSocket, clientCounter);
            clientHandlers.add(handler);
            Thread thread = new Thread(handler);
            thread.start();
            
        } catch (IOException e) {
            System.err.println("Error accepting client: " + e.getMessage());
        }
    }
    
    public void closeServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server stopped");
            }
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }
}
