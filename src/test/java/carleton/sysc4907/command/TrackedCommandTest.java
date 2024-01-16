package carleton.sysc4907.command;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.TargetedMessage;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TrackedCommandTest {
    @Mock
    private Manager mockManager;
    @Mock
    private MoveCommand mockMoveCommand;

    @Test
    void executeTrackedCommand() {
        //setup
        Command<MoveCommandArgs> trackedCommand = new TrackedCommand<>(mockMoveCommand, mockManager);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // test
            trackedCommand.execute();

            // verify
            Mockito.verify(mockManager).send(any(TargetedMessage.class));
            platformMockedStatic.verify(() -> Platform.runLater(any()));
        }
    }


}
