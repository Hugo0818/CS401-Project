package library;

import java.util.ArrayList;

public class Book implements Resource {
    private String author;
    private String publisher;
    private String isbn;
    private String title;
    private boolean isAvailable;
    private ArrayList<Log> checkoutHistory;
    
    public Book(String title, String author, String publisher, String isbn, boolean isAvailable) {
    	this.title = title;
    	this.author = author;
    	this.publisher = publisher;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
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
        "Title: " + title + "\n" +
        "Author: " + author + "\n" + 
        "Publisher: " + publisher + "\n" + 
        "ISBN: " + isbn + "\n";
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
        return title;
    }
}

