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

public class SessionUsersMenuController {

    @FXML
    private Menu usersMenu;

    private final SessionModel sessionModel;

    public SessionUsersMenuController(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }

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

    private void createUserMenuItems() {
        usersMenu.getItems().clear();
        boolean isHostLocalUser = sessionModel.getHostUser() != null &&
                sessionModel.getHostUser() == sessionModel.getLocalUser();
        sessionModel.getUsers().forEach(user -> {
            boolean canKick = isHostLocalUser && user != sessionModel.getHostUser();
            usersMenu.getItems().add(createUserMenuItem(user, canKick));
        });
    }

    private MenuItem createUserMenuItem(User user, boolean canKick) {
        HBox itemContent = new HBox();
        itemContent.setPrefWidth(100);
        // Create a label for the username
        Label userLabel = new Label();
        userLabel.setTextFill(Color.BLACK); // change to style class once stylesheet added
        userLabel.textProperty().bind(user.getUsernameProperty());
//        userLabel.setAlignment(Pos.CENTER_LEFT);
        itemContent.getChildren().add(userLabel);
        // Create a kick button if needed
        if (canKick) {
            Region separator = new Region();
            HBox.setHgrow(separator, Priority.SOMETIMES);
            itemContent.getChildren().add(separator);
            Button kickButton = new Button("Kick");
            kickButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    sessionModel.kickUser(user);
                }
            });
//            kickButton.setAlignment(Pos.CENTER_RIGHT);
            itemContent.getChildren().add(kickButton);
        }
        CustomMenuItem item = new CustomMenuItem(itemContent);
//        item.setHideOnClick(false);
        return item;
    }
}
