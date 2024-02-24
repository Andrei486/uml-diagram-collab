package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.TargetedMessage;
import carleton.sysc4907.model.ExecutedCommandList;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RemoteCommandTest {
    @Mock
    private MoveCommand mockMoveCommand;
    @Mock
    private ExecutedCommandList mockExecutedCommandList;

    @Test
    void executeRemoteCommand() {
        //setup
        Command<MoveCommandArgs> remoteCommand = new RemoteCommand<>(mockMoveCommand, mockExecutedCommandList);
        Mockito.when(mockExecutedCommandList.getCommandList()).thenReturn(new LinkedList<>());

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // test
            remoteCommand.execute();

            // verify
            Mockito.verify(mockExecutedCommandList).getCommandList();
            platformMockedStatic.verify(() -> Platform.runLater(any()));
        }
    }
}
