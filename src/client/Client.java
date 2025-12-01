package client;

import library.*;
import library.Message;
import library.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

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
        // Load configuration from config.properties
        String host = "127.0.0.1"; // Default
        int port = 12345; // Default
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/config.properties");
            props.load(fis);
            host = props.getProperty("HOST", "127.0.0.1");
            port = Integer.parseInt(props.getProperty("PORT", "12345"));
            fis.close();
            System.out.println("[CLIENT] Configuration loaded: " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("[CLIENT] Could not load config.properties, using defaults: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("[CLIENT] Invalid PORT in config.properties, using default: 12345");
        }
        
        Client client = new Client(host, port);
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

    public boolean connect() {
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
   
            case LOGIN_RESPONSE -> {
            	//if ok is true show the corresponding dashboard
            	if(msg.isOk()) {
            		Object payload = msg.getPayload(); //staff or member
            		
            		if(payload instanceof Staff staff) {
            			gui.showStaffDashboard();
            		}            		
            		else if(payload instanceof Member member) {
            			gui.showMemberDashboard();
            		}
            		else {
            			// payload is error message string
            			gui.showError("Login failed: " + payload);
            		}
            		return true;            		
            	}            	
            	else {
            		gui.showError("Login failed: " + msg.getInfo());
            		return true;
            	}           
            }
            
            case LOGOUT_RESPONSE -> {
                gui.showLoginScreen();
                return true;
            }
            
            case W_CLOSED -> {
            	return false;
            }
            
            
            case SIGNUP_RESPONSE ->{
            	//if ok is true go back to login
            	if(msg.isOk()) {
            		// payload: UID string
                    gui.showInfo("Account created. Your UID: " + msg.getPayload());
                    gui.showLoginScreen();
                    return true;
            	}            	
            	else {         
            		gui.showError("Signup failed: " + msg.getInfo());
                    return true;
            		
            	}
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
            case ADD_RESOURCE_RES -> {
                if (msg.isOk()) {
                    gui.showInfo("Resource added successfully!");
                } else {
                    gui.showError("Failed to add resource: " + msg.getInfo());
                }
                return true;
            }
            case REMOVE_RESOURCE_RES -> {
                if (msg.isOk()) {
                    gui.showInfo("Resource removed successfully!");
                } else {
                    gui.showError("Failed to remove resource: " + msg.getInfo());
                }
                return true;
            }
            case CHECK_OUT_RES -> {
                if (msg.isOk()) {
                    gui.showInfo("Resource checked out successfully!");
                } else {
                    gui.showError("Checkout failed: " + msg.getInfo());
                }
                return true;
            }
            case CHECK_IN_RES -> {
                if (msg.isOk()) {
                    gui.showInfo("Resource checked in successfully!");
                } else {
                    gui.showError("Check-in failed: " + msg.getInfo());
                }
                return true;
            }
            
            case REMOVE_MEMBER_RES -> {
                gui.handleRemoveMember(msg.getPayload());
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
            System.out.println("[CLIENT] Sending message: " + msg.getType());
            out.writeObject(msg);
            out.flush();
            System.out.println("[CLIENT] Message sent successfully: " + msg.getType());
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
