//package client;
//import java.io.*;
//
//import library.Message;
//import library.MessageType;
//
//import static util.DebugUtil.getCallerInfo;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class Client implements Runnable {
//    private Socket connection;
//    private ObjectInputStream iStream;
//    private ObjectOutputStream oStream;
//    
//    private  final String host;
//    private final int port;
//    
//    private Thread thread;
//    private GUIManager clientGUI;
//    //private String clientType; //use in constructor later?
//    
//    public Client(String host, int port) {
//        this.host = host;
//        this.port = port;
//        //connectToServer();
//        
//    }
//    
//    
//    //main for different clients 
////    public static void main(String[] args) {
////    	//for now using my default, can change to whatever address you use
////    	Client client = new Client("127.0.0.1", 12345);
////    	Thread clientThread = new Thread(client);
////    	clientThread.start();
////    }
//    
//    //Call to start the client thread which will connect and start the GUI
//    public void start() {
//    	thread = new Thread(this, "Client-Listener");
//    	thread.start();
//    }
//    
//    
//    //Runnable entry: connect, create GUI, listen for messages
//    @Override
//	public void run() {
//    	boolean connected;
//    		
//    	//Start the GUI client
//    	clientGUI = new GUIManager();
//    	
//  
//		
//		//While the thread is running
//		while(!Thread.currentThread().isInterrupted()) {
//			
//			//Wait for server response based on GUI buttons
//			
//			//first response should be login attempts
//			Message server_response = receiveMessage();
//			
//			if(server_response == null) {
//				System.out.println("server closed connection");
//				break;
//			}
//			
//			//while connected wait on server responses
//			connected = processMessage(server_response);
//			
//			
//			//Disconnect message was received break out
//			if(!connected) {
//				break;
//			}
//			
//		}
//		closeConnection();
//    	
//    }
//    	
//    
//    
//    public void connectToServer() {
//        try {
//            connection = new Socket(host, port);
//            System.out.println("clientjava Connected to server at " + host + ":" + port);
//            
//            // Create output stream first
//            oStream = new ObjectOutputStream(connection.getOutputStream());
//            oStream.flush();
//            // Then create input stream
//            iStream = new ObjectInputStream(connection.getInputStream());
//            
//        } catch (IOException e) {
//            System.err.println("Error connecting to server: " + e.getMessage());
//        }
//    }
//    
//    
//    public Message receiveMessage() {
//        try {
//            Message message = (Message) iStream.readObject();
//            return message;
//        } catch (Exception e) {
//            System.err.println("Error receiving message: " + e.getMessage());
//            return null;
//        }
//    }
//    
//    public boolean processMessage(Message message) {
//    	
//    	
//    	switch (message.getType()) {
//        case LOGIN_SUCCESS -> {
//        	boolean handling = handleLogin(message);
//        	return handling;
//        }
//        case LOGIN_FAIL -> {
//        	boolean handling = handleLogin(message);
//        	return handling;
//        }
//        case LOGOUT_RESPONSE -> {
//        	boolean handling = handleLogin(message);
//        	return handling;
//        }
//        
//        
//        default -> {
//            // default response
//            return true;
//        }
//    
//    }
//    	
//    	
//        
//
//        
//        
//}
//        
//        
//    
//    
//    private boolean handleLogin(Message message) {
//    	if(message.getType() == MessageType.LOGIN_SUCCESS) {
//    		System.out.println("Client login successful");
//        	//Message content should be the client object
//        	//Depending on the clientTYpe
//        	if(message.getContent().getType() == "Staff") {
//        		clientGUI.showStaffDashboard();
//        	}
//        	
//        	else if(message.getContent().getType() == "Member") {
//        		clientGUI.showMemberDashboard();
//        	}
//        	
//        	return true;
//    		
//    	}
//    	
//    	else if(message.getType() == MessageType.LOGIN_FAIL) {
//    		System.out.println("Client login successful");
//        	return false;
//    		
//    	}
//    	
//    	else if(message.getType() == MessageType.LOGOUT_RESPONSE) {
//    		System.out.println("Client attempting disconnection");
//        	return false;
//    		
//    	}
//    	
//  
//
//    }
//    
//    public void sendMessage(Message message) {
//        try {
//            oStream.writeObject(message);
//            oStream.flush();
//            System.out.println("[DEBUG] " + getCallerInfo() + " Sent message: " + message.getType());
//        } catch (IOException e) {
//            System.err.println("Error sending message: " + e.getMessage());
//        }
//    }
//    
//    public void closeConnection() {
//        try {
//            if (iStream != null) iStream.close();
//            if (oStream != null) oStream.close();
//            if (connection != null && !connection.isClosed()) connection.close();
//            System.out.println("Client disconnected");
//        } catch (IOException e) {
//            System.err.println("Error closing connection: " + e.getMessage());
//        }
//    }
//    
//    public boolean isConnected() {
//        return connection != null && !connection.isClosed();
//    }
//
//
//	
//}
package client;

import library.Message;
import library.MessageType;

import java.io.*;
import java.net.Socket;

/**
 * Threaded client. Construct with host,port; call start() to start its listener thread.
 * GUI should construct Client and pass `this` into GUIManager so GUI can send messages:
 *   Client client = new Client(host, port);
 *   client.start(); // spawns the listener and shows GUI
 */
public class Client implements Runnable {
    private final String host;
    private final int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Thread thread;
    private GUIManager gui; // set when GUIManager constructed
    
    public static void main(String[] args) {
        Client client = new Client("localhost", 12345); // or your server port
        client.start();
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    

    /** Call this to start the client thread which will connect and start the GUI. */
    public void start() {
        thread = new Thread(this, "Client-Listener");
        thread.start();
    }

    /** Runnable entry: connect, create GUI, then listen for messages */
    @Override
    public void run() {
        if (!connect()) {
            System.err.println("[CLIENT] Unable to connect to server.");
            return;
        }

        // create GUI and pass this client so GUI can send messages
        gui = new GUIManager(this);
        gui.showLoginScreen();

        // Listen loop
        try {
            while (!Thread.currentThread().isInterrupted() && socket != null && !socket.isClosed()) {
                Message msg = (Message) in.readObject();
                if (msg == null) break;
                boolean keepRunning = handleServerMessage(msg);
                if (!keepRunning) break;
            }
        } catch (EOFException eof) {
            System.out.println("[CLIENT] Server closed connection");
        } catch (Exception e) {
            System.err.println("[CLIENT] Listener error: " + e.getMessage());
        } finally {
            close();
        }
    }

    private boolean connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("[CLIENT] Connected to " + host + ":" + port);
            return true;
        } catch (IOException e) {
            System.err.println("[CLIENT] Connect failed: " + e.getMessage());
            return false;
        }
    }

    /** Handles incoming messages from server and updates GUI accordingly. */
    private boolean handleServerMessage(Message msg) {
        switch (msg.getType()) {
            case LOGIN_SUCCESS -> {
                // payload: either "STAFF"/"MEMBER" or full object depending on your design
                Object payload = msg.getPayload();
                if (payload instanceof String s) {
                    if (s.equalsIgnoreCase("STAFF")) gui.showStaffDashboard();
                    else gui.showMemberDashboard();
                } else {
                    // fallback: show member dashboard
                    gui.showMemberDashboard();
                }
                return true;
            }
            case LOGIN_FAIL -> {
                gui.showError("Login failed: " + msg.getInfo());
                return true;
            }
            case SIGNUP_SUCCESS -> {
                // payload: UID string
                gui.showInfo("Account created. Your UID: " + msg.getPayload());
                gui.showLoginScreen();
                return true;
            }
            case SIGNUP_FAIL -> {
                gui.showError("Signup failed: " + msg.getPayload());
                return true;
            }
            case CATALOG_SEARCH_RES -> {
                // payload: ArrayList<Resource>
                gui.handleCatalogSearchResults(msg.getPayload());
                return true;
            }
            case MEMBER_SEARCH_RES -> {
                gui.handleMemberSearchResults(msg.getPayload());
                return true;
            }
            case LOGOUT_RESPONSE -> {
                gui.showLoginScreen();
                return true;
            }
            case ERROR -> {
                gui.showError("Server error: " + msg.getInfo());
                return true;
            }
            case PING -> {
                // do nothing
                return true;
            }
            default -> {
                System.out.println("[CLIENT] Unhandled message: " + msg);
                return true;
            }
        }
    }

    /** Thread-safe send */
    public synchronized void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.err.println("[CLIENT] Send failed: " + e.getMessage());
            gui.showError("Network error sending message.");
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public GUIManager getGui() { return gui; }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
