package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConnectorMovePointCommandTest {
    @Mock
    private Node mockNode;
    @Mock
    private ConnectorElementController mockConnectorController;
    @Mock
    private ElementIdManager mockElementIdManager;
    @Mock
    private ObservableMap<Object, Object> mockPropertiesMap;

    @Test
    void executeMoveStartPoint() {
        long testId = 12L;
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        when(mockNode.getProperties()).thenReturn(mockPropertiesMap);
        when(mockPropertiesMap.get("controller")).thenReturn(mockConnectorController);
        when(mockConnectorController.getStartX()).thenReturn(100.0);
        when(mockConnectorController.getStartY()).thenReturn(0.0);
        doNothing().when(mockConnectorController).setStartX(anyDouble());
        doNothing().when(mockConnectorController).setStartY(anyDouble());

        var args = new ConnectorMovePointCommandArgs(true, -55.0, 30.0, testId);
        var command = new ConnectorMovePointCommand(args, mockElementIdManager);
        command.execute();

        verify(mockConnectorController).getStartX();
        verify(mockConnectorController).getStartY();
        verify(mockConnectorController).setStartX(45.0);
        verify(mockConnectorController).setStartY(30.0);
        verify(mockConnectorController, never()).setEndX(anyDouble());
        verify(mockConnectorController, never()).setEndY(anyDouble());
    }

    @Test
    void executeMoveEndPoint() {
        long testId = 12L;
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        when(mockNode.getProperties()).thenReturn(mockPropertiesMap);
        when(mockPropertiesMap.get("controller")).thenReturn(mockConnectorController);
        when(mockConnectorController.getEndX()).thenReturn(100.0);
        when(mockConnectorController.getEndY()).thenReturn(200.0);
        doNothing().when(mockConnectorController).setEndX(anyDouble());
        doNothing().when(mockConnectorController).setEndY(anyDouble());

        var args = new ConnectorMovePointCommandArgs(false, 20, 10, testId);
        var command = new ConnectorMovePointCommand(args, mockElementIdManager);
        command.execute();

        verify(mockConnectorController).getEndX();
        verify(mockConnectorController).getEndY();
        verify(mockConnectorController).setEndX(120.0);
        verify(mockConnectorController).setEndY(210.0);
        verify(mockConnectorController, never()).setStartX(anyDouble());
        verify(mockConnectorController, never()).setStartY(anyDouble());
    }
}
