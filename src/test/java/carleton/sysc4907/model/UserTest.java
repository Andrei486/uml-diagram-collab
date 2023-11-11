package carleton.sysc4907.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testConstructSuccess() {
        User host = new User(0, "Test User", PermissionLevel.HOST);
        assertEquals(0, host.getId());
        assertEquals("Test User", host.getUsername());
        assertEquals(PermissionLevel.HOST, host.getPermissionLevel());
    }

    @Test
    public void testSetUsername() {
        User host = new User(0, "Test User", PermissionLevel.HOST);
        host.setUsername("New Name");
        assertEquals("New Name", host.getUsername());
    }

    @Test
    public void testSetPermissionLevelValid() {
        User guest = new User(0, "Test User", PermissionLevel.READ_WRITE);
        assertEquals(PermissionLevel.READ_WRITE, guest.getPermissionLevel());
        guest.setPermissionLevel(PermissionLevel.READ_ONLY);
        assertEquals(PermissionLevel.READ_ONLY, guest.getPermissionLevel());
        guest.setPermissionLevel(PermissionLevel.READ_ONLY);
        assertEquals(PermissionLevel.READ_ONLY, guest.getPermissionLevel());

        User host = new User(0, "Test User", PermissionLevel.HOST);
        host.setPermissionLevel(PermissionLevel.HOST);
        assertEquals(PermissionLevel.HOST, host.getPermissionLevel());
    }

    @Test
    public void testSetPermissionLevelInvalid() {
        User guest = new User(0, "Test User", PermissionLevel.READ_WRITE);
        assertEquals(PermissionLevel.READ_WRITE, guest.getPermissionLevel());

        // Try reducing a host's permissions
        User host = new User(0, "Test User", PermissionLevel.HOST);
        assertThrows(IllegalArgumentException.class,
                () -> host.setPermissionLevel(PermissionLevel.READ_ONLY));

        // Try escalating a guest to host
        assertThrows(IllegalStateException.class,
                () -> guest.setPermissionLevel(PermissionLevel.HOST));
    }
}
