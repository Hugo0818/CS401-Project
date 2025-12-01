package library;

import java.io.Serializable;
import java.util.ArrayList;

public class Member implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private LoginInfo creds;
    private String uid;
    private String clientType = "Member";
    
    
    private ArrayList<Log> checkoutHistory;
    private ArrayList<Resource> heldResources;
    private static int newID = 1;

    public Member (LoginInfo info) {
    	creds = info;
        this.uid = "M" + newID++; // M for member
        this.checkoutHistory = new ArrayList<>(); // New checkoutHistory array for logs
        this.heldResources = new ArrayList<>(); // New heldResources array for resources currently checked out
    }

    // Getters
    public String getName() {
        return creds.getUidOrName();
    }
    
    public String getpassword() {
    	return creds.getPassword();
    }
    
    public String getUID() {
        return uid;
    }
    
    public String getType() {
    	return clientType;
    }
    
    public ArrayList<Log> getCheckoutHistory() {
        return checkoutHistory;
    }
    
    public ArrayList<Resource> getCurrentlyHeldResources() {
        return heldResources;
    }

    // Setters
    public void setUID(String uid) {
        this.uid = uid;
    }
    
    public void addResourceToPossession(Resource resource) {
        heldResources.add(resource);
    }
    
    public void removeResourceFromPossession(Resource resource) {
        heldResources.remove(resource);
    }
    
    public void addLog(Log log) {
        checkoutHistory.add(log);
    }
    
    public void setLogs(ArrayList<Log> logs) {
        this.checkoutHistory = new ArrayList<>(logs);
    }
    
    public String toString() {
        return "Username: " + getName() + "  —  UID: " + getUID();
    }
}
