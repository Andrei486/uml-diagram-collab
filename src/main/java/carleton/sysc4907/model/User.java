package carleton.sysc4907.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Represents a user in a room.
 */
public class User {

    private final int id;

    private StringProperty username = new SimpleStringProperty(); // Should this be a property at all? Users should not persist past a room

    private ObjectProperty<PermissionLevel> permissionLevel = new SimpleObjectProperty<>();

    /**
     * Gets the user's ID, which uniquely identifies the user within the room.
     * @return the user's ID number
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username property of this User.
     * @return the StringProperty corresponding to the username
     */
    public StringProperty getUsernameProperty() {
        return username;
    }

    /**
     * Gets the username of this User.
     * @return the User's username
     */
    public String getUsername() {
        return getUsernameProperty().get();
    }

    /**
     * Sets this User's current username.
     * @param username the value to set the username to
     */
    public void setUsername(String username) {
        getUsernameProperty().set(username);
    }

    /**
     * Gets the permission level property for this User.
     * @return the property corresponding to the user's permission level
     */
    public ObjectProperty<PermissionLevel> getPermissionLevelProperty() {
        return permissionLevel;
    }

    /**
     * Gets the current permission level of this User.
     * @return the User's current permission level
     */
    public PermissionLevel getPermissionLevel() {
        return getPermissionLevelProperty().get();
    }

    /**
     * Sets the user's current permission level.
     * @param permissionLevel the new permission level for the user
     */
    public void setPermissionLevel(PermissionLevel permissionLevel) {
        if (permissionLevel == PermissionLevel.HOST && getPermissionLevel() != null) {
            throw new IllegalStateException("Cannot escalate permissions of an existing user to host!");
        }
        getPermissionLevelProperty().set(permissionLevel);
    }

    public User(int id, String username, PermissionLevel permissionLevel) {
        this.id = id;
        setUsername(username);
        setPermissionLevel(permissionLevel);
    }
}
