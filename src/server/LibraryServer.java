package server;

import library.LibraryFacade;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class LibraryServer {
    private LibraryFacade libraryFacade;
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clientHandlers;
    
    public void startServer() {
        // TODO: Implement startServer logic
    }
    
    public void acceptClient() {
        // TODO: Implement acceptClient logic
    }
    
    public void closeServer() {
        // TODO: Implement closeServer logic
    }
}
