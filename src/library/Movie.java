package library;

import java.util.ArrayList;

public class Movie implements Resource {
    private String director;
    private int runtime;
    private String rating;
    private String title;
    private Boolean isAvailable;
    private ArrayList<Log> checkoutHistory;

    public Movie(String title, String director, int runtime, String rating) {
        this.title = title;
        this.director = director;
        this.runtime = runtime;
        this.rating = rating;
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
        "Title: " + title + "\n" +
        "Director: " + director + "\n" + 
        "Runtime: " + runtime + " mins\n" +
        "Rating: " + rating + "\n";
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
