package library;

public class Staff {
	private String name;
	private String uid;
	private static int idCounter = 1; // unique id
	
	public Staff(String Name, Boolean isAdmin) {
		name = Name;
		uid = "SID" + idCounter++;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUID() {
		return uid;
	}
}
