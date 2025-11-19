package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Movie class.
 * Tests Movie resource implementation.
 */
class MovieTest {
    
    private Movie movie;
    
    @BeforeEach
    void setUp() {
        movie = new Movie("The Matrix", "Wachowskis", 136, "R");
    }
    
    @Test
    void testInitialAvailability() {
        // TODO: Implement test
    }
    
    @Test
    void testGetDetails() {
        // TODO: Implement test
    }
    
    @Test
    void testGetDisplayName() {
        // TODO: Implement test
    }
    
    @Test
    void testAddLog() {
        // TODO: Implement test
    }
}
