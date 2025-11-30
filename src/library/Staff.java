package library;

import java.io.Serializable;

public class Staff implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private LoginInfo creds;
	private String uid;
	private static int idCounter = 1; //unique id
	private String clientType = "Staff";
	
	public Staff(LoginInfo info) {
		uid = "S" + idCounter++;
		creds = info;
	}
	
	
	public void setCreds(LoginInfo info) {
		creds= info;
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
	
	
	
	public void setUID(String uid) {
	    this.uid = uid;
	}

}
