package server;

import static org.junit.jupiter.api.Assertions.*;

import library.LibraryFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

/**
 * Unit tests for LibraryServer class (simple, no network integration).
 */
public class LibraryServerTest {

    private LibraryServer server;
    private FakeFacade fakeFacade;

    @BeforeEach
    void setUp() {
        fakeFacade = new FakeFacade();
        server = new LibraryServer("localhost", 9999, fakeFacade);
    }

    @Test
    void testServerCreation() {
        assertNotNull(server);
    }

    @Test
    void testStopServer() {
        // Assign a real temporary ServerSocket so stopServer() can close it.
        try {
            ServerSocket socket = new ServerSocket(0);
            var field = LibraryServer.class.getDeclaredField("serverSocket");
            field.setAccessible(true);
            field.set(server, socket);
        } catch (Exception e) {
            fail("Reflection setup failed: " + e.getMessage());
        }

        assertDoesNotThrow(() -> server.stopServer());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testRemoveHandler() {
        FakeHandler handler1 = new FakeHandler();
        FakeHandler handler2 = new FakeHandler();

        // Add handlers manually using reflection
        try {
            var field = LibraryServer.class.getDeclaredField("handlers");
            field.setAccessible(true);
            List<ClientHandler> handlers = (List<ClientHandler>) field.get(server);

            handlers.add(handler1);
            handlers.add(handler2);

            assertEquals(2, handlers.size());

            server.removeHandler(handler1);
            assertEquals(1, handlers.size());

            server.removeHandler(handler2);
            assertEquals(0, handlers.size());

        } catch (Exception e) {
            fail("Reflection access failed: " + e.getMessage());
        }
    }

    /*
     * FakeFacade: calls super with a dummy filename so it compiles.
     * LibraryFacade will initialize empty managers when file missing.
     */
    static class FakeFacade extends LibraryFacade {
        public FakeFacade() {
            super("nonexistent_test_facade.ser"); // file likely doesn't exist -> facade starts empty
        }
    }

    /**
     * Fake ClientHandler (no networking). We override closeConnection to avoid touching sockets.
     */
    static class FakeHandler extends ClientHandler {
        FakeHandler() {
            // pass nulls for socket/facade/server since we won't use them in this fake
            super(null, 0, null, null);
        }

        @Override
        public void closeConnection() {
            // do nothing - prevents real socket activity in unit tests
        }
    }
}

