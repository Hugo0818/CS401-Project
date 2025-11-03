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
        
    }
    
    @Override
    public String getName() {
        return albumName;
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
