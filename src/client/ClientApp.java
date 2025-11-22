package client;
import static util.DebugUtil.getCallerInfo;

public class ClientApp {
    public static void main(String[] args) {
        System.out.println("[DEBUG] " + getCallerInfo() + " ClientApp main() called.");
        GUIManager gui = new GUIManager();
        // For now, just instantiate the GUIManager
        // the GUIManager automatically makes a client object and connects to the server
    }
}
