package library;

import java.util.ArrayList;


public class StaffManager {
    private ArrayList<Staff> staffList;

    public StaffManager(ArrayList<Staff> staffList) {
        if (staffList != null) {
            this.staffList = staffList;
        } else {
            this.staffList = new ArrayList<>();
        }
    }
    public void addStaff(Staff staff) {
        // Set UID based on current list size + 1
        int newId = staffList.size() + 1;
        staff.setUID("S" + newId);
        staffList.add(staff);
    }

    public boolean removeStaff(Staff staff) {
        return staffList.remove(staff);
    }
    
    public Staff getStaff(int index) {
        return staffList.get(index);
    }

    public boolean setStaff(Staff original, Staff updated) {
        int index = staffList.indexOf(original);
        if (index != -1) {
            staffList.set(index, updated);
            return true;
        }
        return false;
    }
    
    public ArrayList<Staff> searchStaff(String query) {
        ArrayList<Staff> results = new ArrayList<>();
        for(Staff s : staffList) {
        	if(s.getName().toLowerCase().contains(query.toLowerCase()) || s.getUID().toLowerCase().contains(query.toLowerCase())) {
        		results.add(s);
        	}
        }
        return results;
    }
    
    public Staff findByUsername(String username) {
        for (Staff staff : staffList) {
            if (staff.getName().equals(username)) {
                return staff;
            }
        }
        return null;
    }
    
    public Staff findByUID(int uid) {
        String uidStr = "S" + uid;
        for (Staff staff : staffList) {
            if (staff.getUID().equals(uidStr)) {
                return staff;
            }
        }
        return null;
    }
    
    public ArrayList<Staff> getAll() {
        return staffList;
    }
    
}
