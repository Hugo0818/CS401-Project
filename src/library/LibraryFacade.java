package library;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

public class LibraryFacade {
    private StaffManager staffManager;
    private ResourceManager resourceManager;
    private MemberManager memberManager;
    private LogManager logManager;
    
    // StaffManager passthrough methods
    public void addStaff(Staff staff) {
        staffManager.addStaff(staff);
    }
    
    public void removeStaff(Staff staff) {
        staffManager.removeStaff(staff);
    }
    
    public Staff getStaff(int index) {
        return staffManager.getStaff(index);
    }
    
    public void setStaff() {
        staffManager.setStaff();
    }
    
    public ArrayList<Staff> searchStaff(String query) {
        return staffManager.searchStaff(query);
    }
    
    public void setPermissions(Staff staff, EnumSet<Permissions> permissions) {
        staffManager.setPermissions(staff, permissions);
    }
    
    public EnumSet<Permissions> getPermissions(Staff staff) {
        return staffManager.getPermissions(staff);
    }
    
    public boolean hasPermission(Permissions permission) {
        return staffManager.hasPermission(permission);
    }
    
    // ResourceManager passthrough methods
    public ArrayList<Resource> searchCatalog(String query) {
        return resourceManager.searchCatalog(query);
    }
    
    public Boolean addResource(Resource resource) {
        return resourceManager.addResource(resource);
    }
    
    public Boolean editResource(Resource resource) {
        return resourceManager.editResource(resource);
    }
    
    public Boolean removeResource(Resource resource) {
        return resourceManager.removeResource(resource);
    }
    
    public Boolean checkoutResource(Resource resource, Member member) {
        return resourceManager.checkoutResource(resource, member);
    }
    
    public Boolean checkinResource(Resource resource, Member member) {
        return resourceManager.checkinResource(resource, member);
    }
    
    // MemberManager passthrough methods
    public void addMember(Member member) {
        memberManager.addMember(member);
    }
    
    public void removeMember(Member member) {
        memberManager.removeMember(member);
    }
    
    public Member getMember(int index) {
        return memberManager.getMember(index);
    }
    
    public ArrayList<Member> searchMembers(String query) {
        return memberManager.searchMembers(query);
    }
    
    // LogManager passthrough methods
    public ArrayList<Log> getRecentLogs() {
        return logManager.getRecentLogs();
    }
    
    public ArrayList<Log> getLogsByDate(Date date) {
        return logManager.getLogsByDate(date);
    }
    
    // SaveChanges methods for all managers
    public void saveChanges() {
        staffManager.saveChanges();
        resourceManager.saveChanges();
        memberManager.saveChanges();
        logManager.saveChanges();
    }
}
