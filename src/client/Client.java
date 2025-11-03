package client;

import library.Message;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {
    private Socket connection;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private GUIManager guiManager;
    
    public void connectToServer(int port) {
        
    }
    
    public void processMessage(Message message) {
        
    }
    
    public void sendMessage(Message message) {
        
    }
    
    public void closeConnection() {
        
    }
}
