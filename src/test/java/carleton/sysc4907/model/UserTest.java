package carleton.sysc4907.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setup() {
        user = new User(0, "Test User", PermissionLevel.HOST);
    }

    @Test
    public void testConstructSuccess() {
        assertEquals(0, user.getId());
        assertEquals("Test User", user.getUsername());
        assertEquals(PermissionLevel.HOST, user.getPermissionLevel());
    }

    @Test
    public void testSetUsername() {
        user.setUsername("New Name");
        assertEquals("New Name", user.getUsername());
    }

    @Test
    public void testSetPermissionLevelValid() {
        User guest = new User(0, "Test User", PermissionLevel.READ_WRITE);
        assertEquals(PermissionLevel.READ_WRITE, guest.getPermissionLevel());
        guest.setPermissionLevel(PermissionLevel.READ_ONLY);
        assertEquals(PermissionLevel.READ_ONLY, guest.getPermissionLevel());
        guest.setPermissionLevel(PermissionLevel.READ_ONLY);
        assertEquals(PermissionLevel.READ_ONLY, guest.getPermissionLevel());

        user.setPermissionLevel(PermissionLevel.HOST);
        assertEquals(PermissionLevel.HOST, user.getPermissionLevel());
    }

    @Test
    public void testSetPermissionLevelInvalid() {
        User guest = new User(0, "Test User", PermissionLevel.READ_WRITE);
        assertEquals(PermissionLevel.READ_WRITE, guest.getPermissionLevel());

        // Try reducing a host's permissions
        assertThrows(IllegalArgumentException.class,
                () -> user.setPermissionLevel(PermissionLevel.READ_ONLY));

        // Try escalating a guest to host
        assertThrows(IllegalStateException.class,
                () -> guest.setPermissionLevel(PermissionLevel.HOST));
    }
}
