package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

public class MoveCommandFactoryTest {

    @Test
    void createCommand() {
        Node mockNode = Mockito.mock(Node.class);
        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, mockNode);
        MoveCommandFactory factory = new MoveCommandFactory();

        var command = factory.create(args);

        assertEquals(MoveCommand.class, command.getClass());
    }
}
