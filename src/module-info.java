module LibraryProject {
    requires java.desktop;
	requires org.junit.jupiter.api; // Required for Swing (javax.swing.*)
    
    exports library;
    exports client;
    exports server;
}