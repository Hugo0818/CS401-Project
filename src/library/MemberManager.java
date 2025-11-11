package library;

import java.util.ArrayList;

public class MemberManager {
    private ArrayList<Member> memberList;
    private String filepath;

    public MemberManager(String filepath) {
        this.filepath = filepath;
        this.memberList = new ArrayList<>();
        if (this.filepath != null) loadFromDisk();
    }

    public void addMember(Member member) {
        memberList.add(member);
    }
    
    public boolean removeMember(Member member) {
        return memberList.remove(member);
    }
    
    public Member getMember(int index) {
        if (index < 0 || index >= memberList.size()) {
            return null;
        }
        return memberList.get(index);
    }
    
    public ArrayList<Member> searchMembers(String query) {
        ArrayList<Member> results = new ArrayList<>();
        for (Member member : memberList) {
            if (member.getName().toLowerCase().contains(query.toLowerCase()) ||
                member.getUID().toLowerCase().contains(query.toLowerCase())) {
                results.add(member);
            }
        }
        return results;
    }
    
    public void saveChanges() {
        try (java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(filepath))) {
            out.writeObject(memberList);
        } catch (Exception e) {
            // Optionally log or handle the error
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromDisk() {
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(filepath))) {
            memberList = (ArrayList<Member>) in.readObject();
        } catch (Exception e) {
            memberList = new ArrayList<>(); // fallback to empty list if file not found or error
        }
    }
}
