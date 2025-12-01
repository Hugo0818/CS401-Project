package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LogTest {

    // ----- Dummy LoginInfo for testing -----
    static class DummyLoginInfo extends LoginInfo {
        public DummyLoginInfo(String user, String pass) {
            super(user, pass, true);
        }
    }

    // ----- Dummy Resource for testing -----
    static class DummyResource implements Resource {
        @Override public String getDisplayName() { return "Dummy"; }
        @Override public boolean isAvailable() { return true; }
        @Override public void setCheckedOut(boolean b) {}
        @Override public String getDetails() { return ""; }
        @Override public java.util.ArrayList<Log> getLogs() { return new java.util.ArrayList<>(); }
        @Override public void addLog(Log log) {}
    }

    @Test
    void testConstructorInitializesWhenValid() {
        Member m = new Member(new DummyLoginInfo("user", "pass"));
        Resource r = new DummyResource();

        Log log = new Log(m, r);

        assertEquals(m, log.getMember());
        assertEquals(r, log.getResource());
        assertNotNull(log.getCheckOutTime());
        assertNull(log.getCheckInTime());
    }

    @Test
    void testConstructorDoesNothingWhenNullsProvided() {
        Log log = new Log(null, null);

        assertNull(log.getMember());
        assertNull(log.getResource());
        assertNull(log.getCheckOutTime());
        assertNull(log.getCheckInTime());
    }

    @Test
    void testCheckOutWorksOnlyOnce() {
        Member m = new Member(new DummyLoginInfo("user", "pass"));
        Resource r = new DummyResource();
        Log log = new Log(null, null);

        // First checkout should succeed
        assertTrue(log.checkOut(m, r));
        assertNotNull(log.getCheckOutTime());

        // Second checkout must fail
        assertFalse(log.checkOut(m, r));
    }

    @Test
    void testCheckInWorksOnlyAfterCheckout() {
        Member m = new Member(new DummyLoginInfo("user", "pass"));
        Resource r = new DummyResource();
        Log log = new Log(m, r);

        assertNull(log.getCheckInTime());

        // First check-in succeeds
        assertTrue(log.checkIn());
        assertNotNull(log.getCheckInTime());

        // Second check-in must fail
        assertFalse(log.checkIn());
    }
}

