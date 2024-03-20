package carleton.sysc4907.communications;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.controller.JoinRequestDialogController;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ExecutedCommandRunner;
import javafx.application.Platform;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import javax.swing.text.html.Option;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
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
    private MessageConstructor messageConstructor;
    @Mock
    private Manager manager;
    @Mock
    private Window window;
    @Mock
    private ExecutedCommandList executedCommandList;
    @Mock
    private ExecutedCommandRunner executedCommandRunner;
    @Mock
    private JoinRequestDialogController joinRequestDialogController;

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

    @Test
    public void interpretJoinRequestHost() {

            //setup command list
            List<Command<?>> commands = new LinkedList<>();
            commands.add(mockCommand);

            Message message = new Message(MessageType.JOIN_REQUEST, "test1");

            ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);

            Mockito.when(manager.isHost()).thenReturn(true);
            Mockito.when(executedCommandList.getCommandList()).thenReturn(commands);

            MessageInterpreter interpreter = new MessageInterpreter(
                    addCommandFactory,
                    removeCommandFactory,
                    moveCommandFactory,
                    resizeCommandFactory,
                    editTextCommandFactory,
                    connectorMovePointCommandFactory,
                    messageConstructor
            );

            interpreter.setManager(manager);
            interpreter.setExecutedCommandList(executedCommandList);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            platformMockedStatic.when(() -> Platform.runLater(any(Runnable.class))).thenAnswer(invocationOnMock -> {
                ((Runnable) invocationOnMock.getArgument(0)).run();
                return null;
            });

            try(MockedConstruction<JoinRequestDialogController> jRDCConstruction = Mockito.mockConstruction(JoinRequestDialogController.class,
                    (mock, context) -> when(mock.showAndWait()).thenReturn(Optional.of(true)));) {

                interpreter.interpret(message, 123);


                TimeUnit.SECONDS.sleep(5);


                verify(messageConstructor).sendTo(messageArgumentCaptor.capture(), eq(123));

                Message sentMessage = messageArgumentCaptor.getValue();
                assertEquals(MessageType.JOIN_RESPONSE, sentMessage.type());
                assertEquals(new Object(), message.payload());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
