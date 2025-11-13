package client;

import library.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket connection;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private String host;
    private int port;
    
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void connectToServer() {
        try {
            connection = new Socket(host, port);
            System.out.println("Connected to server at " + host + ":" + port);
            
            // Create output stream first
            oStream = new ObjectOutputStream(connection.getOutputStream());
            oStream.flush();
            // Then create input stream
            iStream = new ObjectInputStream(connection.getInputStream());
            
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
    
    public Message receiveMessage() {
        try {
            Message message = (Message) iStream.readObject();
            processMessage(message);
            return message;
        } catch (Exception e) {
            System.err.println("Error receiving message: " + e.getMessage());
            return null;
        }
    }
    
    public void processMessage(Message message) {
        // TODO: Implement full message processing logic
        System.out.println("Received from server:");
        System.out.println("  Type: " + message.getType());
        System.out.println("  Content: " + message.getContent());
    }
    
    public void sendMessage(Message message) {
        try {
            oStream.writeObject(message);
            oStream.flush();
            System.out.println("Sent message: " + message.getType());
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
    
    public void closeConnection() {
        try {
            if (iStream != null) iStream.close();
            if (oStream != null) oStream.close();
            if (connection != null && !connection.isClosed()) connection.close();
            System.out.println("Client disconnected");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public boolean isConnected() {
        return connection != null && !connection.isClosed();
    }
}
