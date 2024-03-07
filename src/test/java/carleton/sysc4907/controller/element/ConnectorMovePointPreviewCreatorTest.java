package carleton.sysc4907.controller.element;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.controller.element.pathing.PathingStrategy;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectorMovePointPreviewCreatorTest {

    @Mock
    private ElementIdManager elementIdManager;

    @Mock
    private ElementCreator elementCreator;

    @Mock
    private ConnectorElementController connectorElementController;

    @Mock
    private DiagramElement createdPreview;
    @Mock
    private ObservableMap<Object, Object> createdPreviewProperties;
    @Mock
    private ConnectorElementController createdPreviewController;
    @Mock
    private PathingStrategy pathingStrategy;
    @Mock
    private Pane mockEditingArea;

    @Mock
    private ObservableList<Node> mockNodesList;

    @Test
    public void testPreviewCreatedFromController() {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            ConnectorMovePointPreviewCreator connectorMovePointPreviewCreator = new ConnectorMovePointPreviewCreator(
                    elementIdManager,
                    elementCreator
            );

            when(elementCreator.create(eq("Line"), anyLong(), eq(true))).thenReturn(
                    createdPreview
            );
            when(createdPreview.getProperties()).thenReturn(createdPreviewProperties);
            when(createdPreviewProperties.get("controller")).thenReturn(createdPreviewController);
            when(elementIdManager.getNewId()).thenReturn(0L);
            // Mock getters for previous controller
            when(connectorElementController.getPathingStrategy()).thenReturn(pathingStrategy);
            when(connectorElementController.getStartX()).thenReturn(10.0);
            when(connectorElementController.getStartY()).thenReturn(20.0);
            when(connectorElementController.getEndX()).thenReturn(30.0);
            when(connectorElementController.getEndY()).thenReturn(40.0);
            when(connectorElementController.getIsStartHorizontal()).thenReturn(true);
            when(connectorElementController.getIsEndHorizontal()).thenReturn(false);
            when(connectorElementController.getSnapStart()).thenReturn(false);
            when(connectorElementController.getSnapEnd()).thenReturn(true);

            Mockito.when(mockEditingArea.getChildren())
                    .thenReturn(mockNodesList);
            connectorMovePointPreviewCreator.createMovePointPreview(connectorElementController);

            verify(createdPreview).setOpacity(0.3);
            verify(createdPreviewController).setPathingStrategy(pathingStrategy);
            verify(createdPreviewController).setStartX(10.0);
            verify(createdPreviewController).setStartY(20.0);
            verify(createdPreviewController).setEndX(30.0);
            verify(createdPreviewController).setEndY(40.0);
            verify(createdPreviewController).setIsStartHorizontal(true);
            verify(createdPreviewController).setIsEndHorizontal(false);
            verify(createdPreviewController).setSnapStart(false);
            verify(createdPreviewController).setSnapEnd(true);
            verify(mockNodesList).add(createdPreview);
        }
    }
}
