package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.ConnectorMovePointCommandFactory;
import carleton.sysc4907.command.ConnectorSnapCommandFactory;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.controller.element.pathing.PathingStrategy;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.view.SnapHandle;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectorElementControllerTest {

    @Mock
    private MovePreviewCreator movePreviewCreator;
    @Mock
    private MoveCommandFactory moveCommandFactory;
    @Mock
    private DiagramModel diagramModel;
    @Mock
    private ConnectorHandleCreator connectorHandleCreator;

    @Mock
    private ConnectorMovePointPreviewCreator connectorMovePointPreviewCreator;
    @Mock
    private ConnectorMovePointCommandFactory connectorMovePointCommandFactory;
    @Mock
    private ConnectorSnapCommandFactory connectorSnapCommandFactory;
    @Mock
    private PathingStrategy pathingStrategy;
    @Mock
    private ObservableList<DiagramElement> mockSelectedElementsList;
    @Mock
    private SnapHandle snapHandle;
    @Mock
    private Set<Long> snapHandleIds;
    @Mock
    private DiagramElement element;

    @Test
    public void updateDirectionBothSnapped() {
        when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                pathingStrategy
        );
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
        when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                pathingStrategy
        );
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
        when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                pathingStrategy
        );
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
        when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                pathingStrategy
        );
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
        when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        var controller = new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                pathingStrategy
        );
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

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void snapToHandle(int isStartInt) throws NoSuchFieldException, IllegalAccessException {
        when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        boolean isStart = isStartInt == 1;
        long testId = 12L;

        var xProperty = new SimpleDoubleProperty();
        xProperty.set(100);
        var yProperty = new SimpleDoubleProperty();
        yProperty.set(200);
        when(snapHandle.getCenterXBinding()).thenReturn(xProperty.negate());
        when(snapHandle.getCenterYBinding()).thenReturn(yProperty.negate());
        when(snapHandle.isHorizontal()).thenReturn(false);
        when(snapHandle.getSnappedConnectorIds()).thenReturn(snapHandleIds);
        when(snapHandleIds.add(anyLong())).thenReturn(true);
        when(element.getElementId()).thenReturn(testId);
        var controller = new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                pathingStrategy
        );
        Field field = DiagramElementController.class.getDeclaredField("element");
        field.setAccessible(true);
        field.set(controller, element);
        controller.setIsStartHorizontal(true);
        controller.setIsEndHorizontal(true);

        controller.snapToHandle(snapHandle, isStart);

        if (isStart) {
            assertEquals(-100, controller.getStartX());
            assertEquals(-200, controller.getStartY());
            assertFalse(controller.getIsStartHorizontal());
        } else {
            assertEquals(-100, controller.getEndX());
            assertEquals(-200, controller.getEndY());
            assertFalse(controller.getIsEndHorizontal());
        }
        verify(snapHandleIds).add(testId);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void unsnapFromHandle(int isStartInt) throws NoSuchFieldException, IllegalAccessException {
        when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(Mockito.any(ListChangeListener.class));
        boolean isStart = isStartInt == 1;
        long testId = 12L;

        var xProperty = new SimpleDoubleProperty();
        xProperty.set(100);
        var yProperty = new SimpleDoubleProperty();
        yProperty.set(200);
        when(snapHandle.getCenterXBinding()).thenReturn(xProperty.negate());
        when(snapHandle.getCenterYBinding()).thenReturn(yProperty.negate());
        when(snapHandle.isHorizontal()).thenReturn(false);
        when(snapHandle.getSnappedConnectorIds()).thenReturn(snapHandleIds);
        when(snapHandleIds.add(anyLong())).thenReturn(true);
        when(element.getElementId()).thenReturn(testId);
        var controller = new ConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                pathingStrategy
        );
        Field field = DiagramElementController.class.getDeclaredField("element");
        field.setAccessible(true);
        field.set(controller, element);
        controller.setIsStartHorizontal(true);
        controller.setIsEndHorizontal(true);

        controller.snapToHandle(snapHandle, isStart);
        controller.unsnapFromHandle(isStart);

        if (isStart) {
            controller.setStartX(2); // will throw if not unbound
            controller.setStartY(2); // will throw if not unbound
        } else {
            controller.setEndX(2); // will throw if not unbound
            controller.setEndY(2); // will throw if not unbound
        }

        verify(snapHandleIds).remove(testId);
    }
}
