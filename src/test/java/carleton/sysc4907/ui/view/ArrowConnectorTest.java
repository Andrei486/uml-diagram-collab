package carleton.sysc4907.ui.view;

import carleton.sysc4907.controller.element.ArrowConnectorElementController;
import carleton.sysc4907.controller.element.arrows.ArrowheadFactory;
import carleton.sysc4907.controller.element.pathing.OrthogonalPathStrategy;
import carleton.sysc4907.view.DiagramElement;
import javafx.stage.Stage;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

public class ArrowConnectorTest extends ConnectorTest {

    private ArrowConnectorElementController arrowController;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
        arrowController = (ArrowConnectorElementController) controller;
    }

    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(ArrowConnectorElementController.class,
                () -> new ArrowConnectorElementController(
                        movePreviewCreator,
                        moveCommandFactory,
                        diagramModel,
                        connectorHandleCreator,
                        mockConnectorMovePointPreviewCreator,
                        connectorMovePointCommandFactory,
                        new OrthogonalPathStrategy(),
                        new ArrowheadFactory()));
    }

    @Override
    protected DiagramElement loadElement() {
        try {
            return (DiagramElement) dependencyInjector.load("view/element/ArrowConnector.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
