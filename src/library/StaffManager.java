package library;

import java.util.ArrayList;
import java.util.EnumSet;

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
        // TODO: Implement staff search logic
        return null;
    }
    
    public void setPermissions(Staff staff, EnumSet<Permissions> permissions) {
        // TODO: Implement setPermissions logic
    }
    
    public EnumSet<Permissions> getPermissions(Staff staff) {
        // TODO: Implement getPermissions logic
        return null;
    }
    
    public boolean hasPermission(Permissions permission) {
        // TODO: Implement hasPermission logic
        return false;
    }
    
}
