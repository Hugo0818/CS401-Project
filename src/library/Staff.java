package library;

public class Staff {
	private String name;
	private String uid;
	private String password;
	private static int idCounter = 1; //unique id
	
	public Staff(String Name, String password) {
		name = Name;
		this.password = password;
		uid = "S" + idCounter++;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
		
	public String getUID() {
		return uid;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUID(String uid) {
	    this.uid = uid;
	}

}
