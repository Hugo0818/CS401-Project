package library;
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private MessageType type;
    private Object content;
    
    public Message() {
        this.type = null;
        this.content = null;
    }
    
    public Message(MessageType type, Object content) {
        this.type = type;
        this.content = content;
    }
    
    
    public MessageType getType() {
        return type;
    }
    
    
    //return the info of the message
    public Object getContent() {
        return content;
    }
}
