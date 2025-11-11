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
        // TODO: Implement run logic for handling client communication
    }
    
    public void processMessage(Message message) {
        // TODO: Implement processMessage logic
    }
    
    public void sendMessage(Message message) {
        // TODO: Implement sendMessage logic
    }
}
