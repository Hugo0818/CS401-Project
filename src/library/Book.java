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
        checkoutHistory.add(log);
    }
    
    
    @Override
    public String getDetails() {
        // TODO: Implement book details string
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
        return title;
    }
}
