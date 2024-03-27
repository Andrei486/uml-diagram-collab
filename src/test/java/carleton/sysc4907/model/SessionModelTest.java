package carleton.sysc4907.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionModelTest {

    private final String ROOM_CODE = "TEST1234TEST";

    private SessionModel sessionModel;

    private User hostUser;

    @BeforeEach
    public void setup() {
        // This setup is for test cases that do not exercise the constructor.
        // Some tests may instantiate their own SessionModel.
        hostUser = new User(0, "Test Host", PermissionLevel.HOST);
        sessionModel = new SessionModel(ROOM_CODE, hostUser);
    }

    /**
     * Test that the SessionModel is constructed as expected with valid parameters.
     */
    @Test
    public void createSessionModelValidParams() {
        assertEquals(ROOM_CODE, sessionModel.getRoomCode());
        assertEquals(1, sessionModel.getUsers().size());
        assertEquals(hostUser, sessionModel.getLocalUser());
        assertEquals(hostUser, sessionModel.getHostUser());
    }

    @Test
    public void getHostUserNoHost() {
        User guestUser = new User(0, "Test Guest", PermissionLevel.READ_ONLY);
        sessionModel = new SessionModel(ROOM_CODE, guestUser);
        assertEquals(1, sessionModel.getUsers().size());
        assertNull(sessionModel.getHostUser());
    }

    @Test
    public void addGuestsSuccess() {
        User guestUser1 = new User(0, "Test Guest 1", PermissionLevel.READ_ONLY);
        User guestUser2 = new User(1, "Test Guest 2", PermissionLevel.READ_ONLY);
        sessionModel.addUser(guestUser1);
        sessionModel.addUser(guestUser2);
        assertEquals(3, sessionModel.getUsers().size());
        assertTrue(sessionModel.getUsers().contains(guestUser1));
        assertTrue(sessionModel.getUsers().contains(guestUser2));
    }

    @Test
    public void addHostException() {
        User secondHost = new User(0, "Test Host 2", PermissionLevel.HOST);
        assertThrows(IllegalStateException.class,
                () -> sessionModel.addUser(secondHost));
    }

    @Test
    public void kickUsers() {
        User guestUser1 = new User(0, "Test Guest 1", PermissionLevel.READ_ONLY);
        sessionModel.addUser(guestUser1);
        assertEquals(2, sessionModel.getUsers().size());
        assertTrue(sessionModel.getUsers().contains(guestUser1));
        sessionModel.kickUser(guestUser1);
        assertEquals(1, sessionModel.getUsers().size());
        assertFalse(sessionModel.getUsers().contains(guestUser1));
        assertTrue(sessionModel.getUsers().contains(hostUser));
        sessionModel.kickUser(hostUser);
        assertEquals(1, sessionModel.getUsers().size());
        assertTrue(sessionModel.getUsers().contains(hostUser));
    }
}
