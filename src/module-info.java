module LibraryProject {
    requires java.desktop;
	requires org.junit.jupiter.api;
	requires org.junit.platform.commons;
	requires junit;
	requires org.junit.platform.suite.api; // Required for Swing (javax.swing.*)
    
    exports library;
    exports client;
    exports server;
    
    opens library to org.junit.platform.commons;
    opens client to org.junit.platform.commons;
    opens server to org.junit.platform.commons;
}