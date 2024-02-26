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
        doNothing().when(mockConnectorController).setStartX(anyDouble());
        doNothing().when(mockConnectorController).setStartY(anyDouble());

        var args = new ConnectorMovePointCommandArgs(true, 110, -10, 155, 20, testId);
        var command = new ConnectorMovePointCommand(args, mockElementIdManager);
        command.execute();

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
        doNothing().when(mockConnectorController).setEndX(anyDouble());
        doNothing().when(mockConnectorController).setEndY(anyDouble());

        var args = new ConnectorMovePointCommandArgs(false, 120, 0, 240,210, testId);
        var command = new ConnectorMovePointCommand(args, mockElementIdManager);
        command.execute();

        verify(mockConnectorController).setEndX(120.0);
        verify(mockConnectorController).setEndY(210.0);
        verify(mockConnectorController, never()).setStartX(anyDouble());
        verify(mockConnectorController, never()).setStartY(anyDouble());
    }
}
