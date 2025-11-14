package server;

import library.LibraryFacade;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.net.InetAddress;

public class LibraryServer {
    private LibraryFacade libraryFacade;
    private ServerSocket serverSocket;
    private String host;
    private int port;
    private List<ClientHandler> clientHandlers;
    private int clientCounter;

    public LibraryServer(String host, int port, LibraryFacade libraryFacade) {
        this.host = host;
        this.port = port;
        this.libraryFacade = libraryFacade;
        this.clientHandlers = new ArrayList<>();
        this.clientCounter = 0;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
            serverSocket.setReuseAddress(true);
            System.out.println("Library Server started on port " + port);
            System.out.println("Server address: " + host);
            System.out.println("Listening on " + host + ":" + port);
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
