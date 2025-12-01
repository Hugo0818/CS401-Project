package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LogTest {

    // ----- Dummy Member -----
    static class DummyMember extends Member {
        public DummyMember() { super(null); }
        @Override public String getUID() { return "M123"; }
    }

    // ----- Dummy Staff -----
    static class DummyStaff extends Staff {
        public DummyStaff() { super(null); }
        @Override public String getUID() { return "S999"; }
    }

    // ----- Dummy Resource -----
    static class DummyResource implements Resource {
        @Override public String getDisplayName() { return "DummyResource"; }
        @Override public boolean isAvailable() { return true; }
        @Override public void setCheckedOut(boolean b) {}
        @Override public String getDetails() { return ""; }
        @Override public java.util.ArrayList<Log> getLogs() { return new java.util.ArrayList<>(); }
        @Override public void addLog(Log log) {}
    }

    // ---------- TESTS FOR CONSTRUCTORS ----------

    @Test
    void testMemberCheckOutConstructor() {
        DummyMember m = new DummyMember();
        DummyResource r = new DummyResource();

        Log log = new Log(m, r, MessageType.CHECK_OUT_RES);

        assertEquals(m, log.getMember());
        assertEquals(r, log.getResource());
        assertTrue(log.getDetails().contains("Member M123checked out DummyResource"));
    }

    @Test
    void testMemberCheckInConstructor() {
        DummyMember m = new DummyMember();
        DummyResource r = new DummyResource();

        Log log = new Log(m, r, MessageType.CHECK_IN_RES);

        assertEquals(m, log.getMember());
        assertEquals(r, log.getResource());
        assertTrue(log.getDetails().contains("Member M123checked in DummyResource"));
    }

    @Test
    void testStaffCheckOutConstructor() {
        DummyStaff s = new DummyStaff();
        DummyResource r = new DummyResource();

        Log log = new Log(s, r, MessageType.CHECK_OUT_RES);

        assertEquals(s, log.getStaff());
        assertEquals(r, log.getResource());
        assertTrue(log.getDetails().contains("Staff S999checked out DummyResource"));
    }

    @Test
    void testStaffCheckInConstructor() {
        DummyStaff s = new DummyStaff();
        DummyResource r = new DummyResource();

        Log log = new Log(s, r, MessageType.CHECK_IN_RES);

        assertEquals(s, log.getStaff());
        assertEquals(r, log.getResource());
        assertTrue(log.getDetails().contains("Staff S999checked in DummyResource"));
    }

    @Test
    void testAddResourceConstructor() {
        DummyResource r = new DummyResource();

        Log log = new Log(r, MessageType.ADD_RESOURCE_RES);

        assertEquals(r, log.getResource());
        assertTrue(log.getDetails().contains("New entry added to catalog"));
    }

    @Test
    void testRemoveResourceConstructor() {
        DummyResource r = new DummyResource();

        Log log = new Log(r, MessageType.REMOVE_RESOURCE_RES);

        assertEquals(r, log.getResource());
        assertTrue(log.getDetails().contains("New entry removed from catalog"));
    }

    // ---------- TESTING checkOut + checkIn METHODS ----------

    @Test
    void testCheckOutWorksOnce() {
        Log log = new Log(new DummyMember(), new DummyResource(), MessageType.CHECK_OUT_RES);

        // Initial constructor does NOT set checkout time, so checkOut() should succeed
        assertNull(log.getCheckOutTime());
        assertTrue(log.checkOut(new DummyMember(), new DummyResource()));
        assertNotNull(log.getCheckOutTime());

        // Second call should fail
        assertFalse(log.checkOut(new DummyMember(), new DummyResource()));
    }

    @Test
    void testCheckInOnlyAfterCheckOut() {
        Log log = new Log(new DummyMember(), new DummyResource(), MessageType.CHECK_OUT_RES);

        // Must call checkOut before checkIn can work
        assertTrue(log.checkOut(new DummyMember(), new DummyResource()));
        assertNull(log.getCheckInTime());

        assertTrue(log.checkIn());
        assertNotNull(log.getCheckInTime());

        // Second check-in should fail
        assertFalse(log.checkIn());
    }
}
