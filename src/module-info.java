module LibraryProject {
    requires java.desktop;
	requires org.junit.jupiter.api;
	requires junit;
	requires org.junit.platform.suite.api; // Required for Swing (javax.swing.*)
    
    exports library;
    exports client;
    exports server;
}