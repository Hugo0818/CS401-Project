package library;

import java.util.ArrayList;
import java.util.EnumSet;

public class Staff {
	private String name;
	private String uid;
	private Boolean isAdmin;
	private static int idCounter = 1; //for unique ids
	
	public Staff(String Name, Boolean isAdmin) {
		name = Name;
		this.isAdmin = isAdmin;
		uid = "SID" + idCounter++;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUID() {
		return uid;
	}
}
