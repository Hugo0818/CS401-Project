package server;

import library.Message;
import library.Staff;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private int clientId;
    private Staff loggedInStaff;
    
    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
        this.loggedInStaff = null;
    }
    
    @Override
    public void run() {
        try {
            // Create output stream first
            oStream = new ObjectOutputStream(socket.getOutputStream());
            oStream.flush();
            // Then create input stream
            iStream = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("[Client #" + clientId + "] Handler started");
            
            // Communication loop
            while (true) {
                Message receivedMessage = (Message) iStream.readObject();
                System.out.println("[Client #" + clientId + "] Received " + receivedMessage.getType() + " message");
                
                // Process message and get response
                Message response = processMessage(receivedMessage);
                
                // Send response
                sendMessage(response);
                
                // Check for disconnect message
                if ("DISCONNECT".equalsIgnoreCase(receivedMessage.getType())) {
                    break;
                }
            }
            
        } catch (Exception e) {
            System.err.println("[Client #" + clientId + "] Error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    public Message processMessage(Message message) {
        // TODO: Implement full message processing logic
        // For now, echo back the message
        String type = message.getType();
        
        if ("DISCONNECT".equalsIgnoreCase(type)) {
            return new Message("DISCONNECT", "Disconnected from server");
        }
        
        // Default echo response
        return new Message(type, "Message received: " + message.getContent());
    }
    
    public void sendMessage(Message message) {
        try {
            oStream.writeObject(message);
            oStream.flush();
            System.out.println("[Client #" + clientId + "] Sent " + message.getType() + " response");
        } catch (IOException e) {
            System.err.println("[Client #" + clientId + "] Error sending message: " + e.getMessage());
        }
    }
    
    private void closeConnection() {
        try {
            if (iStream != null) iStream.close();
            if (oStream != null) oStream.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("[Client #" + clientId + "] Disconnected");
        } catch (IOException e) {
            System.err.println("[Client #" + clientId + "] Error closing connection: " + e.getMessage());
        }
    }
}
