package library;

import java.util.ArrayList;
import java.util.Map;

public class GenericResource implements Resource {
    private Map<String, String> extraDetails;
    private String resourceName;
    private Boolean isAvailable;
    private ArrayList<Log> checkoutHistory;
    
    @Override
    public ArrayList<Log> getLogs() {
        return checkoutHistory;
    }
    
    @Override
    public void addLog(Log log) {
        
    }
    
    @Override
    public String getName() {
        return resourceName;
    }
    
    @Override
    public String getDetails() {
        return "";
    }
    
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
    
    @Override
    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }
    
    @Override
    public int compareTo(Resource other) {
        return 0;
    }
}
