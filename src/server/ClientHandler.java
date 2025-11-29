package server;

import library.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private int clientId;
    private Staff loggedInStaff;
    private LibraryFacade facade;
    
    
    public ClientHandler(Socket socket, int clientId, LibraryFacade lf) {
        this.socket = socket;
        this.clientId = clientId;
        this.loggedInStaff = null;
        this.facade = lf;
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
                processMessage(receivedMessage);
                
           
                
                // Check for disconnect message
                if (receivedMessage.getType() == MessageType.LOGOUT_ATTEMPT) {
                    break;
                }
            }
            
        } catch (Exception e) {
            System.err.println("[Client #" + clientId + "] Error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    
    public void processMessage(Message message) {
        
        switch (message.getType()) {
	        case LOGIN_ATTEMPT -> handleLogin(message);
	        case LOGOUT_ATTEMPT -> handleLogout(message);
	        case SIGNUP_ATTEMPT -> handleSignup(message);
	        case CATALOG_SEARCH_REQ -> handleCatalogSearch(message);
	        case CATALOG_VIEW_REQ -> handleCatalogView(message);
	        case CHECK_IN_REQ -> handleCheckIn(message);
	        case CHECK_OUT_REQ -> handleCheckOut(message);
	        default -> {
	            // default response
	            return;
        }
        
        }
    }
    
    
    private void handleLogin(Message message) {
    	
    	
    	//check the username and password before they proceed to the next page in GUI
    	////
    	
    	//return a new message wheter the info was valid or not valid
    	//for now it returns true
    	sendMessage(new Message(MessageType.LOGIN_SUCCESS, "The Found Client"));
    	
    	//or return login fail

    }
    
    private void handleLogout(Message message) {
    	 sendMessage(new Message(MessageType.LOGOUT_RESPONSE, "Disconnected from server"));

    }
    
    private void handleSignup(Message message) {
    	LoginInfo info = (LoginInfo) message.getContent();
    	String username = info.getUsername();
    	//check if the user exists
    	
  
    	sendMessage(new Message(MessageType.SIGNUP_ATTEMPT, "User sign up"));

   }
    
    private void handleCatalogSearch(Message message) {
    	System.out.println("catalog search being done");
    	String searching = (String) message.getContent();
    	
    	//search the catalog for the item by title and return it to client
    	ArrayList<Resource> results = facade.searchCatalog(searching);
    	sendMessage(new Message(MessageType.CATALOG_S_RES, results));
      	 

    }
    
    private void handleCatalogView(Message message) {
		
    
    }
    
    private void handleCheckIn(Message message) {
	
    }
    
    private void handleCheckOut(Message message) {

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
