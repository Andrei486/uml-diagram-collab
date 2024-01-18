package carleton.sysc4907.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A model containing information about the online session.
 */
public class SessionModel {

    private final StringProperty roomCode = new SimpleStringProperty(); // This may need to be populated from a TCP message.

    private final User localUser; // This can always be populated immediately.
    private ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * Creates a new SessionModel for a room with the given room code and local user.
     * @param roomCode the room's room code, a 12-length alphanumeric string (letters must be uppercase)
     * @param localUser the local User accessing the room
     */
    public SessionModel(String roomCode, User localUser) {
        if (!roomCode.matches("([A-Z0-9]){12}")) {
            throw new IllegalArgumentException("Room code must be 12 digits long and alphanumeric (uppercase only).");
        }
        if (localUser == null) {
            throw new IllegalArgumentException("Local user for a room cannot be null.");
        }
        setRoomCode(roomCode);
        this.localUser = localUser;
        getUsers().add(localUser);
    }

    /**
     * Gets the current room's room code.
     * @return the current room's room code
     */
    public String getRoomCode() {
        return roomCode.get();
    }

    /**
     * Sets the current room's room code.
     * @param val the new room code
     */
    public void setRoomCode(String val) {roomCode.set(val);}
    public StringProperty roomCodeProperty() {return roomCode;}

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
