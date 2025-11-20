package library;

import java.util.ArrayList;

public class CD implements Resource {
    private String albumName;
    private String artist;
    private int numSongs;
    private Boolean isAvailable;
    private ArrayList<Log> checkoutHistory;
    
    public CD(String albumName, String artist, int numSongs) {
    	this.albumName = albumName;
    	this.artist = artist;
    	this.numSongs = numSongs;
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
        return 
        "Name: " + albumName + "\n" +
        "Artist: " + artist + "\n" + 
        "Number of Songs: " + numSongs + "\n";
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
