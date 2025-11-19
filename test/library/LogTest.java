package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Log class.
 * Tests checkout/checkin log functionality.
 */
class LogTest {
    
    private Log log;
    private Member member;
    private Resource resource;
    
    @BeforeEach
    void setUp() {
        member = new Member("Test Member");
        resource = new Movie("Test Movie", "Director", 120, "PG");
        log = new Log(member, resource);
    }
    
    @Test
    void testLogCreation() {
        // TODO: Implement test
    }
    
    @Test
    void testCheckIn() {
        // TODO: Implement test
    }
    
    @Test
    void testCheckInTwice() {
        // TODO: Implement test
    }
    
    @Test
    void testCheckOutWhenAlreadyCheckedOut() {
        // TODO: Implement test
    }
    
    @Test
    void testGetMember() {
        // TODO: Implement test
    }
    
    @Test
    void testGetResource() {
        // TODO: Implement test
    }
}
