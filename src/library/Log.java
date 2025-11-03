package library;

import java.util.Date;

public class Log {
    private Member member;
    private Resource resource;
    private Date checkOutTime;
    private Date checkInTime;
    
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
}
