package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.collections.ObservableMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ConnectorMovePointFactoryTest {

    @Mock
    private ElementIdManager mockElementIdManager;

    @Mock
    private ExecutedCommandList mockExecutedCommandList;

    @Test
    void createCommand() {
        long testId = 12L;
        Manager mockManager = Mockito.mock(Manager.class);
        var args = new ConnectorMovePointCommandArgs(false, 0, 20, 0, -30, testId);
        var factory = new ConnectorMovePointCommandFactory(mockElementIdManager, mockManager, mockExecutedCommandList);

        var command = factory.create(args);

        assertEquals(ConnectorMovePointCommand.class, command.getClass());
    }
}
