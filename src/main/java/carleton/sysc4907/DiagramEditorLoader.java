package carleton.sysc4907;

import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.controller.SessionUsersMenuController;
import carleton.sysc4907.model.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DiagramEditorLoader {

    public void load(Stage stage, String username, String roomCode) throws IOException {
        //Create dependency injector to link models and controllers
        DependencyInjector injector = new DependencyInjector();
        //Create the models and supporting classes
        FontOptionsFinder fontOptionsFinder = new FontOptionsFinder();
        UserFactory userFactory = new UserFactory();
        List<User> guestUsers = new LinkedList<>();
        //create example guest users
        for (int i = 0; i < 4; i++) {
            guestUsers.add(userFactory.createGuestUser("Guest" + i));
        }

        User hostUser = userFactory.createHostUser(username);
        SessionModel sessionModel = new SessionModel(roomCode, hostUser);
        guestUsers.forEach(sessionModel::addUser);
        TextFormattingModel textFormattingModel = new TextFormattingModel(fontOptionsFinder);

        //Add instantiation methods to the dependency injector
        injector.addInjectionMethod(SessionInfoBarController.class,
                () -> new SessionInfoBarController(sessionModel));
        injector.addInjectionMethod(FormattingPanelController.class,
                () -> new FormattingPanelController(textFormattingModel));
        injector.addInjectionMethod(SessionUsersMenuController.class,
                () -> new SessionUsersMenuController(sessionModel));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/DiagramEditorScreen.fxml"), 1280, 720);
        scene.getStylesheets().add("stylesheet/DiagramEditorScreenStyle.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Diagram Editor");
        stage.show();
    }
}
