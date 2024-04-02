package carleton.sysc4907.ui.view;

import carleton.sysc4907.controller.element.UmlBoxElementController;
import carleton.sysc4907.controller.element.UmlClassController;
import carleton.sysc4907.controller.element.UmlEnumElementController;
import carleton.sysc4907.model.EditableLabelTracker;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public abstract class UmlBoxElementTest extends ResizableElementTest {

    protected final long testTitleLabelId = 14L;

    protected final long testEntriesLabelId = 15L;

    protected UmlBoxElementController controller;

    @Mock
    protected EditableLabelTracker editableLabelTracker;


    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
    }

    @Test
    protected void testTitleDoubleClickEditable(FxRobot robot) {
        // Find parts of the editable label
        var label = robot.lookup("#titleLabel").queryAs(Parent.class);
        var titleLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        titleLabel.setUserData(testTitleLabelId);
        Mockito.when(elementIdManager.getElementById(testTitleLabelId)).thenReturn(titleLabel);
        // Check that the editable label shows as a label
        assertTrue(label.isVisible());
        // Double click element
        robot.doubleClickOn(label);
        // Check that the editable label shows as a text area
        assertTrue(label.isVisible());
    }

    @Test
    protected void testEntriesDoubleClickEditable(FxRobot robot) {
        // Find parts of the editable label
        var label = robot.lookup("#entriesLabel").queryAs(Parent.class);
        var fieldsLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        fieldsLabel.setUserData(testEntriesLabelId);
        Mockito.when(elementIdManager.getElementById(testEntriesLabelId)).thenReturn(fieldsLabel);
        // Check that the editable label shows as a label
        assertTrue(label.isVisible());
        // Double click element
        robot.doubleClickOn(label);
        // Check that the editable label shows as a text area
        assertTrue(label.isVisible());
    }

    @Test
    protected void testEditTextTitle(FxRobot robot) {
        var label = robot.lookup("#titleLabel").queryAs(Parent.class);
        var titleLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        titleLabel.setUserData(testTitleLabelId);
        Mockito.when(elementIdManager.getElementById(testTitleLabelId)).thenReturn(titleLabel);
        robot.doubleClickOn(label);
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);

        //click off the element to update the text
        robot.moveBy(100, 100);
        robot.clickOn();

        //check the text
        String titleText = controller.getTitleText();
        assertEquals("test", titleText);

        //make sure the other field did not change
        String methodsText = controller.getEntriesText();
        assertNotEquals("test", methodsText);
    }

    @Test
    protected void testEditTextEntries(FxRobot robot) {
        var label = robot.lookup("#entriesLabel").queryAs(Parent.class);
        var fieldsLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        fieldsLabel.setUserData(testEntriesLabelId);
        Mockito.when(elementIdManager.getElementById(testEntriesLabelId)).thenReturn(fieldsLabel);
        robot.doubleClickOn(label);
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);

        //click off the element to update the text
        robot.moveBy(100, 100);
        robot.clickOn();

        //check the text
        String entriesText = controller.getEntriesText();
        assertEquals("test", entriesText);

        //make sure the other field did not change
        String titleText = controller.getTitleText();
        assertNotEquals("test", titleText);

    }

}
