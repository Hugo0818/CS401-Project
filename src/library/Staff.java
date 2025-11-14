package library;

import java.util.ArrayList;
import java.util.EnumSet;

public class Staff {
	private String name;
	private String uid;
	private String role;
	private static int idCounter = 1; //for unique ids
	
	public Staff(String Name, String Role) {
		name = Name;
		role = Role;
		uid = "SID" + idCounter++;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUID() {
		return uid;
	}
}
