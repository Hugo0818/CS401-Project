package library;

import java.util.ArrayList;

public class Member {
    private String name;
    private String uid;
    private ArrayList<Log> checkoutHistory;
    private ArrayList<Resource> heldResources;
    
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
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setUID(String uid) {
        this.uid = uid;
    }
    
    public void addResourceToPossession(Resource resource) {
        
    }
    
    public void removeResourceFromPossession(Resource resource) {
        
    }
    
    public void addLog(Log log) {
        
    }
    
    public void setLogs(ArrayList<Log> logs) {
        
    }
}
