package library;
import java.io.Serializable;

public class Message implements Serializable {
    private MessageType messageType;
    private Object msgContent;
    
    public MessageType getMessageType() {
        // TODO: Implement getMessageType if needed
        return messageType;
    }
    
    public Object getMessageContent() {
        // TODO: Implement getMessageContent if needed
        return msgContent;
    }
}
