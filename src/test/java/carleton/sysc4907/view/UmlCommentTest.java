package carleton.sysc4907.view;

import carleton.sysc4907.controller.EditableLabelController;
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

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
    }

    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(UmlCommentController.class,
                () -> new UmlCommentController(movePreviewCreator, moveCommandFactory, diagramModel));
        dependencyInjector.addInjectionMethod(EditableLabelController.class,
                EditableLabelController::new);
    }

    @Override
    protected DiagramElement loadElement() {
        try {
            return (DiagramElement) dependencyInjector.load("view/element/UmlComment.fxml");
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
        AtomicInteger changeEventCounter = new AtomicInteger();
        label.textProperty().addListener((observableValue, s, t1) -> changeEventCounter.getAndIncrement());
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
        assertEquals(1, changeEventCounter.get());
    }
}
