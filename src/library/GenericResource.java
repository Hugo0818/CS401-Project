package library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;

public class GenericResource implements Resource, Serializable {
    private Map<String, String> extraDetails;
    private String resourceName;
    private Boolean isAvailable;
    private ArrayList<Log> checkoutHistory;
    
    public GenericResource(String resourceName, Map<String, String> extraDetails) {
        this.resourceName = resourceName;
        this.extraDetails = extraDetails != null ? extraDetails : new HashMap<>();
        this.isAvailable = true;
        this.checkoutHistory = new ArrayList<>();
    }
    
    @Override
    public ArrayList<Log> getLogs() {
        return checkoutHistory;
    }
    
    @Override
    public void addLog(Log log) {
        checkoutHistory.add(log);
    }
    

    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        for (Map.Entry<String, String> entry : extraDetails.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            details.append(key).append(": ").append(value).append("\n");
        }
        return details.toString();
    }
    
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
    
    @Override
    public void setCheckedOut(boolean availability) {
        isAvailable = availability;
    }

    @Override
    public String getDisplayName() {
        return resourceName;
    }
}
