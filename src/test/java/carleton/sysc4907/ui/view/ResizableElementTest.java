package carleton.sysc4907.ui.view;

import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.controller.element.ResizeHandleCreator;
import carleton.sysc4907.controller.element.ResizePreviewCreator;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ResizableElementTest extends DiagramElementTest {

    protected ResizeHandleCreator resizeHandleCreator;

    @Mock
    protected ResizePreviewCreator resizePreviewCreator;
    protected ResizeCommandFactory resizeCommandFactory;

    protected long testResizePreviewId = 14L;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        resizeHandleCreator = new ResizeHandleCreator();
        resizeCommandFactory = new ResizeCommandFactory(elementIdManager);
        super.start(stage);
    }

    /**
     * Tests that the element correctly is resized when the handle is click-dragged.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testResizeOnHandleDragBottomRight(FxRobot robot) {
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        var selectedElements = diagramModel.getSelectedElements();
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        robot.clickOn(element);
        var width = element.getWidth();
        var height = element.getHeight();
        var handles = robot.lookup(".resize-handle").queryAllAs(Rectangle.class);
        // get bottom right handle
        Rectangle brHandle = null;
        double highestX = handles.stream().max(Comparator.comparingInt(o -> (int) o.getLayoutX())).get().getLayoutX();
        for (var handle : handles) {
            if (brHandle == null) {
                brHandle = handle;
            } else if (handle.getLayoutX() == highestX && handle.getLayoutY() >= brHandle.getLayoutY()) {
                brHandle = handle;
            }
        }
        assertNotNull(brHandle);
        robot.drag(brHandle).dropBy(200, 300);
        var newWidth = element.getWidth();
        var newHeight = element.getHeight();
        var deltaX = Math.abs(width + 200 - newWidth);
        var deltaY = Math.abs(height + 300 - newHeight);
        // Check that new size is correct
        assertTrue(deltaX < 10);
        assertTrue(deltaY < 10);
    }

    /**
     * Tests that the element correctly is resized when the handle is click-dragged.
     * @param robot TestFX robot, injected automatically
     */
    @Test
    protected void testResizeOnHandleDragTopLeftMinSize(FxRobot robot) {
        Mockito.when(elementIdManager.getElementById(testId)).thenReturn(element);
        var selectedElements = diagramModel.getSelectedElements();
        assertEquals(0, selectedElements.size());
        assertFalse(element.getStyleClass().contains(SELECTED_STYLE_CLASS));
        robot.clickOn(element);
        var x = element.getLayoutX();
        var y = element.getLayoutY();
        var width = element.getWidth();
        var height = element.getHeight();
        var handles = robot.lookup(".resize-handle").queryAllAs(Rectangle.class);
        // get top left handle
        Rectangle tlHandle = null;
        double lowestX = handles.stream().min(Comparator.comparingInt(o -> (int) o.getLayoutX())).get().getLayoutX();
        for (var handle : handles) {
            if (tlHandle == null) {
                tlHandle = handle;
            } else if (handle.getLayoutX() == lowestX && handle.getLayoutY() <= tlHandle.getLayoutY()) {
                tlHandle = handle;
            }
        }
        assertNotNull(tlHandle);
        robot.drag(tlHandle).dropBy(250, 300);
        var newWidth = element.getWidth();
        var newHeight = element.getHeight();
        // Check that new size is correct
        var deltaX = Math.abs(20 - newWidth);
        var deltaY = Math.abs(20 - newHeight);
        assertTrue(deltaX < 1);
        assertTrue(deltaY < 1);
        // Check that new position is correct
        deltaX = Math.abs(element.getLayoutX() - x - (width - 20));
        deltaY = Math.abs(element.getLayoutY() - y - (height - 20));
        assertTrue(deltaX < 1);
        assertTrue(deltaY < 1);
    }
}
