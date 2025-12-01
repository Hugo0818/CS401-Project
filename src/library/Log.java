package library;

import java.util.Date;

public class Log {
    private Member member;
    private Staff staff;
    private Resource resource;
    private String details;
    private Date checkOutTime;
    private Date checkInTime;
    

    //Log constructor for check out/in
    //takes in the person, ressource, and operation
    public Log(Member m, Resource r, MessageType type) {
    	//not actually passing a message here, just a type
    	
    	//If the member was checking out a ressource
    	if(type == MessageType.CHECK_OUT_RES) {
    		member = m; //record the member 
        	resource = r; //record the ressource
        	details = "Member " + member.getUID() + "checked out " + resource.getDisplayName();    		
    	}
    	//else it was a check in
    	else {
    		member = m; //record the member 
        	resource = r; //record the ressource
        	details = "Member " + member.getUID() + "checked in " + resource.getDisplayName();	
    	}    	
    }
    
    
    
    
    
    public Log(Staff staff, Resource ressource) {
    	
    }
    
    public Staff getStaff() {
    	return staff;
    }
    public Member getMember() {
        return member;
    }
    public String getDetails() {
    	return details;
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
