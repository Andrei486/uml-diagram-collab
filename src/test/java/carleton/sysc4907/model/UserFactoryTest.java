package carleton.sysc4907.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    private UserFactory factory;

    @BeforeEach
    public void setup() {
        factory = new UserFactory();
    }

    @Test
    public void createHostOnceSuccess() {
        User host = factory.createHostUser("Test Host");
        assertEquals(0, host.getId());
        assertEquals(PermissionLevel.HOST, host.getPermissionLevel());
        assertEquals("Test Host", host.getUsername());
    }

    @Test
    public void createHostTwiceException() {
        factory.createHostUser("Test Host");
        assertThrows(IllegalStateException.class,
                () -> factory.createHostUser("Test Host 2"));
    }

    @Test
    public void createGuestUsersSuccess() {
        User guest1 = factory.createGuestUser("Test Guest 1");
        assertEquals(0, guest1.getId());
        assertEquals(PermissionLevel.READ_ONLY, guest1.getPermissionLevel());
        assertEquals("Test Guest 1", guest1.getUsername());

        User guest2 = factory.createGuestUser("Test Guest 2");
        assertEquals(1, guest2.getId());
        assertEquals(PermissionLevel.READ_ONLY, guest2.getPermissionLevel());
        assertEquals("Test Guest 2", guest2.getUsername());

        // Check that there is no error if guests share a username
        User guest3 = factory.createGuestUser("Test Guest 3");
        assertEquals(2, guest3.getId());
    }

    @Test
    public void resetFactoryCreateHostAgainSuccess() {
        factory.createHostUser("Test Host");
        factory.resetFactory();
        // Creating another host now should not throw an error
        User host = factory.createHostUser("Test Host");
        assertEquals(0, host.getId());
        assertEquals(PermissionLevel.HOST, host.getPermissionLevel());
        assertEquals("Test Host", host.getUsername());
    }
}
