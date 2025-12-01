package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MovieTest {
	
	//Dummy member for creating logs
	static class DummyMember extends Member{
		public DummyMember() {super(null);}
		@Override public String getUID() {return "M123";}
	}

    @Test
    void testConstructorInitialValues() {
        Movie movie = new Movie("Inception", "Christopher Nolan", 148, "PG-13");

        assertEquals("Inception", movie.getDisplayName());
        assertTrue(movie.isAvailable());
        assertTrue(movie.getLogs().isEmpty());
        
        String details = movie.getDetails();
        assertTrue(details.contains("Title: Inception"));
        assertTrue(details.contains("Director: Christopher Nolan"));
        assertTrue(details.contains("Runtime: 148 mins"));
        assertTrue(details.contains("Rating: PG-13"));
    }

    @Test
    void testAvailabilityToggle() {
        Movie movie = new Movie("Avatar", "James Cameron", 162, "PG-13");

        assertTrue(movie.isAvailable());
        movie.setCheckedOut(false);
        assertFalse(movie.isAvailable());
        movie.setCheckedOut(true);
        assertTrue(movie.isAvailable());
    }

    @Test
    void testAddLog() {
        Movie movie = new Movie("Jaws", "Spielberg", 124, "PG");
        
        DummyMember m = new DummyMember();
        Log log = new Log(m, movie, MessageType.CHECK_OUT_RES);  // member null is fine for testing

        movie.addLog(log);

        ArrayList<Log> logs = movie.getLogs();
        assertEquals(1, logs.size());
        assertSame(log, logs.get(0));
    }

    @Test
    void testGetDisplayName() {
        Movie movie = new Movie("Toy Story", "John Lasseter", 81, "G");
        assertEquals("Toy Story", movie.getDisplayName());
    }
}