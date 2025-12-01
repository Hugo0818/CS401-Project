package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Unit tests for ResourceManager class.
 * Tests CRUD operations and search functionality.
 */
class ResourceManagerTest {
    
    private ResourceManager resourceManager;
    private Book testBook;
    private Movie testMovie;
    
    @BeforeEach
    void setUp() {
        resourceManager = new ResourceManager(null);
        testBook = new Book();
        testMovie = new Movie("Inception", "Christopher Nolan", 148, "PG-13");
    }
    
    @Test
    void testAddResource() {
        // TODO: Implement test
    }
    
    @Test
    void testSearchCatalog() {
        // TODO: Implement test
    }
    
    @Test
    void testEditResource() {
        // TODO: Implement test
    }
    
    @Test
    void testRemoveResource() {
        // TODO: Implement test
    }
    
    @Test
    void testRemoveNonExistentResource() {
        // TODO: Implement test
    }
}
