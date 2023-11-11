package carleton.sysc4907.controller;

import carleton.sysc4907.model.SessionModel;
import carleton.sysc4907.model.User;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * Controller for the session users menu. This is the menu in the top-right of the diagram editor page,
 * with the user icon. It allows a user to view other users and kick them if they are the host.
 */
public class SessionUsersMenuController {

    @FXML
    private Menu usersMenu;

    private final SessionModel sessionModel;

    /**
     * Creates a SessionUsersMenuController.
     * @param sessionModel the SessionModel for the current session
     */
    public SessionUsersMenuController(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }

    /**
     * Initializes the view, creating menu items and binding properties.
     */
    @FXML
    public void initialize() {
        createUserMenuItems();
        sessionModel.getUsers().addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(Change<? extends User> change) {
                createUserMenuItems(); // re-create the menu items
            }
        });
    }

    /**
     * Creates the menu items corresponding to all users in the room and replaces the current menu items with them.
     */
    private void createUserMenuItems() {
        usersMenu.getItems().clear();
        boolean isHostLocalUser = sessionModel.getHostUser() != null &&
                sessionModel.getHostUser() == sessionModel.getLocalUser();
        sessionModel.getUsers().forEach(user -> {
            boolean canKick = isHostLocalUser && user != sessionModel.getHostUser();
            usersMenu.getItems().add(createUserMenuItem(user, canKick));
        });
    }

    /**
     * Creates the menu item for a particular user.
     * @param user the User that the menu item should correspond to
     * @param canKick true if and only if the current local user is allowed to kick the user
     * @return the created MenuItem
     */
    private MenuItem createUserMenuItem(User user, boolean canKick) {
        HBox itemContent = new HBox();
        itemContent.setPrefWidth(100);

        // Create a label for the username
        Label userLabel = new Label();
        userLabel.setTextFill(Color.BLACK);
        userLabel.textProperty().bind(user.getUsernameProperty());
        itemContent.getChildren().add(userLabel);

        // Create a kick button if needed
        if (canKick) {
            Region separator = new Region();
            HBox.setHgrow(separator, Priority.SOMETIMES);
            itemContent.getChildren().add(separator);
            Button kickButton = new Button("Kick");
            kickButton.getStyleClass().add("button-kick");
            kickButton.setOnAction(actionEvent -> sessionModel.kickUser(user));
            itemContent.getChildren().add(kickButton);
        }
        CustomMenuItem item = new CustomMenuItem(itemContent);
        return item;
    }
}
