package carleton.sysc4907.communications;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.AddCommandFactory;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.RemoveCommandFactory;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.controller.*;
import carleton.sysc4907.controller.element.*;
import carleton.sysc4907.model.*;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.processing.FontOptionsFinder;
import javafx.scene.Scene;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class ComMain {

    private static final String TEMPLATE_FILE_PATH = "/carleton/sysc4907/templates.xml";
    private static String username = "Host";
    private static String roomCode = "111111111111";
    public static void main(String[] args) throws ParserConfigurationException, IOException, URISyntaxException, SAXException {
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
        }

        //TODO
        MoveCommandFactory moveCommandFactory = new MoveCommandFactory(elementIdManager, null);
        ResizeCommandFactory resizeCommandFactory = new ResizeCommandFactory(elementIdManager, null);
        AddCommandFactory addCommandFactory = new AddCommandFactory(diagramModel, elementCreator, null);
        RemoveCommandFactory removeCommandFactory = new RemoveCommandFactory(diagramModel, elementIdManager, null);

        // Add instantiation methods for the element injector, used to create diagram element controllers
        elementControllerInjector.addInjectionMethod(RectangleController.class,
                () -> new RectangleController(movePreviewCreator, moveCommandFactory, diagramModel,
                        resizeHandleCreator, resizePreviewCreator, resizeCommandFactory));
        elementControllerInjector.addInjectionMethod(UmlCommentController.class,
                () -> new UmlCommentController(movePreviewCreator, moveCommandFactory, diagramModel,
                        resizeHandleCreator, resizePreviewCreator, resizeCommandFactory));
        elementControllerInjector.addInjectionMethod(EditableLabelController.class,
                EditableLabelController::new);

        MessageInterpreter messageInterpreter = new MessageInterpreter(addCommandFactory, removeCommandFactory, moveCommandFactory, resizeCommandFactory);

        HostManager hostManager = new HostManager(4000, messageInterpreter);

    }
}
