package carleton.sysc4907.model;

public class UserFactory {

    private static int ID_COUNTER = 0;

    private User hostUser;

    public UserFactory() {
        resetFactory();
    }

    /**
     * Creates a host user. If none exists, throws an IllegalStateException.
     * @param username the username of the host user
     * @return the created host user
     */
    public User createHostUser(String username) {
        if (hostUser != null) {
            throw new IllegalStateException("Cannot create a host user when there is already one");
        }
        hostUser = new User(ID_COUNTER++, username, PermissionLevel.HOST);
        return hostUser;
    }

    /**
     * Creates a guest user. By default, guest permission level is read-only.
     * @param username the username of the guest user
     * @return the created guest user
     */
    public User createGuestUser(String username) {
        return new User(ID_COUNTER++, username, PermissionLevel.READ_ONLY);
    }

    /**
     * Resets the factory, so it can handle a new room.
     */
    public void resetFactory() {
        ID_COUNTER = 0;
        hostUser = null;
    }
}
