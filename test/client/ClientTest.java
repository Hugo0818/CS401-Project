package client;

import library.Message;
import library.MessageType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

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
    void testIsConnected_DefaultFalse() {
        assertFalse(client.isConnected(),
                "Client should not report connected when no socket exists.");
    }

    @Test
    void testCloseIsSafeWhenNotConnected() {
        assertFalse(client.isConnected(), "Client should start disconnected.");

        assertDoesNotThrow(() -> client.close(),
                "close() should not throw even if client is not connected.");

        assertFalse(client.isConnected(),
                "Client should still report disconnected after close().");
    }

    @Test
    void testSendMessageWhenNotConnectedThrows() {
        Message msg = new Message(MessageType.PING, "hello");

        assertThrows(RuntimeException.class, () -> client.sendMessage(msg),
                "sendMessage() should throw when client is not connected.");
    }
}

