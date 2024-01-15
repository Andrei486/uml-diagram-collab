package carleton.sysc4907;

import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.command.AddCommandFactory;
import carleton.sysc4907.command.RemoveCommandFactory;
import carleton.sysc4907.communications.ClientManager;
import carleton.sysc4907.communications.HostManager;
import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.controller.SessionUsersMenuController;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.controller.*;
import carleton.sysc4907.controller.element.*;
import carleton.sysc4907.model.*;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.processing.FontOptionsFinder;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class DiagramEditorLoader {

    private final String TEMPLATE_FILE_PATH = "/carleton/sysc4907/templates.xml";

    private DependencyInjector injector;
    private DiagramModel diagramModel;
    private ElementCreator elementCreator;
    private ElementIdManager elementIdManager;

    public void createAndLoad(Stage stage, String username, String roomCode) throws IOException {
        load(stage, username, roomCode);
        initializeTCPHost(diagramModel, elementCreator, elementIdManager);
        showScene(stage, injector);
    }

    public void loadJoin(Stage stage, String username, String host, int port) throws IOException {
        initializeTCPClient(diagramModel, elementCreator, elementIdManager, host, port);
        load(stage, username, "111111111111");
        showScene(stage, injector);
    }

    private void load(Stage stage, String username, String roomCode) {
        //Create dependency injector to link models and controllers
        injector = new DependencyInjector();
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
        elementIdManager = new ElementIdManager(sessionModel);
        TextFormattingModel textFormattingModel = new TextFormattingModel(fontOptionsFinder);
        diagramModel = new DiagramModel();
        MovePreviewCreator movePreviewCreator = new MovePreviewCreator(elementIdManager);
        ResizeHandleCreator resizeHandleCreator = new ResizeHandleCreator();
        ResizePreviewCreator resizePreviewCreator = new ResizePreviewCreator(elementIdManager);
        DependencyInjector elementControllerInjector = new DependencyInjector();
        try {
            elementCreator = new ElementCreator(elementControllerInjector, TEMPLATE_FILE_PATH, elementIdManager);
        } catch (ParserConfigurationException | SAXException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MoveCommandFactory moveCommandFactory = new MoveCommandFactory(elementIdManager);
        ResizeCommandFactory resizeCommandFactory = new ResizeCommandFactory(elementIdManager);
        AddCommandFactory addCommandFactory = new AddCommandFactory(diagramModel, elementCreator);
        RemoveCommandFactory removeCommandFactory = new RemoveCommandFactory(diagramModel, elementIdManager);

        // Add instantiation methods for the element injector, used to create diagram element controllers
        elementControllerInjector.addInjectionMethod(RectangleController.class,
                () -> new RectangleController(movePreviewCreator, moveCommandFactory, diagramModel,
                        resizeHandleCreator, resizePreviewCreator, resizeCommandFactory));
        elementControllerInjector.addInjectionMethod(UmlCommentController.class,
                () -> new UmlCommentController(movePreviewCreator, moveCommandFactory, diagramModel,
                        resizeHandleCreator, resizePreviewCreator, resizeCommandFactory));
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
                () -> new DiagramMenuBarController(diagramModel, removeCommandFactory));
        injector.addInjectionMethod(DiagramEditingAreaController.class,
                () -> new DiagramEditingAreaController(diagramModel));
        injector.addInjectionMethod(ElementLibraryPanelController.class,
                () -> new ElementLibraryPanelController(diagramModel, addCommandFactory, elementCreator, elementIdManager));
    }

    private void showScene(Stage stage, DependencyInjector injector) throws IOException {
        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/DiagramEditorScreen.fxml"), 1280, 720);
        scene.getStylesheets().add("stylesheet/DiagramEditorScreenStyle.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Diagram Editor");
        stage.show();
    }

    private void initializeTCPHost(
            DiagramModel diagramModel,
            ElementCreator elementCreator,
            ElementIdManager elementIdManager) throws IOException {
        HostManager hostManager = new HostManager(4000, diagramModel, elementCreator, elementIdManager);
    }

    private void initializeTCPClient(
            DiagramModel diagramModel,
            ElementCreator elementCreator,
            ElementIdManager elementIdManager,
            String host,
            int port) throws IOException {
        ClientManager clientManager = new ClientManager(port, host, diagramModel, elementCreator, elementIdManager);
    }
}
