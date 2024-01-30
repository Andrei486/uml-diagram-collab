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
    private Command<MoveCommandArgs> mockCommand;

    @Test
    public void interpretUpdateMoveCommand() {
        // setup
        MessageInterpreter interpreter = new MessageInterpreter(
                addCommandFactory,
                removeCommandFactory,
                moveCommandFactory,
                resizeCommandFactory,
                editTextCommandFactory,
                connectorMovePointCommandFactory,
                new MessageConstructor()
        );



        var args = new MoveCommandArgs(0, 0, 1, 1, 0L);
        Mockito.when(moveCommandFactory.create(any(MoveCommandArgs.class))).thenReturn(mockCommand);
        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            // runLater will do nothing by default
            // test
            interpreter.interpret(new Message(MessageType.UPDATE, args), 0);

            // verify
            Mockito.verify(moveCommandFactory).create(any(MoveCommandArgs.class));
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
                new MessageConstructor()
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
