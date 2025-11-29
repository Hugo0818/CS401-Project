package library;

import java.io.Serializable;

/**
 * Lightweight serializable message used for client-server comms.
 * type: enum describing the message
 * payload: content (String, LoginInfo, Member, ArrayList, etc.)
 * ok/info: optional status fields for convenience
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private Object payload;
    private boolean ok;
    private String info;

    public Message(MessageType type, Object payload) {
        this(type, payload, true, "");
    }

    public Message(MessageType type, Object payload, boolean ok, String info) {
        this.type = type;
        this.payload = payload;
        this.ok = ok;
        this.info = info;
    }

    public MessageType getType() { return type; }
    public Object getPayload() { return payload; }
    public boolean isOk() { return ok; }
    public String getInfo() { return info; }

    public static Message ok(MessageType t, Object payload) {
        return new Message(t, payload, true, "");
    }
    public static Message fail(MessageType t, String info) {
        return new Message(t, null, false, info);
    }

    @Override
    public String toString() {
        return "Message[type=" + type + ", ok=" + ok + ", info=" + info + "]";
    }
}
