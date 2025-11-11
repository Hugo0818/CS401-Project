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
        // TODO: Implement connectToServer logic
    }
    
    public void processMessage(Message message) {
        // TODO: Implement processMessage logic
    }
    
    public void sendMessage(Message message) {
        // TODO: Implement sendMessage logic
    }
    
    public void closeConnection() {
        // TODO: Implement closeConnection logic
    }
}
