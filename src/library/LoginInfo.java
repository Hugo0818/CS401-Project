package library;

import java.io.Serializable;

/**
 * Simple DTO for login/signup attempts
 */
public class LoginInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String uidOrName; // uid for login, name for signup (depending on use)
    private final String password;
    private final boolean isStaff; // true if staff account

    public LoginInfo(String uidOrName, String password, boolean isStaff) {
        this.uidOrName = uidOrName;
        this.password = password;
        this.isStaff = isStaff;
    }
    
    
    

    public String getUidOrName() { return uidOrName; }
    public String getPassword() { return password; }
    public boolean isStaff() { return isStaff; }
}
