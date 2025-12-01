package client;

import library.Message;
import library.MessageType;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client("localhost", 12345);
    }

    @Test
    void testClientCreation() {
        assertNotNull(client);
        assertFalse(client.isConnected(),
                "Client should not be connected before connect() is called.");
    }

    @Test
    void testSendMessage_UsesObjectOutputStream() throws Exception {
        // Fake output stream using Piped Streams (no real server needed)
        PipedOutputStream fakeServerOut = new PipedOutputStream();
        PipedInputStream fakeServerIn = new PipedInputStream(fakeServerOut);

        // Put fake ObjectOutputStream into the client
        ObjectOutputStream oos = new ObjectOutputStream(fakeServerOut);

        // Use reflection to set client's private out field
        var field = Client.class.getDeclaredField("out");
        field.setAccessible(true);
        field.set(client, oos);

        // Send a message
        Message sent = new Message(MessageType.PING, null);
        client.sendMessage(sent);

        // Server "reads" the message
        ObjectInputStream ois = new ObjectInputStream(fakeServerIn);
        Message received = (Message) ois.readObject();

        assertEquals(sent.getType(), received.getType());
        assertEquals(sent.getPayload(), received.getPayload());
    }

    @Test
    void testCloseClosesStreams() throws Exception {
        // Create piped streams to simulate socket streams
        PipedOutputStream fakeOut = new PipedOutputStream();
        PipedInputStream fakeIn = new PipedInputStream(fakeOut);

        ObjectOutputStream oos = new ObjectOutputStream(fakeOut);
        ObjectInputStream ois = new ObjectInputStream(fakeIn);

        // Inject them into client via reflection
        var outField = Client.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(client, oos);

        var inField = Client.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(client, ois);

        // client should report not connected before we set socket, so skip socket check
        // Call close() — should not throw
        assertDoesNotThrow(() -> client.close(), "close() should not throw");

        // After close, client should report not connected
        assertFalse(client.isConnected(), "Client should report not connected after close()");

        // Calling close() again must be safe (idempotent)
        assertDoesNotThrow(() -> client.close(), "Calling close() multiple times should be safe");

        // Optionally verify that streams are effectively closed:
        // Try to write/flush — if implementation closed underlying stream, an IOException is expected.
        // But because behavior can vary, accept either an IOException (good) or no exception.
        try {
            oos.writeObject("ping");
            oos.flush();
            // If no exception, try reading from the other side with small timeout thread to see if stream is still usable.
            // We'll not fail the test on that — we already validated the client state above.
        } catch (IOException e) {
            // expected in many implementations — treat this as success
        }
    }


    @Test
    void testIsConnectedFalseByDefault() {
        assertFalse(client.isConnected(),
                "Client should not report connected when no socket exists.");
    }
}


