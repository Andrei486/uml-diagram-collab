package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.command.args.ResizeCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ResizeCommandFactoryTest {

    @Mock
    private Node mockNode;
    @Mock
    private ElementIdManager mockElementIdManager;

    @Test
    void createCommand() {
        long testId = 12L;
        Pane mockNode = Mockito.mock(Pane.class);
        ResizeCommandArgs args = new ResizeCommandArgs(true, true,
                10, 0, 40, -30, testId);
        Manager mockManager = Mockito.mock(Manager.class);
        ResizeCommandFactory factory = new ResizeCommandFactory(mockElementIdManager, mockManager);

        var command = factory.create(args);

        assertEquals(ResizeCommand.class, command.getClass());
    }
}
