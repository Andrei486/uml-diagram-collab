package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MoveCommandFactoryTest {

    @Mock
    private Node mockNode;
    @Mock
    private ElementIdManager mockElementIdManager;
    @Mock
    private ExecutedCommandList mockExecutedCommandList;
    @Mock
    private MessageConstructor messageConstructor;

    @Test
    void createCommand() {
        long testId = 12L;
        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, testId);
        Manager mockManager = Mockito.mock(Manager.class);
        MoveCommandFactory factory = new MoveCommandFactory(mockElementIdManager, mockManager, mockExecutedCommandList, messageConstructor);

        var command = factory.create(args);

        assertEquals(MoveCommand.class, command.getClass());
    }

    @Test
    void createTracked() {
        long testId = 12L;
        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, testId);
        Manager mockManager = Mockito.mock(Manager.class);
        MoveCommandFactory factory = new MoveCommandFactory(mockElementIdManager, mockManager, mockExecutedCommandList, messageConstructor);

        var command = factory.createTracked(args);

        assertEquals(TrackedCommand.class, command.getClass());
    }
}
