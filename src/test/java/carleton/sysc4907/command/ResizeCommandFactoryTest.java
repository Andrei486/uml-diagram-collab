package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.command.args.ResizeCommandArgs;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResizeCommandFactoryTest {

    @Test
    void createCommand() {
        Pane mockNode = Mockito.mock(Pane.class);
        ResizeCommandArgs args = new ResizeCommandArgs(true, true,
                10, 0, 40, -30, mockNode);
        ResizeCommandFactory factory = new ResizeCommandFactory();

        var command = factory.create(args);

        assertEquals(ResizeCommand.class, command.getClass());
    }
}
