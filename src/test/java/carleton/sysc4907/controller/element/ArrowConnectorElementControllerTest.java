package carleton.sysc4907.controller.element;

import carleton.sysc4907.controller.element.arrows.Arrowhead;
import carleton.sysc4907.controller.element.arrows.ArrowheadFactory;
import carleton.sysc4907.controller.element.arrows.ConnectorType;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ArrowConnectorElementControllerTest extends ConnectorElementControllerTest {

    @Mock
    protected ArrowheadFactory arrowheadFactory;

    @Mock
    protected Arrowhead arrowhead;

    @Mock
    protected DiagramElement element;

    @Mock
    protected Path connectorPath;

    @Mock
    protected ObservableList<Double> mockStrokeDashArray;
    @Mock
    protected Path arrowheadPath;

    @Mock
    protected ObservableList<PathElement> mockPathElementsList;

    @Override
    protected ConnectorElementController createController() {
        var connector = new ArrowConnectorElementController(
                movePreviewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointPreviewCreator,
                connectorMovePointCommandFactory,
                pathingStrategy,
                arrowheadFactory
        );
        try {
            Field elementField = DiagramElementController.class.getDeclaredField("element");
            elementField.setAccessible(true);
            elementField.set(connector, element);
            Field arrowheadPathField = connector.getClass().getDeclaredField("arrowheadPath");
            arrowheadPathField.setAccessible(true);
            arrowheadPathField.set(connector, arrowheadPath);
            Field connectorPathField = connector.getClass().getSuperclass().getDeclaredField("connectorPath");
            connectorPathField.setAccessible(true);
            connectorPathField.set(connector, connectorPath);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return connector;
    }

    @ParameterizedTest
    @EnumSource(ConnectorType.class)
    void testSetArrowheadType(ConnectorType type) {
        Mockito.when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(any(ListChangeListener.class));
        Mockito.lenient().when(element.getLayoutX()).thenReturn(0.0);
        Mockito.lenient().when(element.getLayoutY()).thenReturn(0.0);
        Mockito.lenient().when(arrowheadPath.getElements()).thenReturn(mockPathElementsList);
        if (type != ConnectorType.NONE) {
            Mockito.when(arrowheadFactory.createArrowhead(type)).thenReturn(arrowhead);
        } else {
            Mockito.when(arrowheadFactory.createArrowhead(type)).thenReturn(null);
        }
        var controller = (ArrowConnectorElementController) createController();
        controller.setArrowheadType(type);

        if (type != ConnectorType.NONE) {
            Mockito.verify(arrowhead).makeArrowheadPath(any(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyBoolean(), anyBoolean());
        } else {
            Mockito.verify(mockPathElementsList).clear();
            Mockito.verify(arrowhead, Mockito.never()).makeArrowheadPath(any(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyBoolean(), anyBoolean());
        }
    }

    @ParameterizedTest
    @EnumSource(ConnectorType.class)
    void testSetPathStyle(ConnectorType type) {
        Mockito.when(diagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.doNothing().when(mockSelectedElementsList).addListener(any(ListChangeListener.class));
        Mockito.when(connectorPath.getStrokeDashArray()).thenReturn(mockStrokeDashArray);
        var controller = (ArrowConnectorElementController) createController();
        controller.setPathStyle(type);

        if (type == ConnectorType.IMPLEMENTATION) {
            Mockito.verify(mockStrokeDashArray).add(anyDouble());
        } else {
            Mockito.verify(mockStrokeDashArray).clear();
        }
    }
}
