package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ConnectorSnapCommandArgs;
import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.SnapHandle;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectorSnapCommandTest {

    @Mock
    private Node mockConnector;
    @Mock
    private SnapHandle mockSnapHandle;
    @Mock
    private ConnectorElementController mockConnectorController;
    @Mock
    private ElementIdManager mockElementIdManager;
    @Mock
    private ObservableMap<Object, Object> mockPropertiesMap;

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void testUnsnap(int isStartInt) {
        boolean isStart = isStartInt == 1; // our version of JUnit doesn't support booleans in params yet
        var controllerId = 12L;
        var snapHandleId = 1L;

        when(mockElementIdManager.getElementById(snapHandleId)).thenReturn(mockSnapHandle);
        when(mockElementIdManager.getElementById(controllerId)).thenReturn(mockConnector);
        when(mockElementIdManager.getElementControllerById(controllerId)).thenReturn(mockConnectorController);
        doNothing().when(mockConnectorController).unsnapFromHandle(isStart);

        var args = new ConnectorSnapCommandArgs(controllerId, isStart, true, snapHandleId);
        var command = new ConnectorSnapCommand(args, mockElementIdManager);
        command.execute();

        verify(mockConnectorController).unsnapFromHandle(isStart);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void testSnap(int isStartInt) {
        boolean isStart = isStartInt == 1; // our version of JUnit doesn't support booleans in params yet
        var controllerId = 12L;
        var snapHandleId = 1L;

        when(mockElementIdManager.getElementById(snapHandleId)).thenReturn(mockSnapHandle);
        when(mockElementIdManager.getElementById(controllerId)).thenReturn(mockConnector);
        when(mockElementIdManager.getElementControllerById(controllerId)).thenReturn(mockConnectorController);
        doNothing().when(mockConnectorController).snapToHandle(mockSnapHandle, isStart);

        var args = new ConnectorSnapCommandArgs(controllerId, isStart, false, snapHandleId);
        var command = new ConnectorSnapCommand(args, mockElementIdManager);
        command.execute();

        verify(mockConnectorController).snapToHandle(mockSnapHandle, isStart);
    }
}
