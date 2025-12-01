package library;

import java.util.ArrayList;

import java.io.Serializable;

public class Book implements Resource, Serializable {
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
        System.out.println("[Book] isAvailable() called for '" + title + "' - field value: " + isAvailable);
        return isAvailable;
    }
    
    @Override
    public void setCheckedOut(boolean availability) {
        System.out.println("[Book] setCheckedOut(" + availability + ") called for '" + title + "' - old value: " + isAvailable);
        isAvailable = availability;
        System.out.println("[Book] After setCheckedOut - new value: " + isAvailable);
    }

    @Override
    public String getDisplayName() {
        return title;
    }
}

