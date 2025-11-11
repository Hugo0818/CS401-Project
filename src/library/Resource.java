package library;

import java.util.ArrayList;

public interface Resource extends Comparable<Resource> {
    ArrayList<Log> getLogs();
    void addLog(Log log);
    String getDetails();
    boolean isAvailable();
    void setAvailability(boolean availability);
    String getDisplayName();
    
    @Override
    default int compareTo(Resource other) {
        return getDisplayName().compareTo(other.getDisplayName());
    }
}
