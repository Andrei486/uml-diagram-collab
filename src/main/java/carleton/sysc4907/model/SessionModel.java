package carleton.sysc4907.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model containing information about the online session.
 */
public class SessionModel {

    private final String roomCode; // If we reuse a SessionModel across rooms, this should become a property.

    private final User localUser; // Same thing: if reused across rooms this must be a property.
    private ObservableList<User> users = FXCollections.observableArrayList();

    public SessionModel(String roomCode, User localUser) {
        if (!roomCode.matches("([A-Z0-9]){12}")) {
            throw new IllegalArgumentException("Room code must be 12 digits long and alphanumeric (uppercase only).");
        }
        if (localUser == null) {
            throw new IllegalArgumentException("Local user for a room cannot be null.");
        }
        this.roomCode = roomCode;
        this.localUser = localUser;
        getUsers().add(localUser);
    }

    /**
     * Gets the current room's room code.
     * @return the current room's room code
     */
    public String getRoomCode() {
        return roomCode;
    }

    /**
     * Gets the local user for this application.
     * @return the local User
     */
    public User getLocalUser() {
        return localUser;
    }

    /**
     * Gets the host user in the current room, if any.
     * @return the host User, or null if there is no host currently in the room
     */
    public User getHostUser() {
        return getUsers().stream()
                .filter(u -> u.getPermissionLevel() == PermissionLevel.HOST)
                .findFirst().orElse(null);
    }

    /**
     * Gets the list of users in the current room.
     * @return an ObservableList containing all the users in the current room.
     */
    public ObservableList<User> getUsers() {
        return users;
    }

    /**
     * Adds a user to the room.
     * @param user the User to add to the room
     */
    public void addUser(User user) {
        if (user.getPermissionLevel() == PermissionLevel.HOST && getHostUser() != null) {
            throw new IllegalStateException("Cannot add a host to a room that already has a host.");
        }
        getUsers().add(user);
    }

    /**
     * Kicks (i.e. removes) the specified user from the room. A host cannot be kicked.
     * @param user the User to kick from the room
     */
    public void kickUser(User user) {
        if (user.getPermissionLevel() != PermissionLevel.HOST) {
            getUsers().remove(user);
        }
    }
}
