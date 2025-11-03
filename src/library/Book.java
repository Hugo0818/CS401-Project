package library;

import java.util.ArrayList;

public class Book implements Resource {
    private String author;
    private String publisher;
    private String isbn;
    private String title;
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
        return title;
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
