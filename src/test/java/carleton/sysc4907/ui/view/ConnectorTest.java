package carleton.sysc4907.ui.view;

import carleton.sysc4907.command.ConnectorMovePointCommandFactory;
import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.controller.element.ConnectorHandleCreator;
import carleton.sysc4907.controller.element.pathing.OrthogonalPathStrategy;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectorTest extends DiagramElementTest {

    private ConnectorElementController controller;
    private ConnectorHandleCreator connectorHandleCreator;
    private ConnectorMovePointCommandFactory connectorMovePointCommandFactory;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        connectorHandleCreator = new ConnectorHandleCreator();
        connectorMovePointCommandFactory = new ConnectorMovePointCommandFactory(elementIdManager, mockManager);
        super.start(stage);
        controller = (ConnectorElementController) element.getProperties().get("controller");
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
                        connectorMovePointCommandFactory,
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

    private boolean almostEqual(double a, double b){
        System.out.println(a + " " + b);
        return Math.abs(a-b)<10;
    }

    @Test
    protected void testDragMoveStartPoint(FxRobot robot) {
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        var selectedElements = diagramModel.getSelectedElements();
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        robot.clickOn(element);
        var handles = robot.lookup(".resize-handle").queryAllAs(Rectangle.class);
        // get start handle
        Rectangle startHandle = null;
        var startX = controller.getStartX();
        var startY = controller.getStartY();
        for (var handle : handles) {
            if (almostEqual(handle.getLayoutX(), startX) && almostEqual(handle.getLayoutY(), startY)) {
                startHandle = handle;
            }
        }
        assertNotNull(startHandle);
        robot.drag(startHandle).dropBy(200, 300);
        var newStartX = controller.getStartX();
        var newStartY = controller.getStartY();
        // Check that new position is correct
        assertTrue(almostEqual(startX + 200, newStartX));
        assertTrue(almostEqual(startY + 300, newStartY));
    }

    @Test
    protected void testDragMoveEndPoint(FxRobot robot) {
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        var selectedElements = diagramModel.getSelectedElements();
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        robot.clickOn(element);
        var handles = robot.lookup(".resize-handle").queryAllAs(Rectangle.class);
        // get end handle
        Rectangle endHandle = null;
        var endX = controller.getEndX();
        var endY = controller.getEndY();
        for (var handle : handles) {
            if (almostEqual(handle.getLayoutX(), endX) && almostEqual(handle.getLayoutY(), endY)) {
                endHandle = handle;
            }
        }
        assertNotNull(endHandle);
        robot.drag(endHandle).dropBy(-50, 200);
        var newEndX = controller.getEndX();
        var newEndY = controller.getEndY();
        // Check that new position is correct
        assertTrue(almostEqual(endX - 50, newEndX));
        assertTrue(almostEqual(endY + 200, newEndY));
    }
}
