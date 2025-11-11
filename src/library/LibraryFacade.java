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
    
    public boolean setStaff(Staff original, Staff updated) {
        return staffManager.setStaff(original, updated);
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
    
    public Boolean editResource(Resource original, Resource updated) {
        return resourceManager.editResource(original, updated);
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
        // TODO : Shared references are only preserved within a single serialization operation. All managers should be saved in one .ser file to maintain object references.
        // write all four lists to the same ObjectOutputStream (same file) in sequence
        // read them back in the same order. cant skip objects when reading. use in.readObject() to iterate through them.
    }

    public LibraryFacade(String serFilePath) {
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(serFilePath))) {
            @SuppressWarnings("unchecked")
            ArrayList<Staff> staffList = (ArrayList<Staff>) in.readObject();
            @SuppressWarnings("unchecked")
            ArrayList<Resource> catalog = (ArrayList<Resource>) in.readObject();
            @SuppressWarnings("unchecked")
            ArrayList<Member> memberList = (ArrayList<Member>) in.readObject();
            @SuppressWarnings("unchecked")
            ArrayList<Log> logList = (ArrayList<Log>) in.readObject();
            this.staffManager = new StaffManager(staffList);
            this.resourceManager = new ResourceManager(catalog);
            this.memberManager = new MemberManager(memberList);
            this.logManager = new LogManager(logList);
        } catch (Exception e) {
            // Optionally handle or log the error
            this.staffManager = new StaffManager(new ArrayList<>());
            this.resourceManager = new ResourceManager(new ArrayList<>());
            this.memberManager = new MemberManager(new ArrayList<>());
            this.logManager = new LogManager(new ArrayList<>());
        }
    }
}
