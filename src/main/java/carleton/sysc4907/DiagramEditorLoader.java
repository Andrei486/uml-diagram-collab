package carleton.sysc4907;

import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.controller.SessionUsersMenuController;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.controller.*;
import carleton.sysc4907.controller.element.MovePreviewCreator;
import carleton.sysc4907.controller.element.RectangleController;
import carleton.sysc4907.controller.element.ResizeHandleCreator;
import carleton.sysc4907.controller.element.UmlCommentController;
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
        DiagramModel diagramModel = new DiagramModel();
        MovePreviewCreator movePreviewCreator = new MovePreviewCreator();
        ResizeHandleCreator resizeHandleCreator = new ResizeHandleCreator();
        DependencyInjector elementControllerInjector = new DependencyInjector();
        MoveCommandFactory moveCommandFactory = new MoveCommandFactory();
        // Add instantiation methods for the element injector, used to create diagram element controllers
        elementControllerInjector.addInjectionMethod(RectangleController.class,
                () -> new RectangleController(movePreviewCreator, moveCommandFactory, diagramModel, resizeHandleCreator));
        elementControllerInjector.addInjectionMethod(UmlCommentController.class,
                () -> new UmlCommentController(movePreviewCreator, moveCommandFactory, diagramModel));
        elementControllerInjector.addInjectionMethod(EditableLabelController.class,
                EditableLabelController::new);
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
        stage.setMaximized(true);
        stage.setTitle("Diagram Editor");
        stage.show();
    }
}
