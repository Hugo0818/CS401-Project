# Workload Allocator (TODO)

Use this file to track project tasks, assign them to team members, and mark progress. Update regularly!

## How to Use

- Add new tasks to the list below.
- Assign each task to a team member (add your name).
- Mark status as `[ ]` (not started), `[~]` (in progress), or `[x]` (done).
- Move completed tasks to the bottom or a separate section if desired.

---

## Task List

| Status | Task Description                              | Assigned To | Notes/Files                         |
| ------ | --------------------------------------------- | ----------- | ----------------------------------- |
| [ ]    | Implement checkout logic in ResourceManager   |             | ResourceManager.java (line 44)      |
| [ ]    | Implement checkin logic in ResourceManager    |             | ResourceManager.java (line 49)      |
| [ ]    | Implement message processing in Client        |             | Client.java (processMessage method) |
| [ ]    | Implement message sending in Client           |             | Client.java (sendMessage method)    |
| [ ]    | Implement message processing in ClientHandler |             | ClientHandler.java (processMessage) |
| [ ]    | Implement message sending in ClientHandler    |             | ClientHandler.java (sendMessage)    |
| [ ]    | Implement staff search logic                  |             | StaffManager.java (line 38)         |
| [ ]    | Implement setPermissions in StaffManager      |             | StaffManager.java (line 43)         |
| [ ]    | Implement getPermissions in StaffManager      |             | StaffManager.java (line 47)         |
| [ ]    | Implement hasPermission in StaffManager       |             | StaffManager.java (line 52)         |
| [ ]    | Implement getRecentLogs in LogManager         |             | LogManager.java (line 22)           |
| [ ]    | Implement saveChanges in LibraryFacade        |             | LibraryFacade.java (saveChanges)    |
| [ ]    | Implement getDetails() for CD class           |             | CD.java (line 24)                   |
| [ ]    | Implement getDetails() for Book class         |             | Book.java (line 26)                 |
| [ ]    | Implement addLog() for CD class               |             | CD.java (line 19)                   |
| [ ]    | Define MessageType enum values                |             | MessageType.java (line 4)           |
| [ ]    | Design Swing GUI layout                       |             | GUIManager.java                     |
| [ ]    | Implement Swing GUI panels                    |             | GUIManager.java                     |
| [ ]    | Connect GUI to Client networking              |             | GUIManager.java                     |
| [ ]    | Write unit tests for ResourceManager          |             | Test checkout/checkin logic         |
| [ ]    | Write unit tests for MemberManager            |             | Test member CRUD operations         |
| [ ]    | Write unit tests for StaffManager             |             | Test staff and permissions          |
| [ ]    | Write unit tests for LogManager               |             | Test log retrieval methods          |
| [ ]    | Write unit tests for LibraryFacade            |             | Test passthrough methods            |
| [ ]    | Write README.md with setup instructions       |             | Include Eclipse setup, run commands |
| [ ]    | Add error handling for socket timeouts        |             | Client.java, ClientHandler.java     |
| [ ]    | Add error handling for serialization failures |             | LibraryFacade.java                  |
| [ ]    | Implement login system                        |             |                                     |

## Completed Tasks

| Status | Task Description                               | Assigned To | Notes                                |
| ------ | ---------------------------------------------- | ----------- | ------------------------------------ |
| [x]    | Create Resource interface                      | Hugo        | Resource.java                        |
| [x]    | Create Book class                              | Ralph       | Book.java                            |
| [x]    | Create CD class                                | Ralph       | CD.java                              |
| [x]    | Create Movie class                             | Ryan-pelo   | Movie.java                           |
| [x]    | Create GenericResource class                   | Ryan-pelo   | GenericResource.java                 |
| [x]    | Create Log class                               | Hugo        | Log.java                             |
| [x]    | Create Member class                            | Nathan      | Member.java                          |
| [x]    | Create Staff class                             | Sal         | Staff.java                           |
| [x]    | Create ResourceManager class                   | Ryan-pelo   | ResourceManager.java                 |
| [x]    | Create MemberManager class                     | Ryan-pelo   | MemberManager.java                   |
| [x]    | Create StaffManager class                      | Ryan-pelo   | StaffManager.java                    |
| [x]    | Create LogManager class                        | Ryan-pelo   | LogManager.java                      |
| [x]    | Create LibraryFacade class                     | Ryan-pelo   | LibraryFacade.java                   |
| [x]    | Create Message class                           | Ryan-pelo   | Message.java                         |
| [x]    | Create MessageType enum                        | Ryan-pelo   | MessageType.java (needs values)      |
| [x]    | Create Permissions enum                        | Ryan-pelo   | Permissions.java                     |
| [x]    | Create PermissionLevel enum                    | Ryan-pelo   | PermissionLevel.java                 |
| [x]    | Create Client class                            | Ryan-pelo   | Client.java                          |
| [x]    | Create ClientApp entry point                   | Ryan-pelo   | ClientApp.java                       |
| [x]    | Create GUIManager class skeleton               | Ryan-pelo   | GUIManager.java                      |
| [x]    | Create LibraryServer class                     | Ryan-pelo   | LibraryServer.java                   |
| [x]    | Create ClientHandler class                     | Ryan-pelo   | ClientHandler.java                   |
| [x]    | Create ServerMain entry point                  | Ryan-pelo   | ServerMain.java                      |
| [x]    | Implement searchCatalog in ResourceManager     | Ryan-pelo   | ResourceManager.java                 |
| [x]    | Implement addResource in ResourceManager       | Ryan-pelo   | ResourceManager.java                 |
| [x]    | Implement editResource in ResourceManager      | Ryan-pelo   | ResourceManager.java                 |
| [x]    | Implement removeResource in ResourceManager    | Ryan-pelo   | ResourceManager.java                 |
| [x]    | Implement addLog() for Book class              | Ryan-pelo   | Book.java (line 20)                  |
| [x]    | Implement getDisplayName for all Resources     | Ryan-pelo   | Resource interface + implementations |
| [x]    | Implement setAvailability for all Resources    | Ryan-pelo   | All resource classes                 |
| [x]    | Implement getLogsByDate in LogManager          | Ryan-pelo   | LogManager.java                      |
| [x]    | Implement isSameDay helper in LogManager       | Ryan-pelo   | LogManager.java                      |
| [x]    | Implement addLog in LogManager                 | Ryan-pelo   | LogManager.java                      |
| [x]    | Implement addStaff in StaffManager             | Ryan-pelo   | StaffManager.java                    |
| [x]    | Implement removeStaff in StaffManager          | Ryan-pelo   | StaffManager.java                    |
| [x]    | Implement getStaff in StaffManager             | Ryan-pelo   | StaffManager.java                    |
| [x]    | Implement setStaff in StaffManager             | Ryan-pelo   | StaffManager.java                    |
| [x]    | Implement constructors for all Manager classes | Ryan-pelo   | All *Manager.java files              |
| [x]    | Implement LibraryFacade passthrough methods    | Ryan-pelo   | LibraryFacade.java                   |
| [x]    | Implement LibraryFacade serialization          | Ryan-pelo   | LibraryFacade.java                   |
| [x]    | Configure server to bind to HOST from config   | Ryan-pelo   | ServerMain.java, LibraryServer.java  |
| [x]    | Implement Client socket connection logic       | Ryan-pelo   | Client.java                          |
| [x]    | Implement Client sendMessage method            | Ryan-pelo   | Client.java                          |
| [x]    | Implement Client receiveMessage method         | Ryan-pelo   | Client.java                          |
| [x]    | Implement Client closeConnection method        | Ryan-pelo   | Client.java                          |
| [x]    | Implement ClientHandler run method             | Ryan-pelo   | ClientHandler.java                   |
| [x]    | Implement ClientHandler sendMessage method     | Ryan-pelo   | ClientHandler.java                   |
| [x]    | Implement LibraryServer startServer method     | Ryan-pelo   | LibraryServer.java                   |
| [x]    | Implement LibraryServer acceptClient method    | Ryan-pelo   | LibraryServer.java                   |
| [x]    | Set up Eclipse project structure               | Ryan-pelo   | .project, .classpath configured      |
| [x]    | Create config.properties file                  | Ryan-pelo   | config.properties with HOST/PORT     |
| [x]    | Configure module-info.java                     | Ryan-pelo   | module-info.java with java.desktop   |
| [x]    | Set up .gitignore for collaboration            | Ryan-pelo   | Eclipse metadata excluded            |
| [x]    | Create TODO.md workload allocator              | Ryan-pelo   | TODO.md                              |

---

> Things to consider - should we replace enums with strings?
