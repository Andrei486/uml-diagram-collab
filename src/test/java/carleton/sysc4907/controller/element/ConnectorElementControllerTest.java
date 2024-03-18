package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.ConnectorMovePointCommandFactory;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.controller.element.pathing.PathingStrategy;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConnectorElementControllerTest {

    @Mock
    protected MovePreviewCreator movePreviewCreator;
    @Mock
    protected MoveCommandFactory moveCommandFactory;
    @Mock
    protected DiagramModel diagramModel;
    @Mock
    protected ConnectorHandleCreator connectorHandleCreator;

    @Mock
    protected ConnectorMovePointPreviewCreator connectorMovePointPreviewCreator;
    @Mock
    protected ConnectorMovePointCommandFactory connectorMovePointCommandFactory;
    @Mock
    protected PathingStrategy pathingStrategy;
    @Mock
    protected ObservableList<DiagramElement> mockSelectedElementsList;

    protected ConnectorElementController createController() {
        return new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                pathingStrategy
        );
    }

    @Test
    public void updateDirectionBothSnapped() {
        Mockito.when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = createController();
        // Snap to vertical
        controller.setIsStartHorizontal(false);
        controller.setIsEndHorizontal(false);
        controller.setSnapStart(true);
        controller.setSnapEnd(true);

        // Make the line horizontal, but it shouldn't change because it's snapped
        controller.setStartX(0);
        controller.setStartY(0);
        controller.setEndX(200);
        controller.setEndY(10);
        controller.updateDirections();

        Assertions.assertFalse(controller.getIsStartHorizontal());
        Assertions.assertFalse(controller.getIsEndHorizontal());
    }

    @Test
    public void updateDirectionNoneSnappedStraight() {
        Mockito.when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = createController();
        // Don't snap
        controller.setSnapStart(false);
        controller.setSnapEnd(false);

        // Make the line horizontal, both should be horizontal
        controller.setStartX(0);
        controller.setStartY(0);
        controller.setEndX(200);
        controller.setEndY(10);
        controller.updateDirections();

        Assertions.assertTrue(controller.getIsStartHorizontal());
        Assertions.assertTrue(controller.getIsEndHorizontal());
    }

    @Test
    public void updateDirectionNoneSnappedDiagonal() {
        Mockito.when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = createController();
        // Don't snap
        controller.setSnapStart(false);
        controller.setSnapEnd(false);

        // Make the line diagonal, since neither are snapped we just need the line to not be straight
        controller.setStartX(0);
        controller.setStartY(0);
        controller.setEndX(40);
        controller.setEndY(30);
        controller.updateDirections();

        Assertions.assertNotEquals(controller.getIsStartHorizontal(), controller.getIsEndHorizontal());
    }

    @Test
    public void updateDirectionOneSnappedStraight() {
        Mockito.when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = createController();
        // Snap only start to vertical, shouldn't change
        controller.setIsStartHorizontal(false);
        controller.setIsEndHorizontal(false);
        controller.setSnapStart(true);
        controller.setSnapEnd(false);

        // Make the line horizontal, the unsnapped end should become horizontal
        controller.setStartX(0);
        controller.setStartY(0);
        controller.setEndX(200);
        controller.setEndY(10);
        controller.updateDirections();

        Assertions.assertFalse(controller.getIsStartHorizontal());
        Assertions.assertTrue(controller.getIsEndHorizontal());
    }

    @Test
    public void updateDirectionOneSnappedDiagonal() {
        Mockito.when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = createController();
        // Snap only end to vertical, shouldn't change
        controller.setIsStartHorizontal(false);
        controller.setIsEndHorizontal(true);
        controller.setSnapStart(false);
        controller.setSnapEnd(true);

        // Make the line diagonal, unsnapped end should match snapped end
        controller.setStartX(0);
        controller.setStartY(0);
        controller.setEndX(100);
        controller.setEndY(120);
        controller.updateDirections();

        Assertions.assertTrue(controller.getIsStartHorizontal());
        Assertions.assertTrue(controller.getIsEndHorizontal());
    }
}
