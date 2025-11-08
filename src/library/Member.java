package library;

import java.util.ArrayList;

public class Member {
    private String name;
    private String uid;
    private ArrayList<Log> checkoutHistory;
    private ArrayList<Resource> heldResources;
    private static int newID = 1;

    public Member (String name) {
        this.name = name;
        this.uid = "M" + newID++; // M for member, don't know if there will be other combinations of ID characters
        this.checkoutHistory = new ArrayList<>; // New checkoutHistory array for logs
        this.heldResources = new ArrayList<>; // New heldResources array for resources currently checked out
    }

    // Getters
    public String getName() {
        return name;
    }
    
    public String getUID() {
        return uid;
    }
    
    public ArrayList<Log> getCheckoutHistory() {
        return checkoutHistory;
    }
    
    public ArrayList<Resource> getCurrentlyHeldResources() {
        return heldResources;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
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
}
