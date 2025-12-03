package library;

import java.io.Serializable;

public class Staff implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private LoginInfo creds;
	private String uid;
	private String clientType = "Staff";
	private int accessTry = 0;
	
	public Staff(LoginInfo info) {
		uid = ""; // Will be set by StaffManager
		creds = info;
	}
	
	
	public void setCreds(LoginInfo info) {
		creds= info;
	}
	
	public int getAccessTry() {
    	return accessTry;
    }
	
	public String getName() {
		return creds.getUidOrName();
	}
	
	
	public String getPassword() {
		return creds.getPassword();
	}
		
	public String getUID() {
		return uid;
	}
	
	
	public String getType() {
    	return clientType;
    }
	
	public void setTry(int i) {
		accessTry = i;
	}
	
	public void setUID(String uid) {
	    this.uid = uid;
	}

}
