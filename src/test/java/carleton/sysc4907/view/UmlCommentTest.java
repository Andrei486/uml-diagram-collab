package carleton.sysc4907.view;

import carleton.sysc4907.controller.element.EditableLabelController;
import carleton.sysc4907.controller.element.ResizeHandleCreator;
import carleton.sysc4907.controller.element.UmlCommentController;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UmlCommentTest extends DiagramElementTest {

    private UmlCommentController controller;

    private ResizeHandleCreator resizeHandleCreator;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
        resizeHandleCreator = new ResizeHandleCreator();
    }

    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(UmlCommentController.class,
                () -> new UmlCommentController(movePreviewCreator, moveCommandFactory, diagramModel, resizeHandleCreator));
        dependencyInjector.addInjectionMethod(EditableLabelController.class,
                EditableLabelController::new);
    }

    @Override
    protected DiagramElement loadElement() {
        try {
            var loader = dependencyInjector.getLoader("view/element/UmlComment.fxml");
            var newElement = (DiagramElement) loader.load();
            controller = loader.getController();
            return newElement;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that the comment becomes editable when double-clicked.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testDoubleClickEditable(FxRobot robot) {
        // Find parts of the editable label
        var editableText = robot.lookup("#editableText").queryAs(TextArea.class);
        var label = robot.lookup("#label").queryAs(Label.class);
        // Check that the editable label shows as a label
        assertFalse(editableText.isVisible());
        assertTrue(label.isVisible());
        // Double click element
        robot.doubleClickOn(element);
        // Check that the editable label shows as a text area
        assertTrue(editableText.isVisible());
        assertFalse(label.isVisible());
    }

    /**
     * Tests that the comment becomes uneditable when unfocused.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testOnUnfocusedNotEditable(FxRobot robot) {
        // Double click
        robot.doubleClickOn(element);
        var editableText = robot.lookup("#editableText").queryAs(TextArea.class);
        var label = robot.lookup("#label").queryAs(Label.class);
        robot.moveBy(100, 100);
        robot.clickOn();
        assertFalse(editableText.isVisible());
        assertTrue(label.isVisible());
    }

    /**
     * Tests that when unfocused after editing, the UML comment's text is correctly set, and is only set once.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testOnUnfocusedValueSetOnce(FxRobot robot) {
        // Double click
        var editableText = robot.lookup("#editableText").queryAs(TextArea.class);
        var label = robot.lookup("#label").queryAs(Label.class);
        AtomicInteger labelChangeEventCounter = new AtomicInteger();
        AtomicInteger controllerChangeEventCounter = new AtomicInteger();
        label.textProperty().addListener((observableValue, s, t1) -> labelChangeEventCounter.getAndIncrement());
        controller.getTextProperty().addListener((observableValue, s, t1) -> controllerChangeEventCounter.getAndIncrement());
        robot.doubleClickOn(element);
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T, KeyCode.ENTER, KeyCode.A, KeyCode.ENTER);
        for (int i = 0; i < 20; i++) {
            robot.type(KeyCode.B);
        }
        String expectedText = """
                test
                a
                bbbbbbbbbbbbbbbbbbbb""";
        robot.moveBy(100, 100);
        robot.clickOn();
        assertFalse(editableText.isVisible());
        assertTrue(label.isVisible());
        assertEquals(expectedText, label.getText());
        assertEquals(expectedText, editableText.getText());
        assertEquals(1, labelChangeEventCounter.get());
        assertEquals(1, controllerChangeEventCounter.get());
    }

    /**
     * Tests that the controller's getText() method returns correct values during various states of editing.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testGetText(FxRobot robot) {
        // Test while not editing
        assertEquals("UML Comment", controller.getText());
        robot.doubleClickOn(element);
        // Test while editing before edits
        assertEquals("UML Comment", controller.getText());
        // Test while editing after edits
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T, KeyCode.ENTER, KeyCode.A);
        assertEquals("UML Comment", controller.getText());
        // Test after edits are applied
        robot.moveBy(100, 100);
        robot.clickOn();
        String expectedText = """
                test
                a""";
        assertEquals(expectedText, controller.getText());
    }

    /**
     * Tests that the controller's setText() method correctly sets the UML comment's text,
     * and that it overwrites current edits.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testSetText(FxRobot robot) {
        var editableText = robot.lookup("#editableText").queryAs(TextArea.class);
        var label = robot.lookup("#label").queryAs(Label.class);
        // Test while not editing
        robot.interact(() -> controller.setText("Test"));
        assertEquals("Test", editableText.getText());
        assertEquals("Test", controller.getText());
        // Test while editing
        robot.doubleClickOn(element);
        robot.type(KeyCode.A, KeyCode.B, KeyCode.C, KeyCode.D, KeyCode.ENTER, KeyCode.A);
        robot.interact(() -> controller.setText("Test2"));
        assertEquals("Test2", editableText.getText());
        assertEquals("Test2", controller.getText());
        // Test that edits are not applied
        robot.moveBy(100, 100);
        robot.clickOn();
        assertEquals("Test2", editableText.getText());
        assertEquals("Test2", label.getText());
        assertEquals("Test2", controller.getText());
    }
}
