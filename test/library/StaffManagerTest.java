package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for StaffManager class.
 * Tests staff CRUD operations and permissions management.
 */
class StaffManagerTest {
    
    private StaffManager staffManager;
    ArrayList<Staff> testStaffList = new ArrayList<>();
    
    
    @BeforeEach
    void setUp() {
        testStaffList.add(new Staff("Alice", "Manager"));
        testStaffList.add(new Staff("Bob", "Assistant"));
        staffManager = new StaffManager(testStaffList);
    }
    
    @Test
    void testAddStaff() {
        // TODO: Implement test
    }
    
    @Test
    void testRemoveStaff() {
        // TODO: Implement test
    }
    
    @Test
    void testSetStaff() {
        // TODO: Implement test
    }
    
    @Test
    void testSetStaffNotFound() {
        // TODO: Implement test
    }
}
