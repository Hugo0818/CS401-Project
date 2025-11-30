package library;

import java.util.ArrayList;

public class MemberManager {
    private ArrayList<Member> memberList;

    public MemberManager(ArrayList<Member> memberList) {
        if (memberList != null) {
            this.memberList = memberList;
        } else {
            this.memberList = new ArrayList<>();
        }
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
    
    public Member findByUsername(String username) {
        for (Member member : memberList) {
            if (member.getName().equals(username)) {
                return member;
            }
        }
        return null;
    }
    
    public Member findByUID(int uid) {
        String uidStr = "M" + uid;
        for (Member member : memberList) {
            if (member.getUID().equals(uidStr)) {
                return member;
            }
        }
        return null;
    }
    
    public ArrayList<Member> getAll() {
        return memberList;
    }
    
}
