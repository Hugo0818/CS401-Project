package client;

import library.*;
import library.Message;
import library.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
    private Resource lastCheckoutResource;
    private Resource lastCheckinResource;
    private String lastCheckoutMemberUid;
    
    public static void main(String[] args) {
    	//127.0.0.1
        Client client = new Client("127.0.0.1", 12345); // or your server port
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
   
            case LOGIN_RESPONSE -> {
            	//if ok is true show the corresponding dashboard
            	if(msg.isOk()) {
            		Object payload = msg.getPayload(); //staff or member
            		
            		if(payload instanceof Staff staff) {
            			gui.showStaffDashboard();
            			
            		}            		
            		else {
            			gui.showMemberDashboard();
            		}
            		return true;            		
            	}            	
            	else {
            		gui.showError("Login failed: " + msg.getInfo());
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

            case MEMBER_BORROWED_RES -> {
                gui.handleMemberBorrowedResults(msg.getPayload());
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
                    // Update availability locally and add to borrowed cache
                    if (lastCheckoutResource != null) {
                        gui.updateResourceAvailability(lastCheckoutResource, false);
                        if (lastCheckoutMemberUid != null) {
                            gui.addToBorrowedCache(lastCheckoutMemberUid, lastCheckoutResource);
                        }
                    }
                } else {
                    gui.showError("Checkout failed: " + msg.getInfo());
                }
                return true;
            }
            case CHECK_IN_RES -> {
                if (msg.isOk()) {
                    gui.showInfo("Resource checked in successfully!");
                    // Update availability locally and refresh borrowed list
                    if (lastCheckinResource != null) {
                        gui.updateResourceAvailability(lastCheckinResource, true);
                    }
                    gui.refreshMemberBorrowed();
                } else {
                    gui.showError("Check-in failed: " + msg.getInfo());
                }
                return true;
            }

            
            case REMOVE_MEMBER_RES -> {
                gui.handleRemoveMember(msg.getPayload());
                return true;
            }
            
            case LOGS_RES -> {
                if (msg.isOk() && msg.getPayload() instanceof ArrayList<?>) {
                    @SuppressWarnings("unchecked")
                    ArrayList<Log> logs = (ArrayList<Log>) msg.getPayload();
                    gui.handleLogsResponse(logs);
                } else {
                    gui.showError("Failed to fetch logs: " + msg.getInfo());
                }
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
        
		return false;
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
    
    public void setLastCheckoutResource(Resource resource) {
        this.lastCheckoutResource = resource;
    }
    
    public void setLastCheckinResource(Resource resource) {
        this.lastCheckinResource = resource;
    }
    
    public void setLastCheckoutMemberUid(String memberUid) {
        this.lastCheckoutMemberUid = memberUid;
    }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
