# Mini Library Management System

## Overview
The Mini Library Management System is a Java-based client-server application developed by a team of four as an academic project. The system simulates a small library environment where staff can manage book inventory and process check-ins and check-outs for members. Multiple GUI clients can connect concurrently to a central server over a local network.

**My primary contributions** included implementing the server-side logic, handling message routing from GUI clients, and enabling multi-client networking using TCP sockets and object streams. Teammates were responsible for GUI development and data structure management.

---

## Technologies Used
- **Programming Languages:** Java  
- **GUI Framework:** Java Swing  
- **Networking:** TCP Sockets, Object Streams  
- **Version Control:** Git  

---

## Features
- **Staff Account Management:** Staff users can create accounts to manage library operations.  
- **Member Account Creation:** Users can create library member accounts.  
- **Book Catalog Management:** Staff can add new books to the catalog.  
- **Check-In / Check-Out:** Staff can check books in and out for members.  
- **Multi-Client Support:** Multiple GUI clients can connect to the server simultaneously.  

> **Note:** Member check-in/check-out is currently handled only by staff.

---

## Project Structure
MiniLibrarySystem/

    server/                 # Server-side logic and networking
        Server.java
        ClientHandler.java

    client/                 # GUI clients using Java Swing
        Client.java
        GUI.java

    managers/               # Managers controlling models and business logic
        LibraryManager.java
        LogManager.java
        StaffManager.java
        ...

    facade/                 # Facade design pattern for unified library operations
        LibraryFacade.java

    models/                 # Core entities
        Book.java
        Member.java
        Log.java
        ...


