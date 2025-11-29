package server;

import library.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Client-side handler. Uses LibraryFacade for business logic (login/signup/search/checkout).
 *
 * Expected LibraryFacade methods used by this handler:
 *   Member facade.loginMember(String uid, String password)
 *   String    facade.signupMember(String name, String password)  // returns UID or null/error
 *   Staff    facade.loginStaff(String uid, String password)
 *   String   facade.signupStaff(String name, String password)
 *   ArrayList<Resource> facade.searchCatalog(String query)
 *   Member   facade.getMemberByUID(String uid) // optional for member info
 *
 * If your facade uses different method names, adapt the calls accordingly.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final int clientId;
    private final LibraryFacade facade;
    private final LibraryServer server;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    // track logged user (could be Staff or Member)
    private Staff loggedStaff = null;
    private Member loggedMember = null;

    public ClientHandler(Socket socket, int clientId, LibraryFacade facade, LibraryServer server) {
        this.socket = socket;
        this.clientId = clientId;
        this.facade = facade;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("[Handler#" + clientId + "] Started");

            while (!socket.isClosed()) {
                Message msg = (Message) in.readObject();
                if (msg == null) break;
                processMessage(msg);
                if (msg.getType() == MessageType.LOGOUT_ATTEMPT) break;
            }
        } catch (EOFException eof) {
            System.out.println("[Handler#" + clientId + "] Client disconnected.");
        } catch (Exception e) {
            System.err.println("[Handler#" + clientId + "] Error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void processMessage(Message msg) {
        try {
            switch (msg.getType()) {
                case LOGIN_ATTEMPT -> handleLogin(msg);
                case SIGNUP_ATTEMPT -> handleSignup(msg);
                case CATALOG_SEARCH_REQ -> handleCatalogSearch(msg);
                case MEMBER_SEARCH_REQ -> handleMemberSearch(msg);
                case LOGOUT_ATTEMPT -> handleLogout(msg);
                default -> sendMessage(Message.fail(MessageType.ERROR, "Unknown message type: " + msg.getType()));
            }
        } catch (Exception ex) {
            sendMessage(Message.fail(MessageType.ERROR, "Server error: " + ex.getMessage()));
        }
    }

    // LOGIN: payload is LoginInfo (uidOrName,password,isStaff) for login attempts
    private void handleLogin(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof LoginInfo info)) {
            sendMessage(Message.fail(MessageType.LOGIN_FAIL, "Invalid login payload"));
            return;
        }

        if (info.isStaff()) {
            // facade should provide loginStaff(uid, password) -> Staff or null
            Staff s = facade.loginStaff(info.getUidOrName(), info.getPassword());
            if (s != null) {
                loggedStaff = s;
                sendMessage(Message.ok(MessageType.LOGIN_SUCCESS, "STAFF"));
            } else {
                sendMessage(Message.fail(MessageType.LOGIN_FAIL, "Invalid staff credentials"));
            }
        } else {
            Member m = facade.loginMember(info.getUidOrName(), info.getPassword());
            if (m != null) {
                loggedMember = m;
                sendMessage(Message.ok(MessageType.LOGIN_SUCCESS, "MEMBER"));
            } else {
                sendMessage(Message.fail(MessageType.LOGIN_FAIL, "Invalid member credentials"));
            }
        }
    }

    // SIGNUP: payload is LoginInfo where uidOrName is name when signing up
    private void handleSignup(Message msg) {
        Object p = msg.getPayload();
        if (!(p instanceof LoginInfo info)) {
            sendMessage(Message.fail(MessageType.SIGNUP_FAIL, "Invalid signup payload"));
            return;
        }

        if (info.isStaff()) {
            String newUID = facade.signupStaff(info.getUidOrName(), info.getPassword());
            if (newUID != null) {
                sendMessage(Message.ok(MessageType.SIGNUP_SUCCESS, newUID));
            } else {
                sendMessage(Message.fail(MessageType.SIGNUP_FAIL, "Could not create staff account"));
            }
        } else {
            String newUID = facade.signupMember(info.getUidOrName(), info.getPassword());
            if (newUID != null) {
                sendMessage(Message.ok(MessageType.SIGNUP_SUCCESS, newUID));
            } else {
                sendMessage(Message.fail(MessageType.SIGNUP_FAIL, "Could not create member account"));
            }
        }
    }

    private void handleCatalogSearch(Message msg) {
        Object p = msg.getPayload();
        String q = (p instanceof String) ? (String) p : "";
        ArrayList<Resource> results = facade.searchCatalog(q);
        sendMessage(Message.ok(MessageType.CATALOG_SEARCH_RES, results));
    }

    private void handleMemberSearch(Message msg) {
        Object p = msg.getPayload();
        String q = (p instanceof String) ? (String) p : "";
        ArrayList<Member> results = facade.searchMembers(q); // assume facade exposes this
        sendMessage(Message.ok(MessageType.MEMBER_SEARCH_RES, results));
    }

    private void handleLogout(Message msg) {
        sendMessage(Message.ok(MessageType.LOGOUT_RESPONSE, "Goodbye"));
    }

    public synchronized void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.err.println("[Handler#" + clientId + "] Send failed: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
        server.removeHandler(this);
    }
}
