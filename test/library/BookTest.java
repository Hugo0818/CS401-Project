package library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testConstructorAndGetters() {
        Book b = new Book("Test Title", "John Doe", "TestPub", "12345", true);

        assertEquals("Title: Test Title\n" +
                     "Author: John Doe\n" +
                     "Publisher: TestPub\n" +
                     "ISBN: 12345\n",
                b.getDetails());

        assertTrue(b.isAvailable());
        assertEquals("Test Title", b.getDisplayName());
        assertNotNull(b.getLogs());
        assertTrue(b.getLogs().isEmpty());
    }

    @Test
    void testAvailabilitySetter() {
        Book b = new Book("A", "B", "C", "D", true);

        assertTrue(b.isAvailable());
        b.setCheckedOut(false);
        assertFalse(b.isAvailable());
        b.setCheckedOut(true);
        assertTrue(b.isAvailable());
    }

    @Test
    void testGetDisplayName() {
        Book b = new Book("My Book", "Author", "Publisher", "111", true);
        assertEquals("My Book", b.getDisplayName());
    }
}

