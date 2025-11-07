package library;

import java.util.ArrayList;

public interface Resource extends Comparable<Resource> {
    ArrayList<Log> getLogs();
    void addLog(Log log);
    String getName();
    String getDetails();
    boolean isAvailable();
    void setAvailability(boolean availability);
    String getDisplayName();
    
    @Override
    int compareTo(Resource other) {
        return this.getDisplayName().compareTo(other.getDisplayName());
    }
}
