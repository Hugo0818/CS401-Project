package library;

import java.util.ArrayList;
import java.util.EnumSet;

public class Staff {
    // Staff class implementation - member variables and methods to be determined based on application needs
	String name;
	String uid;
	static int idCounter = 0;
	
	public Staff(String Name) {
		name = Name;
		idCounter++;
		uid = "SID" + idCounter;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUID() {
		return uid;
	}
}
