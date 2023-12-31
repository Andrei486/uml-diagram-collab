package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class MoveCommandTest {

    @Test
    void executeMoves() {
        Node mockNode = Mockito.mock(Node.class);
        doNothing().when(mockNode).setLayoutX(30);
        doNothing().when(mockNode).setLayoutY(-30);
        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, mockNode);
        MoveCommand command = new MoveCommand(args);

        command.execute();

        verify(mockNode).setLayoutX(30);
        verify(mockNode).setLayoutY(-30);
    }
}
