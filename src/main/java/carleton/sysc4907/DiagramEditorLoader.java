package carleton.sysc4907;

import carleton.sysc4907.command.*;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.command.AddCommandFactory;
import carleton.sysc4907.command.RemoveCommandFactory;
import carleton.sysc4907.communications.ClientManager;
import carleton.sysc4907.communications.HostManager;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageInterpreter;
import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.controller.SessionUsersMenuController;
import carleton.sysc4907.controller.*;
import carleton.sysc4907.controller.element.*;
import carleton.sysc4907.model.*;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.processing.FontOptionsFinder;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class DiagramEditorLoader {

    private final String TEMPLATE_FILE_PATH = "/carleton/sysc4907/templates.xml";

    private DependencyInjector injector;

    private MessageInterpreter interpreter;

    /**
     * Creates a new diagram room and loads the editor. This method is to be used for hosting a diagram.
     * @param stage the stage
     * @param username the username to use
     * @param roomCode the room code generated
     * @throws IOException when an error has occurred loading the editor or initializing TCP
     */
    public void createAndLoad(Stage stage, String username, String roomCode) throws IOException {
        var manager = initializeTCPHost();
        load(username, roomCode, manager);
        showScene(stage, injector, manager);
    }

    /**
     * Connects to the diagram as a client and loads the editor
     * @param stage the stage
     * @param username the username to join with
     * @param host the IP of the host to join
     * @param port the port number on the host
     * @throws IOException when an error has occurred loading the editor or initializing TCP
     */
    public void loadJoin(Stage stage, String username, String host, int port) throws IOException {
        var manager = initializeTCPClient(host, port);
        load(username, "111111111111", manager);
        showScene(stage, injector, manager);
    }

    /**
     * Load required resources for the scene and instantiate required objects
     * @param username the username to use
     * @param roomCode the room code of the room to load
     * @param manager the TCP manager
     */
    private void load(String username, String roomCode, Manager manager) {
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
        ElementIdManager elementIdManager = new ElementIdManager(sessionModel);
        TextFormattingModel textFormattingModel = new TextFormattingModel(fontOptionsFinder);
        DiagramModel diagramModel = new DiagramModel();
        MovePreviewCreator movePreviewCreator = new MovePreviewCreator(elementIdManager);
        ResizeHandleCreator resizeHandleCreator = new ResizeHandleCreator();
        ResizePreviewCreator resizePreviewCreator = new ResizePreviewCreator(elementIdManager);
        DependencyInjector elementControllerInjector = new DependencyInjector();
        ElementCreator elementCreator;
        try {
            elementCreator = new ElementCreator(elementControllerInjector, TEMPLATE_FILE_PATH, elementIdManager);
        } catch (ParserConfigurationException | SAXException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MoveCommandFactory moveCommandFactory = new MoveCommandFactory(elementIdManager, manager);
        ResizeCommandFactory resizeCommandFactory = new ResizeCommandFactory(elementIdManager, manager);
        AddCommandFactory addCommandFactory = new AddCommandFactory(diagramModel, elementCreator, manager);
        RemoveCommandFactory removeCommandFactory = new RemoveCommandFactory(diagramModel, elementIdManager, manager);
        EditTextCommandFactory editTextCommandFactory = new EditTextCommandFactory(elementIdManager);
        // Add factories to message interpreter: avoids circular dependencies
        interpreter.addFactories(
                addCommandFactory,
                removeCommandFactory,
                moveCommandFactory,
                resizeCommandFactory
        );

        // Add instantiation methods for the element injector, used to create diagram element controllers
        elementControllerInjector.addInjectionMethod(RectangleController.class,
                () -> new RectangleController(movePreviewCreator, moveCommandFactory, diagramModel,
                        resizeHandleCreator, resizePreviewCreator, resizeCommandFactory));
        elementControllerInjector.addInjectionMethod(UmlCommentController.class,
                () -> new UmlCommentController(movePreviewCreator, moveCommandFactory, diagramModel,
                        resizeHandleCreator, resizePreviewCreator, resizeCommandFactory));
        elementControllerInjector.addInjectionMethod(EditableLabelController.class,
                () -> new EditableLabelController(editTextCommandFactory));

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

    /**
     * Opens the editor screen.
     * @param stage the stage to open on.
     * @param injector the dependancy injector
     * @param manager the TCP manager
     * @throws IOException when loading the resources required for the scene fails
     */
    private void showScene(Stage stage, DependencyInjector injector, Manager manager) throws IOException {
        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/DiagramEditorScreen.fxml"), 1280, 720);
        scene.getStylesheets().add("stylesheet/DiagramEditorScreenStyle.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Diagram Editor");
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> manager.close());
        stage.show();
    }

    /**
     * Initializes TCP for the host user
     * @return the TCP host manager for the user
     * @throws IOException when the host manager could not be initialized
     */
    private Manager initializeTCPHost() throws IOException {
        interpreter = new MessageInterpreter();
        return new HostManager(4000, interpreter);
    }

    /**
     * Initializes TCP for the client user
     * @param host the IP address of the host
     * @param port the port on the host
     * @return the TCP client manager for the user
     * @throws IOException when the client manager could not be initialized
     */
    private Manager initializeTCPClient(
            String host,
            int port) throws IOException {
        interpreter = new MessageInterpreter();
        return new ClientManager(port, host, interpreter);
    }
}
