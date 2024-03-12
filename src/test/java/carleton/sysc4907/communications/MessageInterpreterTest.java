package carleton.sysc4907.communications;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.MoveCommandArgs;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MessageInterpreterTest {

    @Mock
    private AddCommandFactory addCommandFactory;
    @Mock
    private RemoveCommandFactory removeCommandFactory;
    @Mock
    private MoveCommandFactory moveCommandFactory;
    @Mock
    private ResizeCommandFactory resizeCommandFactory;
    @Mock
    private EditTextCommandFactory editTextCommandFactory;
    @Mock
    private ConnectorMovePointCommandFactory connectorMovePointCommandFactory;
    @Mock
    private ConnectorSnapCommandFactory connectorSnapCommandFactory;
    @Mock
    private MessageConstructor messageConstructor;
    @Mock
    private Manager manager;

    @Mock
    private Command<MoveCommandArgs> mockCommand;

    @Test
    public void interpretUpdateMoveCommandHost() {
        // setup
        var args = new MoveCommandArgs(0, 0, 1, 1, 0L);
        Mockito.when(moveCommandFactory.createRemote(any(MoveCommandArgs.class))).thenReturn(mockCommand);
        Mockito.doNothing().when(messageConstructor).send(any(Message.class));
        Mockito.when(manager.isHost()).thenReturn(true);
        MessageInterpreter interpreter = new MessageInterpreter(
                addCommandFactory,
                removeCommandFactory,
                moveCommandFactory,
                resizeCommandFactory,
                editTextCommandFactory,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                messageConstructor
        );
        interpreter.setManager(manager);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // runLater will do nothing by default
            // test
            interpreter.interpret(new Message(MessageType.UPDATE, args), 0);

            // verify
            Mockito.verify(moveCommandFactory).createRemote(any(MoveCommandArgs.class));
            Mockito.verify(messageConstructor).send(any(Message.class));
            platformMockedStatic.verify(() -> Platform.runLater(any()));
        }
    }

    @Test
    public void interpretUpdateMoveCommandClient() {
        // setup
        var args = new MoveCommandArgs(0, 0, 1, 1, 0L);
        Mockito.when(moveCommandFactory.createRemote(any(MoveCommandArgs.class))).thenReturn(mockCommand);
        Mockito.when(manager.isHost()).thenReturn(false);
        MessageInterpreter interpreter = new MessageInterpreter(
                addCommandFactory,
                removeCommandFactory,
                moveCommandFactory,
                resizeCommandFactory,
                editTextCommandFactory,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                messageConstructor
        );
        interpreter.setManager(manager);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // runLater will do nothing by default
            // test
            interpreter.interpret(new Message(MessageType.UPDATE, args), 0);

            // verify
            Mockito.verify(moveCommandFactory).createRemote(any(MoveCommandArgs.class));
            Mockito.verify(messageConstructor, Mockito.never()).send(any(Message.class));
            platformMockedStatic.verify(() -> Platform.runLater(any()));
        }
    }

    @Test
    public void interpretUpdateIncorrectPayload() {
        // setup
        MessageInterpreter interpreter = new MessageInterpreter(
                addCommandFactory,
                removeCommandFactory,
                moveCommandFactory,
                resizeCommandFactory,
                editTextCommandFactory,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                messageConstructor
        );
        var payload = "Not an args object";
        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // runLater will do nothing by default
            // test
            assertThrows(
                    IllegalArgumentException.class,
                    () -> interpreter.interpret(new Message(MessageType.UPDATE, payload), 0));

            // verify
            platformMockedStatic.verify(() -> Platform.runLater(any()), Mockito.never());
        }
    }
}
