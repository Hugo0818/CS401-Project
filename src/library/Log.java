package library;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {
    private Member member;
    private Staff staff;
    private Resource resource;
    private String details;
    private Date checkOutTime;
    private Date checkInTime;
   

    //Log constructor for check out/in
    //From member
    //takes in the person, ressource, and operation
    public Log(Member m, Resource r, MessageType type) {
    	//not actually passing a message here, just a type
    	
    	//If the member was checking out a ressource
    	if(type == MessageType.CHECK_OUT_RES) {
    		member = m; //record the member 
        	resource = r; //record the ressource
        	checkOutTime = new Date(); //record timestamp
        	details = "Member " + member.getUID() + " checked out " + resource.getDisplayName();    		
    	}
    	//else it was a check in
    	else {
    		member = m; //record the member 
        	resource = r; //record the ressource
        	checkInTime = new Date(); //record timestamp
        	details = "Member " + member.getUID() + " checked in " + resource.getDisplayName();	
    	}    	
    }
    
    
    //From staff (MIGHT NOT BE NEEDED if staff checks in/out for the member)
    public Log(Staff s, Resource r, MessageType type) {
    	//not actually passing a message here, just a type
    	
    	//If the member was checking out a ressource
    	if(type == MessageType.CHECK_OUT_RES) {
    		staff = s; //record the member 
        	resource = r; //record the ressource
        	checkOutTime = new Date(); //record timestamp
        	details = "Staff " + staff.getUID() + " checked out " + resource.getDisplayName();    		
    	}
    	//else it was a check in
    	else if(type == MessageType.CHECK_IN_RES) {
    		staff = s; //record the member 
        	resource = r; //record the ressource
        	checkInTime = new Date(); //record timestamp
        	details = "Staff " + staff.getUID() + " checked in " + resource.getDisplayName();	
    	}  
    	
    }
    
    //Adding and removing ressource
    public Log(Resource r, MessageType type) {
    	if(type == MessageType.ADD_RESOURCE_RES){
    		resource = r;
    		details = "New entry added to catalog: " + resource.getDisplayName();
            checkOutTime = new Date();
            checkInTime = new Date();
    	}
    	
    	else if(type == MessageType.REMOVE_RESOURCE_RES) {
    		resource = r;
    		details = "New entry removed from catalog: " + resource.getDisplayName();
            checkOutTime = new Date();
            checkInTime = new Date();
    	}
    }
    
    //Staff log in
    public Log(Staff s, MessageType type, boolean ok) {
    	if(type == MessageType.LOGIN_RESPONSE) {
    		if(ok) {
    			staff = s;
    			details = staff.getUID() + " logged in successfully";
    			staff.setTry(0); 
    		}
    		else {
    			details = "Login attempt failed on " + staff.getUID() + "" + staff.getAccessTry() + "th time";
    			staff.setTry(staff.getAccessTry() + 1);
    			
    
    		}
    	}
    	
    }
    
    //Member log in
    public Log(Member m, MessageType type, boolean ok) {
    	if(type == MessageType.LOGIN_RESPONSE) {
    		if(ok) {
    			member = m;
    			details = member.getUID() + " logged in successfully";
    			member.setTry(0); 
    		}
    		else {
    			details = "Login attempt failed on " + member.getUID() + "" + member.getAccessTry() + "th time";
    			member.setTry(member.getAccessTry() + 1);
    		}
    	}
    	
    	
    	
    }
    
    public Log(String x) {
    	details = x;
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
