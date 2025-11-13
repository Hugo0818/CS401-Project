package library;
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private Object content;
    
    public Message() {
        this.type = "Undefined";
        this.content = null;
    }
    
    public Message(String type, Object content) {
        this.type = type;
        this.content = content;
    }
    
    public String getType() {
        return type;
    }
    
    public Object getContent() {
        return content;
    }
}
