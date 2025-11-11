package library;

import java.util.ArrayList;

public class CD implements Resource {
    private String albumName;
    private String artist;
    private int numSongs;
    private Boolean isAvailable;
    private ArrayList<Log> checkoutHistory;
    
    @Override
    public ArrayList<Log> getLogs() {
        return checkoutHistory;
    }
    
    @Override
    public void addLog(Log log) {
        // TODO: Implement addLog for CD
    }
    
    @Override
    public String getDetails() {
        // TODO: Implement CD details string
        return "";
    }
    
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
    
    @Override
    public void setAvailability(boolean availability) {
        isAvailable = availability;
    }

    @Override
    public String getDisplayName() {
        return albumName;
    }
}
