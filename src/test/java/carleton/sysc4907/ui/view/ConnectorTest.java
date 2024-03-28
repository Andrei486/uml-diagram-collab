package carleton.sysc4907.ui.view;

import carleton.sysc4907.command.ConnectorMovePointCommandFactory;
import carleton.sysc4907.command.ConnectorSnapCommandFactory;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.controller.element.ConnectorHandleCreator;
import carleton.sysc4907.controller.element.ConnectorMovePointPreviewCreator;
import carleton.sysc4907.controller.element.pathing.OrthogonalPathStrategy;
import carleton.sysc4907.model.SnapHandleProvider;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.view.EditingAreaLayer;
import carleton.sysc4907.view.SnapHandle;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ConnectorTest extends DiagramElementTest {

    protected ConnectorElementController controller;
    protected ConnectorHandleCreator connectorHandleCreator;

    @Mock
    protected ConnectorMovePointPreviewCreator mockConnectorMovePointPreviewCreator;
    protected ConnectorMovePointCommandFactory connectorMovePointCommandFactory;
    protected ConnectorSnapCommandFactory connectorSnapCommandFactory;
    @Mock
    private MessageConstructor mockMessageConstructor;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        connectorHandleCreator = new ConnectorHandleCreator();
        connectorMovePointCommandFactory = new ConnectorMovePointCommandFactory(elementIdManager, mockManager, mockExecutedCommandList, mockMessageConstructor);
        connectorSnapCommandFactory = new ConnectorSnapCommandFactory(elementIdManager, mockManager, mockExecutedCommandList, mockMessageConstructor);
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
                        mockConnectorMovePointPreviewCreator,
                        connectorMovePointCommandFactory,
                        connectorSnapCommandFactory,
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

    @Test
    protected void testDragResizeCancel(FxRobot robot) {
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
        robot.drag(endHandle, MouseButton.PRIMARY).moveBy(-50, 200).clickOn(MouseButton.SECONDARY).drop();
        var newEndX = controller.getEndX();
        var newEndY = controller.getEndY();
        // Check that new position is correct
        assertEquals(endX, newEndX);
        assertEquals(endY, newEndY);
        verify(elementIdManager, never()).getElementById(testId);
    }

    private SnapHandle constructDummySnapHandle(long snapHandleId) {
        var node = new DiagramElement();
        var snapHandle = new SnapHandle();
        snapHandle.setUserData(snapHandleId);
        snapHandle.setHandleVisible(true);
        snapHandle.setHorizontal(false);
        node.getChildren().add(snapHandle);
        node.setViewOrder(EditingAreaLayer.ELEMENT.getViewOrder());
        return snapHandle;
    }

    @Test
    protected void testSnapStartToHandle(FxRobot robot) throws InterruptedException {
        long testSnapHandleId = 9L;
        // Set up snap handle and node for it
        var snapHandle = constructDummySnapHandle(testSnapHandleId);
        var snapElement = (Node) snapHandle.getParent();
        Platform.runLater(() -> {
            editingArea.getChildren().add(snapElement);
            snapElement.setLayoutX(300);
            snapElement.setLayoutY(50);
        });
        TimeUnit.MILLISECONDS.sleep(500); // wait for runLater to execute
        var snapHandleProvider = SnapHandleProvider.getSingleton();
        snapHandleProvider.getSnapHandleList().add(snapHandle);
        // Stub appropriate methods
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        Mockito.when(elementIdManager.getElementControllerById(testId)).thenReturn(controller);
        Mockito.when(elementIdManager.getElementById(testSnapHandleId)).thenReturn(snapHandle);

        // Act
        robot.clickOn(element);
        var handles = robot.lookup(".resize-handle").queryAllAs(Rectangle.class);
        // get handle
        Rectangle startHandle = null;
        var startX = controller.getStartX();
        var startY = controller.getStartY();
        for (var handle : handles) {
            if (almostEqual(handle.getLayoutX(), startX) && almostEqual(handle.getLayoutY(), startY)) {
                startHandle = handle;
            }
        }
        assertNotNull(startHandle);
        // Drag handle over the snap handle
        robot.drag(startHandle).dropTo(snapHandle);

        // Check that snapping happened correctly and that the values are bound
        assertTrue(snapHandle.getSnappedConnectorIds().contains(testId));
        assertFalse(controller.getIsStartHorizontal());
        assertTrue(controller.getSnapStart());
        assertThrows(Exception.class, () -> controller.setStartX(100));
        assertThrows(Exception.class, () -> controller.setStartY(100));

        // Check that the coordinates follow the snap handle
        startX = controller.getStartX();
        startY = controller.getStartY();
        snapElement.setLayoutX(150);
        snapElement.setLayoutY(150);
        var newStartX = controller.getStartX();
        var newStartY = controller.getStartY();
        assertTrue(almostEqual(startX - 150, newStartX));
        assertTrue(almostEqual(startY + 100, newStartY));
    }

    @Test
    protected void testSnapEndToHandle(FxRobot robot) throws InterruptedException {
        long testSnapHandleId = 9L;
        // Set up snap handle and node for it
        var snapHandle = constructDummySnapHandle(testSnapHandleId);
        var snapElement = (Node) snapHandle.getParent();
        Platform.runLater(() -> {
            editingArea.getChildren().add(snapElement);
            snapElement.setLayoutX(300);
            snapElement.setLayoutY(50);
        });
        TimeUnit.MILLISECONDS.sleep(500); // wait for runLater to execute
        var snapHandleProvider = SnapHandleProvider.getSingleton();
        snapHandleProvider.getSnapHandleList().add(snapHandle);
        // Stub appropriate methods
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        Mockito.when(elementIdManager.getElementControllerById(testId)).thenReturn(controller);
        Mockito.when(elementIdManager.getElementById(testSnapHandleId)).thenReturn(snapHandle);

        // Act
        robot.clickOn(element);
        var handles = robot.lookup(".resize-handle").queryAllAs(Rectangle.class);
        // get handle
        Rectangle endHandle = null;
        var endX = controller.getEndX();
        var endY = controller.getEndY();
        for (var handle : handles) {
            if (almostEqual(handle.getLayoutX(), endX) && almostEqual(handle.getLayoutY(), endY)) {
                endHandle = handle;
            }
        }
        assertNotNull(endHandle);
        // Drag handle over the snap handle
        robot.drag(endHandle).dropTo(snapHandle);

        // Check that snapping happened correctly and that the values are bound
        assertTrue(snapHandle.getSnappedConnectorIds().contains(testId));
        assertFalse(controller.getIsEndHorizontal());
        assertTrue(controller.getSnapEnd());
        assertThrows(Exception.class, () -> controller.setEndX(100));
        assertThrows(Exception.class, () -> controller.setEndY(100));

        // Check that the coordinates follow the snap handle
        endX = controller.getEndX();
        endY = controller.getEndY();
        snapElement.setLayoutX(150);
        snapElement.setLayoutY(150);
        var newEndX = controller.getEndX();
        var newEndY = controller.getEndY();
        assertTrue(almostEqual(endX - 150, newEndX));
        assertTrue(almostEqual(endY + 100, newEndY));
    }

    @Test
    protected void testUnsnapByMovingPoint(FxRobot robot) throws InterruptedException {
        long testSnapHandleId = 9L;
        long testSnapHandleId2 = 99L;
        // Set up snap handle and node for it
        var snapHandle = constructDummySnapHandle(testSnapHandleId);
        snapHandle.setHorizontal(true);
        var snapElement = (Node) snapHandle.getParent();
        var snapHandle2 = constructDummySnapHandle(testSnapHandleId2);
        snapHandle2.setHorizontal(true);
        var snapElement2 = (Node) snapHandle2.getParent();
        Platform.runLater(() -> {
            editingArea.getChildren().add(snapElement);
            editingArea.getChildren().add(snapElement2);
            snapElement.setLayoutX(300);
            snapElement.setLayoutY(50);
        });
        TimeUnit.MILLISECONDS.sleep(500); // wait for runLater to execute
        var snapHandleProvider = SnapHandleProvider.getSingleton();
        snapHandleProvider.getSnapHandleList().add(snapHandle);
        snapHandleProvider.getSnapHandleList().add(snapHandle2);
        // Stub appropriate methods
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        controller.snapToHandle(snapHandle, true);
        controller.snapToHandle(snapHandle2, false);
        assertTrue(controller.getSnapStart());
        assertTrue(controller.getSnapEnd());

        // Act
        robot.clickOn(element);
        var handles = robot.lookup(".resize-handle").queryAllAs(Rectangle.class);
        // get handle
        Rectangle startHandle = null;
        var startX = controller.getStartX();
        var startY = controller.getStartY();
        for (var handle : handles) {
            if (almostEqual(handle.getLayoutX() + handle.getParent().getLayoutX(), startX)
                    && almostEqual(handle.getLayoutY() + handle.getParent().getLayoutY(), startY)) {
                startHandle = handle;
            }
        }
        assertNotNull(startHandle);
        // Drag handle over the snap handle
        robot.drag(startHandle).dropBy(100, 100);

        // Check that unsnapping happened correctly and that the point was correctly moved
        assertFalse(snapHandle.getSnappedConnectorIds().contains(testId));
        assertFalse(controller.getSnapStart());
        // Check that other point is still snapped
        assertTrue(snapHandle2.getSnappedConnectorIds().contains(testId));
        assertTrue(controller.getSnapEnd());
        var newStartX = controller.getStartX();
        var newStartY = controller.getStartY();
        // Check that new position is correct
        assertTrue(almostEqual(startX + 100, newStartX));
        assertTrue(almostEqual(startY + 100, newStartY));
    }

    @Test
    protected void testUnsnapByMovingConnector(FxRobot robot) throws InterruptedException {
        long testSnapHandleId = 9L;
        long testSnapHandleId2 = 99L;
        // Set up snap handle and node for it
        var snapHandle = constructDummySnapHandle(testSnapHandleId);
        snapHandle.setHorizontal(true);
        var snapElement = (Node) snapHandle.getParent();
        var snapHandle2 = constructDummySnapHandle(testSnapHandleId2);
        snapHandle2.setHorizontal(true);
        var snapElement2 = (Node) snapHandle2.getParent();
        snapElement.setViewOrder(EditingAreaLayer.ELEMENT.getViewOrder());
        Platform.runLater(() -> {
            editingArea.getChildren().add(snapElement);
            editingArea.getChildren().add(snapElement2);
            snapElement.setLayoutX(300);
            snapElement.setLayoutY(50);
        });
        TimeUnit.MILLISECONDS.sleep(500); // wait for runLater to execute
        var snapHandleProvider = SnapHandleProvider.getSingleton();
        snapHandleProvider.getSnapHandleList().add(snapHandle);
        snapHandleProvider.getSnapHandleList().add(snapHandle2);
        // Stub appropriate methods
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        controller.snapToHandle(snapHandle, false);
        controller.snapToHandle(snapHandle2, true);
        assertTrue(controller.getSnapStart());
        assertTrue(controller.getSnapEnd());

        robot.drag(element).dropBy(100, 150);
        // Check that points are no longer snapped
        assertFalse(controller.getSnapStart());
        assertFalse(controller.getSnapEnd());
        assertFalse(snapHandle.getSnappedConnectorIds().contains(testId));
        assertFalse(snapHandle2.getSnappedConnectorIds().contains(testId));
    }
}
