package library;
import java.io.Serializable;

public class Message implements Serializable {
    private MessageType messageType;
    private Object msgContent;
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public Object getMessageContent() {
        return msgContent;
    }
}
