package carleton.sysc4907.command;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.Message;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.communications.TargetedMessage;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TrackedCommandTest {
    @Mock
    private Manager mockManager;
    @Mock
    private MoveCommand mockMoveCommand;
    @Mock
    private ExecutedCommandList mockExecutedCommandList;
    @Mock
    private MessageConstructor mockMessageConstructor;

    @Test
    void executeTrackedCommand() {
        //setup
        Command<MoveCommandArgs> trackedCommand = new TrackedCommand<>(mockMoveCommand, mockManager, mockExecutedCommandList, mockMessageConstructor);
        Mockito.when(mockManager.isHost()).thenReturn(true);
        Mockito.when(mockExecutedCommandList.getCommandList()).thenReturn(new LinkedList<>());

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // test
            trackedCommand.execute();

            // verify
            Mockito.verify(mockMessageConstructor).send(any(Message.class));
            Mockito.verify(mockExecutedCommandList).getCommandList();
            platformMockedStatic.verify(() -> Platform.runLater(any()));
        }
    }

    @Test
    void executeTrackedCommandClient() {
        //setup
        Command<MoveCommandArgs> trackedCommand = new TrackedCommand<>(mockMoveCommand, mockManager, mockExecutedCommandList, mockMessageConstructor);
        Mockito.when(mockManager.isHost()).thenReturn(false);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // test
            trackedCommand.execute();

            // verify
            Mockito.verify(mockMessageConstructor).send(any(Message.class));
            platformMockedStatic.verify(() -> Platform.runLater(any()), Mockito.never());
        }
    }


}
