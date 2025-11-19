package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Unit tests for MemberManager class.
 * Tests member CRUD operations and search functionality.
 */
class MemberManagerTest {
    
    private MemberManager memberManager;
    private Member testMember;
    
    @BeforeEach
    void setUp() {
        memberManager = new MemberManager(null);
        testMember = new Member("John Doe");
    }
    
    @Test
    void testAddMember() {
        // TODO: Implement test
    }
    
    @Test
    void testRemoveMember() {
        // TODO: Implement test
    }
    
    @Test
    void testSearchMembers() {
        // TODO: Implement test
    }
    
    @Test
    void testSearchMembersByUID() {
        // TODO: Implement test
    }
    
    @Test
    void testGetMemberOutOfBounds() {
        // TODO: Implement test
    }
}
