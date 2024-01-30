package carleton.sysc4907.ui.view;

import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.controller.element.ConnectorHandleCreator;
import carleton.sysc4907.controller.element.pathing.OrthogonalPathStrategy;
import carleton.sysc4907.view.DiagramElement;
import javafx.stage.Stage;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

public class ConnectorTest extends DiagramElementTest {

    private ConnectorHandleCreator connectorHandleCreator;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        connectorHandleCreator = new ConnectorHandleCreator();
        super.start(stage);
    }

    /**
     * Adds additional injection methods, such as for the controller of the diagram element under test.
     */
    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(ConnectorElementController.class,
                () -> new ConnectorElementController(movePreviewCreator,
                        moveCommandFactory,
                        diagramModel,
                        connectorHandleCreator,
                        new OrthogonalPathStrategy()));
    }

    /**
     * Loads the diagram element under test.
     *
     * @return the DiagramElement to test
     */
    @Override
    protected DiagramElement loadElement() {
        try {
            return (DiagramElement) dependencyInjector.load("view/element/Connector.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
