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
    private GUIManager clientGUI;
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
    	boolean connected;
    	
    	
    	//Start the GUI client
    	clientGUI = new GUIManager();
    	
  
		
		//While the thread is running
		while(!Thread.currentThread().isInterrupted()) {
			
			//Wait for server response based on GUI buttons
			
			//first response should be login attempts
			Message server_response = receiveMessage();
			
			if(server_response == null) {
				System.out.println("server closed connection");
				break;
			}
			
			//while connected wait on server responses
			connected = processMessage(server_response);
			
			
			//Disconnect message was received break out
			if(!connected) {
				break;
			}
			
		}
		closeConnection();
    	
    }
    	
    
    
    public void connectToServer() {
        try {
            connection = new Socket(host, port);
            System.out.println("clientjava Connected to server at " + host + ":" + port);
            
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
    
    public boolean processMessage(Message message) {
    	
    	
    	switch (message.getType()) {
        case LOGIN_SUCCESS -> {
        	boolean handling = handleLogin(message);
        	return handling;
        }
        case LOGIN_FAIL -> {
        	boolean handling = handleLogin(message);
        	return handling;
        }
        case LOGOUT_RESPONSE -> {
        	boolean handling = handleLogin(message);
        	return handling;
        }
        
        
        default -> {
            // default response
            return true;
        }
    
    }
    	
    	
        

        
        
}
        
        
    
    
    private boolean handleLogin(Message message) {
    	if(message.getType() == MessageType.LOGIN_SUCCESS) {
    		System.out.println("Client login successful");
        	//Message content should be the client object
        	//Depending on the clientTYpe
        	if(message.getContent().getType() == "Staff") {
        		clientGUI.showStaffDashboard();
        	}
        	
        	else if(message.getContent().getType() == "Member") {
        		clientGUI.showMemberDashboard();
        	}
        	
        	return true;
    		
    	}
    	
    	else if(message.getType() == MessageType.LOGIN_FAIL) {
    		System.out.println("Client login successful");
        	return false;
    		
    	}
    	
    	else if(message.getType() == MessageType.LOGOUT_RESPONSE) {
    		System.out.println("Client attempting disconnection");
        	return false;
    		
    	}
    	
  

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
