package server;

import library.LibraryFacade;
import library.Message;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private LibraryFacade libraryFacade;
    
    @Override
    public void run() {
        
    }
    
    public void processMessage(Message message) {
        
    }
    
    public void sendMessage(Message message) {
        
    }
}
