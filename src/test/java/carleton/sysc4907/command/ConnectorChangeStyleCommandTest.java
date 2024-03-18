package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeConnectorStyleCommandArgs;
import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.controller.element.ArrowConnectorElementController;
import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.controller.element.arrows.ConnectorType;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectorChangeStyleCommandTest {
    @Mock
    private ArrowConnectorElementController mockConnectorController;
    @Mock
    private ElementIdManager mockElementIdManager;

    @ParameterizedTest
    @EnumSource(ConnectorType.class)
    void execute(ConnectorType type) {
        long testId = 12L;
        when(mockElementIdManager.getElementControllerById(testId)).thenReturn(mockConnectorController);

        var args = new ChangeConnectorStyleCommandArgs(testId, type);
        var command = new ChangeConnectorStyleCommand(args, mockElementIdManager);
        command.execute();

        verify(mockConnectorController).setPathStyle(type);
        verify(mockConnectorController).setArrowheadType(type);
    }
}
