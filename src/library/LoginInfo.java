package library;

public class LoginInfo {
	
	private String username;
	private String password;
	
	public LoginInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	
	public void setUsername(String x) {
		username = x;
	}
	
	
	public void setPassword(String x) {
		password = x;
		
	}
		
	
	public String getUsername() {
		return username;
	}
	
	
	String getPassword() {
		return password;
	}

}
