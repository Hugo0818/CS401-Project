package library;

import java.util.Date;

public class Log {
    private Member member;
    private Resource resource;
    private Date checkOutTime;
    private Date checkInTime;

    public Log(Member member, Resource resource) {
        if (member != null && resource != null) {
            checkOut(member, resource);
        }
    }
    
    public Member getMember() {
        return member;
    }
    
    public Resource getResource() {
        return resource;
    }
    
    public Date getCheckOutTime() {
        return checkOutTime;
    }
    
    public Date getCheckInTime() {
        return checkInTime;
    }

    public boolean checkOut(Member member, Resource resource) {
        if (this.checkOutTime == null) {
            this.member = member;
            this.resource = resource;
            this.checkOutTime = new Date();
            return true;
        }
        return false;
    }

    public boolean checkIn() {
        if (member != null && resource != null && checkOutTime != null && checkInTime == null) {
            checkInTime = new Date();
            return true;
        }
        return false;
    }
    
}
