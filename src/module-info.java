module LibraryProject {
    requires java.desktop; // Required for Swing (javax.swing.*)
    
    exports library;
    exports client;
    exports server;
}