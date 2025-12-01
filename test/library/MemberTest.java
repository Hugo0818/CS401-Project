package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MemberTest {

    // Fake LoginInfo class so we can test Member independently
    private static class FakeLoginInfo extends LoginInfo {
        private final String name;
        private final String password;

        public FakeLoginInfo(String name, String password) {
            super(name, password, true); // If your LoginInfo has a different constructor, adjust
            this.name = name;
            this.password = password;
        }

        @Override
        public String getUidOrName() {
            return name;
        }

        @Override
        public String getPassword() {
            return password;
        }
    }

    // Fake resource
    private static class FakeResource implements Resource {
        @Override public ArrayList<Log> getLogs() { return new ArrayList<>(); }
        @Override public void addLog(Log log) {}
        @Override public String getDetails() { return "Fake"; }
        @Override public boolean isAvailable() { return true; }
        @Override public void setCheckedOut(boolean b) {}
        @Override public String getDisplayName() { return "FakeResource"; }
    }

    private Member member;
    private FakeLoginInfo login;

    @BeforeEach
    void setup() {
        login = new FakeLoginInfo("John Doe", "pass123");
        member = new Member(login);
    }

    @Test
    void testConstructorInitializesFields() {
        assertEquals("John Doe", member.getName());
        assertEquals("pass123", member.getpassword());
        assertNotNull(member.getUID());
        assertEquals("Member", member.getType());
        assertNotNull(member.getCheckoutHistory());
        assertNotNull(member.getCurrentlyHeldResources());
    }

    @Test
    void testUIDAutoIncrement() {
        Member m1 = new Member(new FakeLoginInfo("A", "1"));
        Member m2 = new Member(new FakeLoginInfo("B", "2"));

        assertNotEquals(m1.getUID(), m2.getUID());
        assertTrue(m1.getUID().startsWith("M"));
        assertTrue(m2.getUID().startsWith("M"));
    }

    @Test
    void testAddResourceToPossession() {
        FakeResource r = new FakeResource();
        member.addResourceToPossession(r);

        assertEquals(1, member.getCurrentlyHeldResources().size());
        assertTrue(member.getCurrentlyHeldResources().contains(r));
    }

    @Test
    void testRemoveResourceFromPossession() {
        FakeResource r = new FakeResource();
        member.addResourceToPossession(r);

        member.removeResourceFromPossession(r);

        assertEquals(0, member.getCurrentlyHeldResources().size());
    }

    @Test
    void testAddLog() {
        Log log = new Log(member, new FakeResource());
        member.addLog(log);

        assertEquals(1, member.getCheckoutHistory().size());
        assertTrue(member.getCheckoutHistory().contains(log));
    }

    @Test
    void testSetLogs() {
        Log log1 = new Log(member, new FakeResource());
        Log log2 = new Log(member, new FakeResource());
        ArrayList<Log> newLogs = new ArrayList<>();
        newLogs.add(log1);
        newLogs.add(log2);

        member.setLogs(newLogs);

        assertEquals(2, member.getCheckoutHistory().size());
        assertSame(log1, member.getCheckoutHistory().get(0));
        assertSame(log2, member.getCheckoutHistory().get(1));
    }
}