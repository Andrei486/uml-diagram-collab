package carleton.sysc4907.ui.view;

import carleton.sysc4907.command.EditTextCommandFactory;
import carleton.sysc4907.controller.element.EditableLabelController;
import carleton.sysc4907.controller.element.UmlClassController;
import carleton.sysc4907.model.EditableLabelTracker;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UmlClassTest extends ResizableElementTest {

    private UmlClassController controller;

    @Mock
    private EditableLabelTracker editableLabelTracker;

    private final long testTitleLabelId = 14L;

    private final long testFieldsLabelId = 15L;

    private final long testMethodsLabelId = 16L;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
    }

    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(UmlClassController.class,
                () -> new UmlClassController(movePreviewCreator,
                        moveCommandFactory,
                        diagramModel,
                        resizeHandleCreator,
                        resizePreviewCreator,
                        resizeCommandFactory));
        dependencyInjector.addInjectionMethod(EditableLabelController.class,
                () -> new EditableLabelController(new EditTextCommandFactory(
                        elementIdManager,
                        mockManager,
                        mockExecutedCommandList,
                        mockMessageConstructor),
                        editableLabelTracker
                        ));
    }

    @Override
    protected DiagramElement loadElement() {
        try {
            var loader = dependencyInjector.getLoader("view/element/UmlClass.fxml");
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
    protected void testFieldsDoubleClickEditable(FxRobot robot) {
        // Find parts of the editable label
        var label = robot.lookup("#fieldsLabel").queryAs(Parent.class);
        var fieldsLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        fieldsLabel.setUserData(testFieldsLabelId);
        Mockito.when(elementIdManager.getElementById(testFieldsLabelId)).thenReturn(fieldsLabel);
        // Check that the editable label shows as a label
        assertTrue(label.isVisible());
        // Double click element
        robot.doubleClickOn(label);
        // Check that the editable label shows as a text area
        assertTrue(label.isVisible());
    }

    @Test
    protected void testMethodsDoubleClickEditable(FxRobot robot) {
        // Find parts of the editable label
        var label = robot.lookup("#methodsLabel").queryAs(Parent.class);
        var methodsLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        methodsLabel.setUserData(testMethodsLabelId);
        Mockito.when(elementIdManager.getElementById(testMethodsLabelId)).thenReturn(methodsLabel);
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

        //make sure the other fields did not change
        String fieldsText = controller.getFieldsText();
        assertNotEquals("test", fieldsText);

        String methodsText = controller.getMethodsText();
        assertNotEquals("test", methodsText);
    }

    @Test
    protected void testEditTextFields(FxRobot robot) {
        var label = robot.lookup("#fieldsLabel").queryAs(Parent.class);
        var fieldsLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        fieldsLabel.setUserData(testFieldsLabelId);
        Mockito.when(elementIdManager.getElementById(testFieldsLabelId)).thenReturn(fieldsLabel);
        robot.doubleClickOn(label);
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);

        //click off the element to update the text
        robot.moveBy(100, 100);
        robot.clickOn();

        //check the text
        String fieldsText = controller.getFieldsText();
        assertEquals("test", fieldsText);

        //make sure the other fields did not change
        String titleText = controller.getTitleText();
        assertNotEquals("test", titleText);

        String methodsText = controller.getMethodsText();
        assertNotEquals("test", methodsText);
    }

    @Test
    protected void testEditTextMethods(FxRobot robot) {
        var label = robot.lookup("#methodsLabel").queryAs(Parent.class);
        var methodsLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        methodsLabel.setUserData(testMethodsLabelId);
        Mockito.when(elementIdManager.getElementById(testMethodsLabelId)).thenReturn(methodsLabel);
        robot.doubleClickOn(label);
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);

        //click off the element to update the text
        robot.moveBy(100, 100);
        robot.clickOn();

        //check the text
        String methodsText = controller.getMethodsText();
        assertEquals("test", methodsText);

        //make sure the other fields did not change
        String titleText = controller.getTitleText();
        assertNotEquals("test", titleText);

        String fieldsText = controller.getFieldsText();
        assertNotEquals("test", fieldsText);

    }
}
