package library;

import java.util.ArrayList;
import java.util.Date;

public class LogManager {
    private ArrayList<Log> allLogs;

    public LogManager(ArrayList<Log> allLogs) {
        if (allLogs != null) {
            this.allLogs = allLogs;
        } else {
            this.allLogs = new ArrayList<>();
        }
    }

    public void addLog(Log log) {
        allLogs.add(log);
    }
    
    public ArrayList<Log> getRecentLogs() {
        ArrayList<Log> recent = new ArrayList<>(); // Create empty list
        long now = System.currentTimeMillis(); // Gets current time

        long sevenDays = 7L * 24 * 60 * 60 * 1000; // Calculate time

        for (Log log : allLogs) { // Loop
            Date out = log.getCheckOutTime(); // Retrieve checkout time 
            Date in = log.getCheckInTime(); // retrive check in time

            if ((out != null && now - out.getTime() <= sevenDays) || // only check if there is a check out log
                (in != null && now - in.getTime() <= sevenDays)) { 
                recent.add(log); // add log to recent list
            }
        }
        
        return recent;
    }
    
    public ArrayList<Log> getLogsByDate(Date date) {
        ArrayList<Log> results = new ArrayList<>();
        if (date == null) return results;
        for (Log log : allLogs) {
            Date out = log.getCheckOutTime();
            Date in = log.getCheckInTime();
            if (isSameDay(out, date) || isSameDay(in, date)) {
                results.add(log);
            }
        }
        return results;
    }

    private boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        long msPerDay = 24L * 60 * 60 * 1000;
        long day1 = d1.getTime() / msPerDay;
        long day2 = d2.getTime() / msPerDay;
        return day1 == day2;
    }
    
}
