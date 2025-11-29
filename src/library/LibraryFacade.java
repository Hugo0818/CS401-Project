//package library;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//public class LibraryFacade {
//    private StaffManager staffManager;
//    private ResourceManager resourceManager;
//    private MemberManager memberManager;
//    private LogManager logManager;
//    
//    // StaffManager passthrough methods 
//    public void addStaff(Staff staff) {
//        staffManager.addStaff(staff);
//    }
//    
//    public void removeStaff(Staff staff) {
//        staffManager.removeStaff(staff);
//    }
//    
//    public Staff getStaff(int index) {
//        return staffManager.getStaff(index);
//    }
//    
//    public boolean setStaff(Staff original, Staff updated) {
//        return staffManager.setStaff(original, updated);
//    }
//    
//    public ArrayList<Staff> searchStaff(String query) {
//        return staffManager.searchStaff(query);
//    }
//    
//    // ResourceManager passthrough methods
//    public ArrayList<Resource> searchCatalog(String query) {
//        return resourceManager.searchCatalog(query);
//    }
//    
//    public Boolean addResource(Resource resource) {
//        return resourceManager.addResource(resource);
//    }
//    
//    public Boolean editResource(Resource original, Resource updated) {
//        return resourceManager.editResource(original, updated);
//    }
//    
//    public Boolean removeResource(Resource resource) {
//        return resourceManager.removeResource(resource);
//    }
//    
//    public Boolean checkoutResource(Resource resource, Member member) {
//        return resourceManager.checkoutResource(resource, member);
//    }
//    
//    public Boolean checkinResource(Resource resource, Member member) {
//        return resourceManager.checkinResource(resource, member);
//    }
//    
//    // MemberManager passthrough methods
//    public void addMember(Member member) {
//        memberManager.addMember(member);
//    }
//    
//    public void removeMember(Member member) {
//        memberManager.removeMember(member);
//    }
//    
//    public Member getMember(int index) {
//        return memberManager.getMember(index);
//    }
//    
//    public ArrayList<Member> searchMembers(String query) {
//        return memberManager.searchMembers(query);
//    }
//    
//    // LogManager passthrough methods
//    public ArrayList<Log> getRecentLogs() {
//        return logManager.getRecentLogs();
//    }
//    
//    public ArrayList<Log> getLogsByDate(Date date) {
//        return logManager.getLogsByDate(date);
//    }
//    
//    // SaveChanges methods for all managers
//    public void saveChanges() {
//        // TODO: Implement saveChanges to serialize all four lists to a single .ser file, preserving shared references.
//        // Write all four lists to the same ObjectOutputStream (same file) in sequence.
//        // Read them back in the same order. Can't skip objects when reading. Use in.readObject() to iterate through them.
//    }
//
//    public LibraryFacade(String serFilePath) {
//        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(serFilePath))) {
//            @SuppressWarnings("unchecked")
//            ArrayList<Staff> staffList = (ArrayList<Staff>) in.readObject();
//            @SuppressWarnings("unchecked")
//            ArrayList<Resource> catalog = (ArrayList<Resource>) in.readObject();
//            @SuppressWarnings("unchecked")
//            ArrayList<Member> memberList = (ArrayList<Member>) in.readObject();
//            @SuppressWarnings("unchecked")
//            ArrayList<Log> logList = (ArrayList<Log>) in.readObject();
//            this.staffManager = new StaffManager(staffList);
//            this.resourceManager = new ResourceManager(catalog);
//            this.memberManager = new MemberManager(memberList);
//            this.logManager = new LogManager(logList);
//        } catch (Exception e) {
//            // Optionally handle or log the error
//            this.staffManager = new StaffManager(new ArrayList<>());
//            this.resourceManager = new ResourceManager(new ArrayList<>());
//            this.memberManager = new MemberManager(new ArrayList<>());
//            this.logManager = new LogManager(new ArrayList<>());
//        }
//    }
//}
package library;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class LibraryFacade {
    private StaffManager staffManager;
    private ResourceManager resourceManager;
    private MemberManager memberManager;
    private LogManager logManager;

    private final String serFilePath;

    // ---------------------------
    // Constructor
    // ---------------------------
    public LibraryFacade(String serFilePath) {
        this.serFilePath = serFilePath;

        // Attempt to load .ser file
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(serFilePath))) {

            System.out.println("[LibraryFacade] Loading data from " + serFilePath);

            @SuppressWarnings("unchecked")
            ArrayList<Staff> staffList = (ArrayList<Staff>) in.readObject();
            @SuppressWarnings("unchecked")
            ArrayList<Resource> resources = (ArrayList<Resource>) in.readObject();
            @SuppressWarnings("unchecked")
            ArrayList<Member> memberList = (ArrayList<Member>) in.readObject();
            @SuppressWarnings("unchecked")
            ArrayList<Log> logList = (ArrayList<Log>) in.readObject();

            this.staffManager = new StaffManager(staffList);
            this.resourceManager = new ResourceManager(resources);
            this.memberManager = new MemberManager(memberList);
            this.logManager = new LogManager(logList);

        } catch (Exception e) {
            // File does NOT exist or is corrupted → start empty
            System.out.println("[LibraryFacade] No existing data found. Starting with empty lists.");

            this.staffManager = new StaffManager(new ArrayList<>());
            this.resourceManager = new ResourceManager(new ArrayList<>());
            this.memberManager = new MemberManager(new ArrayList<>());
            this.logManager = new LogManager(new ArrayList<>());
        }
    }

    // ---------------------------
    // Staff passthrough methods
    // ---------------------------
    public void addStaff(Staff staff) {
        staffManager.addStaff(staff);
        saveChanges();
    }

//    public Staff findStaffByUsername(String username) {
//        return staffManager.findByUsername(username);
//    }
//
//    public Staff findStaffByUID(int uid) {
//        return staffManager.findByUID(uid);
//    }

    public ArrayList<Staff> searchStaff(String q) {
        return staffManager.searchStaff(q);
    }

    // ---------------------------
    // Member passthrough methods
    // ---------------------------
    public void addMember(Member member) {
        memberManager.addMember(member);
        saveChanges();
    }

//    public Member findMemberByUsername(String username) {
//        return memberManager.findByUsername(username);
//    }
//
//    public Member findMemberByUID(int uid) {
//        return memberManager.findByUID(uid);
//    }

    public ArrayList<Member> searchMembers(String query) {
        return memberManager.searchMembers(query);
    }

    // ---------------------------
    // Resource passthrough methods
    // ---------------------------
    public ArrayList<Resource> searchCatalog(String query) {
        return resourceManager.searchCatalog(query);
    }

    public boolean addResource(Resource resource) {
        boolean ok = resourceManager.addResource(resource);
        if (ok) saveChanges();
        return ok;
    }

    public boolean editResource(Resource original, Resource updated) {
        boolean ok = resourceManager.editResource(original, updated);
        if (ok) saveChanges();
        return ok;
    }

    public boolean removeResource(Resource resource) {
        boolean ok = resourceManager.removeResource(resource);
        if (ok) saveChanges();
        return ok;
    }

    public boolean checkoutResource(Resource resource, Member member) {
        boolean ok = resourceManager.checkoutResource(resource, member);
        if (ok) saveChanges();
        return ok;
    }

    public boolean checkinResource(Resource resource, Member member) {
        boolean ok = resourceManager.checkinResource(resource, member);
        if (ok) saveChanges();
        return ok;
    }

    // ---------------------------
    // Logs
    // ---------------------------
    public ArrayList<Log> getRecentLogs() {
        return logManager.getRecentLogs();
    }

    public ArrayList<Log> getLogsByDate(Date d) {
        return logManager.getLogsByDate(d);
    }

    // ---------------------------
    // Save Changes
    // ---------------------------
    public void saveChanges() {
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serFilePath))) {
//
//            System.out.println("[LibraryFacade] Saving data to " + serFilePath);
//
//            out.writeObject(staffManager.getAll());
//            out.writeObject(resourceManager.getAll());
//            out.writeObject(memberManager.getAll());
//            out.writeObject(logManager.getAll());
//
//        } catch (Exception e) {
//            System.err.println("[LibraryFacade] Error saving data: " + e.getMessage());
//        }
    }
}
