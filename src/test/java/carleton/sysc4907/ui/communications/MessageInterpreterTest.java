package carleton.sysc4907.ui.communications;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.*;
import carleton.sysc4907.communications.records.JoinResponse;
import carleton.sysc4907.controller.JoinRequestDialogController;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.model.SessionModel;
import carleton.sysc4907.model.User;
import carleton.sysc4907.processing.ExecutedCommandRunner;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;
import javafx.scene.control.Dialog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import javax.swing.text.html.Option;

import java.awt.*;
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
    private ConnectorSnapCommandFactory connectorSnapCommandFactory;
    @Mock
    private ChangeConnectorStyleCommandFactory changeConnectorStyleCommandFactory;
    @Mock
    private ChangeTextStyleCommandFactory changeTextStyleCommandFactory;
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
    private ClientList clientList;
    @Mock
    private ClientData clientData;
    @Mock
    private SessionModel sessionModel;
    @Mock
    private User user;
    @Mock
    private Command<MoveCommandArgs> mockCommand;
    @Mock
    private DialogPane dialogPane;

    private MessageInterpreter constructMessageInterpreter() {
        return new MessageInterpreter(
                addCommandFactory,
                removeCommandFactory,
                moveCommandFactory,
                resizeCommandFactory,
                editTextCommandFactory,
                connectorMovePointCommandFactory,
                connectorSnapCommandFactory,
                changeConnectorStyleCommandFactory,
                messageConstructor,
                changeTextStyleCommandFactory
        );
    }

    @Test
    public void interpretUpdateMoveCommandHost() {
        // setup
        var args = new MoveCommandArgs(0, 0, 1, 1, 0L);
        Mockito.when(moveCommandFactory.createRemote(any(MoveCommandArgs.class))).thenReturn(mockCommand);
        Mockito.doNothing().when(messageConstructor).send(any(Message.class));
        Mockito.when(manager.isHost()).thenReturn(true);
        MessageInterpreter interpreter = constructMessageInterpreter();
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
        MessageInterpreter interpreter = constructMessageInterpreter();
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
        MessageInterpreter interpreter = constructMessageInterpreter();
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

        Long userId = 123L;
        String clientName = "client";
        String hostName = "host";

        //setup command list
        List<Command<?>> commands = new LinkedList<>();
        commands.add(mockCommand);

        Message message = new Message(MessageType.JOIN_REQUEST, clientName);

        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);

        when(manager.isHost()).thenReturn(true);
        when(executedCommandList.getCommandList()).thenReturn(commands);
        when(mockCommand.getArgs()).thenReturn(new MoveCommandArgs(0, 0, 1, 1, 0L));
        when(manager.getClientList()).thenReturn(clientList);
        when(clientList.getClient(eq(userId))).thenReturn(clientData);
        when(sessionModel.getLocalUser()).thenReturn(user);
        when(user.getUsername()).thenReturn(hostName);

        MessageInterpreter interpreter = constructMessageInterpreter();

        interpreter.setManager(manager);
        interpreter.setSessionModel(sessionModel);
        interpreter.setExecutedCommandList(executedCommandList);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            platformMockedStatic.when(() -> Platform.runLater(any(Runnable.class))).thenAnswer(invocationOnMock -> {
                ((Runnable) invocationOnMock.getArgument(0)).run();
                return null;
            });

            try(MockedConstruction<JoinRequestDialogController> jRDCConstruction = Mockito.mockConstruction(JoinRequestDialogController.class,
                    (mock, context) -> when(mock.showAndWait()).thenReturn(Optional.of(true)));) {

                interpreter.interpret(message, userId);

                verify(messageConstructor).sendTo(messageArgumentCaptor.capture(), eq(userId));
                verify(clientData).setUsername(eq(clientName));
                verify(manager).validateClient(userId);

                Message sentMessage = messageArgumentCaptor.getValue();
                assertEquals(MessageType.JOIN_RESPONSE, sentMessage.type());

                JoinResponse joinResponse = (JoinResponse) sentMessage.payload();

                assertEquals(hostName, joinResponse.username());
                assertEquals(mockCommand.getArgs(), joinResponse.commandList()[0]);
            }
        }
    }

    @Test
    public void interpretJoinRequestHostDenied() {

        Long userId = 123L;
        String clientName = "client";

        Message message = new Message(MessageType.JOIN_REQUEST, clientName);

        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);

        when(manager.isHost()).thenReturn(true);

        MessageInterpreter interpreter = constructMessageInterpreter();

        interpreter.setManager(manager);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            platformMockedStatic.when(() -> Platform.runLater(any(Runnable.class))).thenAnswer(invocationOnMock -> {
                ((Runnable) invocationOnMock.getArgument(0)).run();
                return null;
            });

            try(MockedConstruction<JoinRequestDialogController> jRDCConstruction = Mockito.mockConstruction(JoinRequestDialogController.class,
                    (mock, context) -> when(mock.showAndWait()).thenReturn(Optional.of(false)));) {

                interpreter.interpret(message, userId);

                verify(messageConstructor).sendToInvalidAndClose(messageArgumentCaptor.capture(), eq(userId));

                Message sentMessage = messageArgumentCaptor.getValue();
                assertEquals(MessageType.JOIN_DENIED, sentMessage.type());
                assertEquals(null, sentMessage.payload());
            }
        }
    }

    @Test
    public void interpretJoinResponseClient() {

        Long userId = 123L;
        String hostName = "host";

        MoveCommandArgs args = new MoveCommandArgs(0, 0, 1, 1, 0L);

        ArgumentCaptor<Object[]> argumentCaptor = ArgumentCaptor.forClass(Object[].class);

        Message message = new Message(MessageType.JOIN_RESPONSE, new JoinResponse(hostName, new Object[]{args}));

        when(manager.isHost()).thenReturn(false);
        when(manager.getClientList()).thenReturn(clientList);
        when(clientList.getClient(eq(userId))).thenReturn(clientData);

        MessageInterpreter interpreter = constructMessageInterpreter();

        interpreter.setManager(manager);
        interpreter.setExecutedCommandRunner(executedCommandRunner);

        interpreter.interpret(message, userId);

        verify(manager).validateClient(userId);
        verify(clientData).setUsername(eq(hostName));
        verify(executedCommandRunner).runPreviousCommands(argumentCaptor.capture());

        assertEquals(args, argumentCaptor.getValue()[0]);
    }

    @Test
    public void interpretJoinDeniedClient() {

        Long userId = 123L;

        Message message = new Message(MessageType.JOIN_DENIED, null);

        when(manager.isHost()).thenReturn(false);
        when(manager.getClientList()).thenReturn(clientList);


        MessageInterpreter interpreter = constructMessageInterpreter();

        interpreter.setManager(manager);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            platformMockedStatic.when(() -> Platform.runLater(any(Runnable.class))).thenAnswer(invocationOnMock -> {
                ((Runnable) invocationOnMock.getArgument(0)).run();
                return null;
            });

            try(MockedConstruction<Dialog> dialogMockedConstruction = Mockito.mockConstruction(Dialog.class,
                    (mock, context) -> {
                        when(mock.getDialogPane()).thenReturn(dialogPane);
                        when(dialogPane.getButtonTypes()).thenReturn(FXCollections.observableArrayList());
                    })) {

                interpreter.interpret(message, userId);

                verify(clientList).removeClient(userId);

                Dialog dialog = dialogMockedConstruction.constructed().get(0);

                verify(dialog).setTitle("Denied");
                verify(dialog).show();
            }
        }

    }

    @Test
    public void interpretClose() {

        Long userId = 123L;
        String closedUser = "Test";

        Message message = new Message(MessageType.CLOSE, null);

        when(manager.getClientList()).thenReturn(clientList);
        when(clientList.getClient(userId)).thenReturn(clientData);
        when(clientData.getUsername()).thenReturn(closedUser);

        MessageInterpreter interpreter = constructMessageInterpreter();

        interpreter.setManager(manager);

        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            platformMockedStatic.when(() -> Platform.runLater(any(Runnable.class))).thenAnswer(invocationOnMock -> {
                ((Runnable) invocationOnMock.getArgument(0)).run();
                return null;
            });

            try(MockedConstruction<Dialog> dialogMockedConstruction = Mockito.mockConstruction(Dialog.class,
                    (mock, context) -> {
                        when(mock.getDialogPane()).thenReturn(dialogPane);
                        when(dialogPane.getButtonTypes()).thenReturn(FXCollections.observableArrayList());
                    })) {

                interpreter.interpret(message, userId);

                verify(clientList).removeClient(userId);

                Dialog dialog = dialogMockedConstruction.constructed().get(0);

                verify(dialog).setTitle("Connection Lost");
                verify(dialog).show();
            }
        }
    }

}
