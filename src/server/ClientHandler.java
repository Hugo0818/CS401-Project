package server;

import library.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Client-side handler. Uses LibraryFacade for business logic (login/signup/search/checkout).
 *
 * Expected LibraryFacade methods used by this handler:
 *   Member facade.loginMember(String uid, String password)
 *   String    facade.signupMember(String name, String password)  // returns UID or null/error
 *   Staff    facade.loginStaff(String uid, String password)
 *   String   facade.signupStaff(String name, String password)
 *   ArrayList<Resource> facade.searchCatalog(String query)
 *   Member   facade.getMemberByUID(String uid) // optional for member info
 *
 * If your facade uses different method names, adapt the calls accordingly.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final int clientId;
    private final LibraryFacade facade;
    private final LibraryServer server;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    // track logged user (could be Staff or Member)
    private Staff loggedStaff = null;
    private Member loggedMember = null;

    public ClientHandler(Socket socket, int clientId, LibraryFacade facade, LibraryServer server) {
        this.socket = socket;
        this.clientId = clientId;
        this.facade = facade;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("[Handler#" + clientId + "] Started");

            while (!socket.isClosed()) {
                Message msg = (Message) in.readObject();
                if (msg == null) break;
                System.out.println("[Handler#" + clientId + "] Received message: " + msg.getType());
                processMessage(msg);
                if (msg.getType() == MessageType.W_CLOSED) break;
            }
        } catch (EOFException eof) {
            System.out.println("[Handler#" + clientId + "] Client disconnected.");
        } catch (Exception e) {
            System.err.println("[Handler#" + clientId + "] Error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void processMessage(Message msg) {
        System.out.println("[Handler#" + clientId + "] Processing message: " + msg.getType());
        try {
            switch (msg.getType()) {
                case LOGIN_ATTEMPT -> handleLogin(msg); 
                case SIGNUP_ATTEMPT -> handleSignup(msg);
                case W_CLOSED -> {
                    sendMessage(Message.ok(MessageType.W_CLOSED, "Goodbye"));
                    closeConnection();
                }
                
                case CATALOG_SEARCH_REQ -> handleCatalogSearch(msg);
                case MEMBER_SEARCH_REQ -> handleMemberSearch(msg);
                case ADD_RESOURCE_REQ -> handleAddResource(msg);
                case REMOVE_RESOURCE_REQ -> handleRemoveResource(msg);
                case CHECK_OUT_REQ -> handleCheckout(msg);
                case CHECK_IN_REQ -> handleCheckin(msg);
                case LOGOUT_ATTEMPT -> handleLogout(msg);
                default -> sendMessage(Message.fail(MessageType.ERROR, "Unknown message type: " + msg.getType()));
            }
        } catch (Exception ex) {
            sendMessage(Message.fail(MessageType.ERROR, "Server error: " + ex.getMessage()));
        }
    }

    // LOGIN: payload is LoginInfo (uidOrName,password,isStaff) for login attempts
    private void handleLogin(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof LoginInfo info)) {
            sendMessage(Message.fail(MessageType.LOGIN_RESPONSE, "Invalid login payload"));
            return;
        } else {
            System.out.println("[Handler#" + clientId + "] Login attempt for " + info.getUidOrName() + " as " + (info.isStaff() ? "Staff" : "Member"));
        }
        
        
        if (info.isStaff()) {
            // Look for the staff member
            Staff searchedStaff = facade.findStaffByUsername(info.getUidOrName());
            if(searchedStaff == null) {
            	sendMessage(Message.fail(MessageType.LOGIN_RESPONSE, "This username does not exist"));
            }
            else {
            	//passwords match
            	if(searchedStaff.getPassword().equals(info.getPassword())) {
            		loggedStaff = searchedStaff;
            		sendMessage(Message.ok(MessageType.LOGIN_RESPONSE, searchedStaff));
            		System.out.println("[Handler#" + clientId + "] Staff login success: " + searchedStaff.getName());
            	}
            	
            	//passwords don't match
            	else {
            		sendMessage(Message.fail(MessageType.LOGIN_RESPONSE, "Invalid password"));
            		
            	}            		
            }                                 
        } else {
            Member searchedMember = facade.findMemberByUsername(info.getUidOrName());
            if(searchedMember == null) {
            	sendMessage(Message.fail(MessageType.LOGIN_RESPONSE, "This username does not exist"));
            }           
            else {
            	//passwords match
            	if(searchedMember.getpassword().equals(info.getPassword())) {
            		loggedMember = searchedMember;
            		sendMessage(Message.ok(MessageType.LOGIN_RESPONSE, searchedMember));
            		System.out.println("[Handler#" + clientId + "] Member login success: " + searchedMember.getName());
            	}            	
            	//passwords don't match
            	else {
            		sendMessage(Message.fail(MessageType.LOGIN_RESPONSE, "Invalid password"));
            		
            	}            	
            }     
        }
    }
    

    // SIGNUP: payload is LoginInfo where uidOrName is name when signing up
    private void handleSignup(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof LoginInfo info)) {
            sendMessage(Message.fail(MessageType.SIGNUP_RESPONSE, "Invalid signup payload"));
            return;
        }

        if (info.isStaff()) {
        	//check if the username exists 
            Staff newStaff = facade.findStaffByUsername(info.getUidOrName());
            //new staff passed as message to be created
            if (newStaff == null) {
            	newStaff = new Staff(info); //create new staff with username and password
            	facade.addStaff(newStaff); //add to the staff list
            	System.out.print("Staff signup success"); //DEBUG MSG
                sendMessage(Message.ok(MessageType.SIGNUP_RESPONSE, newStaff.getUID()));
                //if the usernames exists
            } else {
                sendMessage(Message.fail(MessageType.SIGNUP_RESPONSE, "This username is taken"));
            }
            
         
         //same logic as staff, check if the member exists and create new one if not
        } else {
            Member newMember = facade.findMemberByUsername(info.getUidOrName());
            if (newMember == null) {
            	newMember = new Member(info); //create new member with info entered
            	facade.addMember(newMember); //add to the member list
            	System.out.print("Member signup success"); //DEBUG MSG
                sendMessage(Message.ok(MessageType.SIGNUP_RESPONSE, newMember.getUID()));
            } else {
                sendMessage(Message.fail(MessageType.SIGNUP_RESPONSE, "This username is taken"));
            }
        }
    }

    private void handleCatalogSearch(Message msg) {    	       
        String ressourceTitle = (String) msg.getPayload();        
        ArrayList<Resource> results = facade.searchCatalog(ressourceTitle);
        if(results.size() > 0) {
        	//send the list showing available copies
        	sendMessage(Message.ok(MessageType.CATALOG_SEARCH_RES, results));
        	}
        else {
        	//send a string meaning not found
        	sendMessage(Message.fail(MessageType.CATALOG_SEARCH_RES, "unavailable"));
        }
    }

    
    private void handleMemberSearch(Message msg) {
        Object p = msg.getPayload();
        String q = (p instanceof String) ? (String) p : "";
        ArrayList<Member> results = facade.searchMembers(q); // assume facade exposes this
        sendMessage(Message.ok(MessageType.MEMBER_SEARCH_RES, results));
    }

    private void handleLogout(Message msg) {
        sendMessage(Message.ok(MessageType.LOGOUT_RESPONSE, "Goodbye"));
    }
    
    private void handleAddResource(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof Resource resource)) {
            sendMessage(Message.fail(MessageType.ADD_RESOURCE_RES, "Invalid resource payload"));
            return;
        }
        
        // Check if user is staff
        if (loggedStaff == null) {
            sendMessage(Message.fail(MessageType.ADD_RESOURCE_RES, "Only staff can add resources"));
            return;
        }
        
        boolean success = facade.addResource(resource);
        if (success) {
            System.out.println("[Handler#" + clientId + "] Resource added: " + resource.getDisplayName());
            sendMessage(Message.ok(MessageType.ADD_RESOURCE_RES, "Resource added successfully"));
        } else {
            sendMessage(Message.fail(MessageType.ADD_RESOURCE_RES, "Failed to add resource"));
        }
    }
    
    private void handleRemoveResource(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof Resource resource)) {
            sendMessage(Message.fail(MessageType.REMOVE_RESOURCE_RES, "Invalid resource payload"));
            return;
        }
        
        // Check if user is staff
        if (loggedStaff == null) {
            sendMessage(Message.fail(MessageType.REMOVE_RESOURCE_RES, "Only staff can remove resources"));
            return;
        }
        
        boolean success = facade.removeResource(resource);
        if (success) {
            System.out.println("[Handler#" + clientId + "] Resource removed: " + resource.getDisplayName());
            sendMessage(Message.ok(MessageType.REMOVE_RESOURCE_RES, "Resource removed successfully"));
        } else {
            sendMessage(Message.fail(MessageType.REMOVE_RESOURCE_RES, "Failed to remove resource"));
        }
    }
    
    private void handleCheckout(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof java.util.Map)) {
            sendMessage(Message.fail(MessageType.CHECK_OUT_RES, "Invalid checkout payload"));
            return;
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> data = (java.util.Map<String, Object>) p;
        String memberUid = (String) data.get("memberUid");
        Resource resource = (Resource) data.get("resource");
        
        if (memberUid == null || resource == null) {
            sendMessage(Message.fail(MessageType.CHECK_OUT_RES, "Missing member UID or resource"));
            return;
        }
        
        // Check if user is staff
        if (loggedStaff == null) {
            sendMessage(Message.fail(MessageType.CHECK_OUT_RES, "Only staff can checkout resources"));
            return;
        }
        
        // Find member by UID
        Member member = null;
        try {
            // Extract numeric part from UID (e.g., "M123" -> 123)
            if (memberUid.startsWith("M")) {
                int uid = Integer.parseInt(memberUid.substring(1));
                member = facade.findMemberByUID(uid);
            } else {
                // Try to find by username if not a UID format
                member = facade.findMemberByUsername(memberUid);
            }
        } catch (NumberFormatException e) {
            // Try as username
            member = facade.findMemberByUsername(memberUid);
        }
        
        if (member == null) {
            sendMessage(Message.fail(MessageType.CHECK_OUT_RES, "Member not found: " + memberUid));
            return;
        }
        
        boolean success = facade.checkoutResource(resource, member);
        if (success) {
            System.out.println("[Handler#" + clientId + "] Checkout: " + resource.getDisplayName() + " to " + member.getName());
            sendMessage(Message.ok(MessageType.CHECK_OUT_RES, "Checked out successfully"));
        } else {
            sendMessage(Message.fail(MessageType.CHECK_OUT_RES, "Checkout failed - resource may not be available"));
        }
    }
    
    private void handleCheckin(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof java.util.Map)) {
            sendMessage(Message.fail(MessageType.CHECK_IN_RES, "Invalid check-in payload"));
            return;
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> data = (java.util.Map<String, Object>) p;
        String memberUid = (String) data.get("memberUid");
        Resource resource = (Resource) data.get("resource");
        
        if (memberUid == null || resource == null) {
            sendMessage(Message.fail(MessageType.CHECK_IN_RES, "Missing member UID or resource"));
            return;
        }
        
        // Check if user is staff
        if (loggedStaff == null) {
            sendMessage(Message.fail(MessageType.CHECK_IN_RES, "Only staff can check in resources"));
            return;
        }
        
        // Find member by UID
        Member member = null;
        try {
            // Extract numeric part from UID (e.g., "M123" -> 123)
            if (memberUid.startsWith("M")) {
                int uid = Integer.parseInt(memberUid.substring(1));
                member = facade.findMemberByUID(uid);
            } else {
                // Try to find by username if not a UID format
                member = facade.findMemberByUsername(memberUid);
            }
        } catch (NumberFormatException e) {
            // Try as username
            member = facade.findMemberByUsername(memberUid);
        }
        
        if (member == null) {
            sendMessage(Message.fail(MessageType.CHECK_IN_RES, "Member not found: " + memberUid));
            return;
        }
        
        boolean success = facade.checkinResource(resource, member);
        if (success) {
            System.out.println("[Handler#" + clientId + "] Check-in: " + resource.getDisplayName() + " from " + member.getName());
            sendMessage(Message.ok(MessageType.CHECK_IN_RES, "Checked in successfully"));
        } else {
            sendMessage(Message.fail(MessageType.CHECK_IN_RES, "Check-in failed"));
        }
    }

    public synchronized void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.err.println("[Handler#" + clientId + "] Send failed: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
        server.removeHandler(this);
    }
}
