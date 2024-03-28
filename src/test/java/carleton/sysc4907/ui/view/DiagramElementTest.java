package carleton.sysc4907.ui.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.controller.DiagramEditingAreaController;
import carleton.sysc4907.controller.element.MovePreviewCreator;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.controller.element.RectangleController;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.TextFormattingModel;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Base class for UI tests on diagram elements within an editing area.
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public abstract class DiagramElementTest {

    protected final String SELECTED_STYLE_CLASS = "selected-element";
    protected DiagramModel diagramModel;

    @Mock
    protected MovePreviewCreator movePreviewCreator;

    protected MoveCommandFactory moveCommandFactory;

    protected DependencyInjector dependencyInjector;

    protected Pane editingArea;

    protected DiagramElement element;

    @Mock
    protected Manager mockManager;

    protected long testId = 12L;

    @Mock
    protected ElementIdManager elementIdManager;
    @Mock
    protected ExecutedCommandList mockExecutedCommandList;
    @Mock
    protected MessageConstructor mockMessageConstructor;

    /**
     * Sets up the scene before each test, loading an editing area and adding the element inside it.
     * @param stage Stage used for testing
     * @throws IOException if an FXML path is incorrect
     */
    @Start
    protected void start(Stage stage) throws IOException {
        diagramModel = new DiagramModel();
        moveCommandFactory = new MoveCommandFactory(elementIdManager, mockManager, mockExecutedCommandList, mockMessageConstructor);
        initializeDependencyInjector();
        // Load the scroll pane and get the editing area from it
        ScrollPane root = (ScrollPane) dependencyInjector.load("view/DiagramEditingArea.fxml");
        editingArea = (Pane) root.getContent();
        // Load the element and add it
        element = loadElement();
        element.setUserData(12L);
        editingArea.getChildren().add(element);
        diagramModel.getElements().add(element);
        // Make sure commands are run as host, running them as client does nothing
        lenient().when(mockManager.isHost()).thenReturn(true);
        // Show the scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the dependency injector used to create FXML controllers.
     */
    private void initializeDependencyInjector() {
        dependencyInjector = new DependencyInjector();
        dependencyInjector.addInjectionMethod(DiagramEditingAreaController.class,
                () -> new DiagramEditingAreaController(diagramModel));
        addInjectionMethods();
    }

    /**
     * Adds additional injection methods, such as for the controller of the diagram element under test.
     */
    protected abstract void addInjectionMethods();

    /**
     * Loads the diagram element under test.
     * @return the DiagramElement to test
     */
    protected abstract DiagramElement loadElement();

    /**
     * Tests that the element correctly becomes selected when clicked.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testSelectOnClick(FxRobot robot) {
        var selectedElements = diagramModel.getSelectedElements();
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        robot.clickOn(element);
        assertTrue(selectedElements.contains(element));
        assertTrue(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
    }

    /**
     * Tests that the element is not selected when right-clicked.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testNoSelectOnRightClick(FxRobot robot) {
        var selectedElements = diagramModel.getSelectedElements();
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        robot.rightClickOn(element);
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
    }

    /**
     * Tests that the element is dragged when left-click-dragged.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testDragMove(FxRobot robot) {
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        var x = element.getLayoutX();
        var y = element.getLayoutY();
        robot.drag(element).dropBy(100, 150);
        var deltaX = Math.abs(x+100 - element.getLayoutX());
        var deltaY = Math.abs(y+150 - element.getLayoutY());
        // Check that dragged position is correct
        assertTrue(deltaX < 10);
        assertTrue(deltaY < 10);
    }

    @Test
    protected void testDragMoveCancel(FxRobot robot) {
        var x = element.getLayoutX();
        var y = element.getLayoutY();
        robot.drag(element, MouseButton.PRIMARY).moveBy(100, 150).clickOn(MouseButton.SECONDARY).drop();
        // Check that dragged position is correct
        assertEquals(x, element.getLayoutX());
        assertEquals(y, element.getLayoutY());
        verify(elementIdManager, never()).getElementById(testId);
    }

    /**
     * Tests that the element correctly becomes deselected when CTRL-clicked.
     * @param robot TestFX robot, injected automatically
     * @throws InterruptedException when the wait is interrupted
     */
    @Test
    protected void testCtrlClickDeselect(FxRobot robot) throws InterruptedException {
        var selectedElements = diagramModel.getSelectedElements();
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        robot.press(KeyCode.CONTROL);
        robot.clickOn(element);
        robot.release(KeyCode.CONTROL);
        assertTrue(selectedElements.contains(element));
        assertTrue(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        TimeUnit.MILLISECONDS.sleep(500); // Wait so the second click is not considered a double click
        robot.press(KeyCode.CONTROL);
        robot.clickOn(element);
        robot.release(KeyCode.CONTROL);
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
    }
}
