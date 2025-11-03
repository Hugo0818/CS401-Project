package library;

public class Message {
    private MessageType messageType;
    private Object msgContent;
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public Object getMessageContent() {
        return msgContent;
    }
}
