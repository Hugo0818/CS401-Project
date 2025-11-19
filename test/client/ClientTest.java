package client;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import library.Message;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Unit tests for Client class.
 * Tests client-server connection and message handling.
 */
class ClientTest {
    
    private Client client;
    private String testHost;
    private int testPort;
    
    @BeforeEach
    void setUp() {
        // Load host and port from config.properties
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/config.properties");
            props.load(fis);
            testHost = props.getProperty("HOST");
            testPort = Integer.parseInt(props.getProperty("PORT"));
            fis.close();
        } catch (IOException | NumberFormatException e) {
            // Fallback to defaults if config fails
            testHost = "localhost";
            testPort = 8080;
        }
        client = new Client(testHost, testPort);
    }
    
    @Test
    void testClientCreation() {
        // TODO: Implement test
    }
    
    @Test
    void testConnectToServer() {
        // TODO: Implement test
    }
    
    @Test
    void testSendMessage() {
        // TODO: Implement test
    }
    
    @Test
    void testReceiveMessage() {
        // TODO: Implement test
    }
    
    @Test
    void testCloseConnection() {
        // TODO: Implement test
    }
    
    @Test
    void testIsConnected() {
        // TODO: Implement test
    }
}
