package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoveCommandTest {

    @Mock
    private Node mockNode;
    @Mock
    private ElementIdManager mockElementIdManager;

    @Test
    void executeMoves() {
        long testId = 12L;
        doNothing().when(mockNode).setLayoutX(30);
        doNothing().when(mockNode).setLayoutY(-30);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);

        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, testId);
        MoveCommand command = new MoveCommand(args, mockElementIdManager);

        command.execute();

        verify(mockNode).setLayoutX(30);
        verify(mockNode).setLayoutY(-30);
    }

    @Test
    void executeMoveIdempotent() {
        long testId = 12L;
        doNothing().when(mockNode).setLayoutX(30);
        doNothing().when(mockNode).setLayoutY(-30);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);

        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, testId);
        MoveCommand command = new MoveCommand(args, mockElementIdManager);

        command.execute();
        command.execute();

        verify(mockNode, times(2)).setLayoutX(30);
        verify(mockNode, times(2)).setLayoutY(-30);
    }

    @Test
    void executeMoveAbsolute() {
        long testId = 12L;
        doNothing().when(mockNode).setLayoutX(30);
        doNothing().when(mockNode).setLayoutY(-30);
        doNothing().when(mockNode).setLayoutX(60);
        doNothing().when(mockNode).setLayoutY(-60);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);

        MoveCommandArgs args = new MoveCommandArgs(10, 0, 40, -30, testId);
        MoveCommandArgs args2 = new MoveCommandArgs(10, 0, 70, -60, testId);
        MoveCommand command = new MoveCommand(args, mockElementIdManager);
        MoveCommand command2 = new MoveCommand(args2, mockElementIdManager);

        command2.execute();

        verify(mockNode).setLayoutX(60);
        verify(mockNode).setLayoutY(-60);

        command.execute();

        verify(mockNode).setLayoutX(30);
        verify(mockNode).setLayoutY(-30);
    }
}
