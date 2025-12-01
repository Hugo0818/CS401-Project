package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void testConstructorStoresFieldsCorrectly() {
        Object payload = 123;  // Any object
        Message m = new Message(
                MessageType.LOGIN_ATTEMPT,
                payload,
                false,
                "Failed login"
        );

        assertEquals(MessageType.LOGIN_ATTEMPT, m.getType());
        assertEquals(payload, m.getPayload());
        assertFalse(m.isOk());
        assertEquals("Failed login", m.getInfo());
    }

    @Test
    void testSimpleConstructorDefaultsOkAndInfo() {
        Object payload = "Some payload";
        Message m = new Message(MessageType.CATALOG_VIEW_REQ, payload);

        assertEquals(MessageType.CATALOG_VIEW_REQ, m.getType());
        assertEquals(payload, m.getPayload());
        assertTrue(m.isOk());
        assertEquals("", m.getInfo());
    }

    @Test
    void testOkFactoryMethod() {
        Object payload = "All good";
        Message m = Message.ok(MessageType.PING, payload);

        assertEquals(MessageType.PING, m.getType());
        assertEquals(payload, m.getPayload());
        assertTrue(m.isOk());
        assertEquals("", m.getInfo());
    }

    @Test
    void testFailFactoryMethod() {
        Message m = Message.fail(MessageType.ERROR, "Something went wrong");

        assertEquals(MessageType.ERROR, m.getType());
        assertNull(m.getPayload());
        assertFalse(m.isOk());
        assertEquals("Something went wrong", m.getInfo());
    }

    @Test
    void testToStringFormat() {
        Message m = new Message(
                MessageType.W_CLOSED,
                null,
                true,
                "Window closed"
        );

        String s = m.toString();
        assertTrue(s.contains("W_CLOSED"));
        assertTrue(s.contains("ok=true"));
        assertTrue(s.contains("info=Window closed"));
    }
}


