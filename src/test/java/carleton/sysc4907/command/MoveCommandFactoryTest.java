package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MoveCommandFactoryTest {

    @Mock
    private Node mockNode;
    @Mock
    private ElementIdManager mockElementIdManager;

    @Test
    void createCommand() {
        long testId = 12L;
        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, testId);
        MoveCommandFactory factory = new MoveCommandFactory(mockElementIdManager);

        var command = factory.create(args);

        assertEquals(MoveCommand.class, command.getClass());
    }
}
