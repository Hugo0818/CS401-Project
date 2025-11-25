package client;
import java.io.*;

import library.Message;
import library.MessageType;

import static util.DebugUtil.getCallerInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket connection;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private String host;
    private int port;
    private String clientType; //use in constructor later?
    
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        connectToServer();
        
    }
    
    
    //main for different clients 
    public static void main(String[] args) {
    	//for now using my default, can change to whatever address you use
    	Client client = new Client("127.0.0.1", 12345);
    	Thread clientThread = new Thread(client);
    	clientThread.start();
    }
    
    
    
    @Override
	public void run() {
    	
    	//Start the GUI here
    	
  
		
		//While the thread is running
		while(true) {
			
			//Wait for server response based on GUI buttons
			Message server_response = receiveMessage();
			
			if(server_response != null) {
				//Process response
				processMessage(server_response);
			}
			
		}	
    	
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
            return message;
        } catch (Exception e) {
            System.err.println("Error receiving message: " + e.getMessage());
            return null;
        }
    }
    
    public void processMessage(Message message) {
        if(message.getType() == MessageType.LOGIN) {
        	//checks if login was successful
        	Object valid = message.getContent();
        	
        	if((boolean) valid) {
        		System.out.println("Client login was successful");
        	}
        	
        	else {
        		System.out.println("Client login was not successful");
        	}
        	
        }
        System.out.println("Received from server:");
        System.out.println("  Type: " + message.getType());
        System.out.println("  Content: " + message.getContent());
    }
    
    public void sendMessage(Message message) {
        try {
            oStream.writeObject(message);
            oStream.flush();
            System.out.println("[DEBUG] " + getCallerInfo() + " Sent message: " + message.getType());
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
