package carleton.sysc4907;

import carleton.sysc4907.controller.*;
import carleton.sysc4907.controller.element.MovePreviewCreator;
import carleton.sysc4907.controller.element.RectangleController;
import carleton.sysc4907.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //Create dependency injector to link models and controllers
        DependencyInjector injector = new DependencyInjector();
        //Create the models and supporting classes
        FontOptionsFinder fontOptionsFinder = new FontOptionsFinder();
        UserFactory userFactory = new UserFactory();
        List<User> guestUsers = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            guestUsers.add(userFactory.createGuestUser("Guest" + i));
        }
        User hostUser = userFactory.createHostUser("Host");
        SessionModel sessionModel = new SessionModel("TEST1234TEST", hostUser);
        guestUsers.forEach(sessionModel::addUser);
        TextFormattingModel textFormattingModel = new TextFormattingModel(fontOptionsFinder);
        DiagramModel diagramModel = new DiagramModel();
        MovePreviewCreator movePreviewCreator = new MovePreviewCreator();
        DependencyInjector elementControllerInjector = new DependencyInjector();
        // Add instantiation methods for the element injector, used to create diagram element controllers
        elementControllerInjector.addInjectionMethod(RectangleController.class,
                () -> new RectangleController(movePreviewCreator, diagramModel));
        // Add instantiation methods to the main dependency injector, used to create UI elements
        injector.addInjectionMethod(SessionInfoBarController.class,
                () -> new SessionInfoBarController(sessionModel));
        injector.addInjectionMethod(FormattingPanelController.class,
                () -> new FormattingPanelController(textFormattingModel));
        injector.addInjectionMethod(SessionUsersMenuController.class,
                () -> new SessionUsersMenuController(sessionModel));
        injector.addInjectionMethod(DiagramMenuBarController.class,
                () -> new DiagramMenuBarController(diagramModel));
        injector.addInjectionMethod(DiagramEditingAreaController.class,
                () -> new DiagramEditingAreaController(diagramModel));
        injector.addInjectionMethod(ElementLibraryPanelController.class,
                () -> new ElementLibraryPanelController(diagramModel, elementControllerInjector));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/DiagramEditorScreen.fxml"), 1280, 720);
        scene.getStylesheets().add("stylesheet/DiagramEditorScreenStyle.css");
        stage.setScene(scene);
        stage.setTitle("Diagram Editor");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}