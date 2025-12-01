package server;

import library.LibraryFacade;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LibraryServer {

    private final String host;
    private final int port;
    private final LibraryFacade facade;
    private ServerSocket serverSocket;
    private volatile boolean running = true;
    private final List<ClientHandler> handlers = new CopyOnWriteArrayList<>();
    private int clientCounter = 0;

    public LibraryServer(String host, int port, LibraryFacade facade) {
        this.host = host;
        this.port = port;
        this.facade = facade;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
            serverSocket.setReuseAddress(true);
            System.out.println("[SERVER] Listening on " + host + ":" + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setReuseAddress(true);
                clientCounter++;
                System.out.println("[SERVER] Client #" + clientCounter + " connected: " + clientSocket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(clientSocket, clientCounter, facade, this);
                handlers.add(handler);
                new Thread(handler, "Handler-" + clientCounter).start();
            }
        } catch (IOException e) {
            if (running) System.err.println("[SERVER] Error: " + e.getMessage());
        } finally {
            stopServer();
        }
    }

    public void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
        System.out.println("[SERVER] Handler removed. Active clients: " + handlers.size());
    }

    public void stopServer() {
        if (!running) return; // Already stopped
        running = false;
        System.out.println("[SERVER] Stopping server and closing all connections...");
        try {
            // Close all client handlers
            for (ClientHandler h : handlers) {
                h.closeConnection();
            }
            handlers.clear();
            
            // Close server socket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("[SERVER] Server socket closed.");
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Stop error: " + e.getMessage());
        }
    }
}
