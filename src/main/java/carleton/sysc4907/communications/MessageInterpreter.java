package carleton.sysc4907.communications;

import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.*;
import carleton.sysc4907.controller.JoinRequestDialogController;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ExecutedCommandRunner;
import carleton.sysc4907.communications.records.*;
import javafx.application.Platform;
import javafx.stage.Window;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Interprets messages received from other users
 * - give access to the executedCommandList
 * - use file loader to load commands
 */
public class MessageInterpreter {

    private final Map<Class<?>, CommandFactory> commandFactories;
    private Manager manager;
    private boolean isHost;
    private MessageConstructor messageConstructor;
    private Window window;
    private ExecutedCommandList executedCommandList;
    private ExecutedCommandRunner executedCommandRunner;

    /**
     * Constructs a MessageInterpreter
     * @param messageConstructor the MessageConstructor of the system
     */
    public MessageInterpreter(MessageConstructor messageConstructor) {
        this.commandFactories = new HashMap<>();
        this.messageConstructor = messageConstructor;
    }

    /**
     * sets the Manager and whether they are a host
     * @param manager the manager to be set
     */
    public void setManager(Manager manager) {
        this.manager = manager;
        this.isHost = manager.isHost();
    }

    public void setWindow(Window window) {
        this.window = window;
        System.out.println("Window Set");
    }

    public void setExecutedCommandList(ExecutedCommandList executedCommandList) {
        this.executedCommandList = executedCommandList;
    }

    public void setExecutedCommandRunner(ExecutedCommandRunner executedCommandRunner) {
        this.executedCommandRunner = executedCommandRunner;
    }

    /**
     * Constructs a MessageInterpreter with all the command factories
     * @param addCommandFactory the systems AddCommandFactory
     * @param removeCommandFactory the systems RemoveCommandFactory
     * @param moveCommandFactory the systems MoveCommandFactory
     * @param resizeCommandFactory the systems ResizeCommandFactory
     * @param editTextCommandFactory the systems EditTextCommandFactory
     * @param messageConstructor the MessageConstructor of the system
     */
    public MessageInterpreter(
        AddCommandFactory addCommandFactory,
        RemoveCommandFactory removeCommandFactory,
        MoveCommandFactory moveCommandFactory,
        ResizeCommandFactory resizeCommandFactory,
        EditTextCommandFactory editTextCommandFactory,
        ConnectorMovePointCommandFactory connectorMovePointCommandFactory,
        MessageConstructor messageConstructor
    ) {
        this(messageConstructor);
        addFactories(
                addCommandFactory,
                removeCommandFactory,
                moveCommandFactory,
                resizeCommandFactory,
                editTextCommandFactory,
                connectorMovePointCommandFactory);
    }

    /**
     * Adds the factories to the MessageInterpreter
     * @param addCommandFactory the systems AddCommandFactory
     * @param removeCommandFactory the systems RemoveCommandFactory
     * @param moveCommandFactory the systems MoveCommandFactory
     * @param resizeCommandFactory the systems ResizeCommandFactory
     * @param editTextCommandFactory the systems EditTextCommandFactory
     */
    public void addFactories(
            AddCommandFactory addCommandFactory,
            RemoveCommandFactory removeCommandFactory,
            MoveCommandFactory moveCommandFactory,
            ResizeCommandFactory resizeCommandFactory,
            EditTextCommandFactory editTextCommandFactory,
            ConnectorMovePointCommandFactory connectorMovePointCommandFactory) {
        commandFactories.put(AddCommandArgs.class, addCommandFactory);
        commandFactories.put(RemoveCommandArgs.class, removeCommandFactory);
        commandFactories.put(MoveCommandArgs.class, moveCommandFactory);
        commandFactories.put(ResizeCommandArgs.class, resizeCommandFactory);
        commandFactories.put(EditTextCommandArgs.class, editTextCommandFactory);
        commandFactories.put(ConnectorMovePointCommandArgs.class, connectorMovePointCommandFactory);
    }

    /**
     * Interpret the message received
     * @param message the message
     * @param userId the user id who sent the message
     */
    public void interpret(Message message, long userId) {
        System.out.println(message.type() + " - " + message.payload());
        switch (message.type()) {
            case UPDATE -> interpretUpdate(message, userId);
            case JOIN_REQUEST -> interpretJoinRequest(message, userId);
            case JOIN_RESPONSE -> interpretJoinResponse(message, userId);
            default -> System.out.println(message);
        }
    }

    /**
     * Interpret the update message received
     * @param message the message
     * @param userId the user id who sent the message
     */
    private void interpretUpdate(Message message, long userId) {
        Object args = message.payload();
        Class<?> argType = args.getClass();
        System.out.println("Looking for type " + argType);
        var factory = commandFactories.get(argType);
        if (factory == null) {
            throw new IllegalArgumentException("The given message did not correspond to a known type of command arguments.");
        }
        Command<?> command = factory.createRemote((CommandArgs) argType.cast(args));

        Platform.runLater(command::execute);
        System.out.println("Interpreted command finished executing (on platform) at time " + LocalTime.now());

        if (isHost) {
            messageConstructor.send(message);
        }
    }

    /**
     * Interpret the Join Request received
     * @param message the message
     * @param userId the user id who sent the message
     */
    private void interpretJoinRequest(Message message, long userId){
        Platform.runLater(
                () -> {
                    if (isHost) {
                        System.out.println("Join Request Received");
                        var controller = new JoinRequestDialogController(window, (String) message.payload());
                        Optional<Boolean> result = controller.showAndWait();
                        if (result.isPresent() && result.get()) {
                            manager.validateClient(userId);
                            System.out.println((String) message.payload() + " is Valid");
                            Object[] args = executedCommandList.getCommandList().stream().map(Command::getArgs).toArray();
                            messageConstructor.sendTo(new Message(MessageType.JOIN_RESPONSE, new JoinResponse(args)), userId);
                            System.out.println((String) message.payload() + " has been sent a Response");
                        }
                    }

                }
        );
    }

    /**
     * Interpret the Join Response received
     * @param message the message
     * @param userId the user id who sent the message
     */
    private void interpretJoinResponse(Message message, long userId){
        System.out.println("Join Response Received");
        if (!isHost) {
            manager.validateClient(userId);
            Object[] commandList = ((JoinResponse) message.payload()).commandList();
            executedCommandRunner.runPreviousCommands(commandList);
        }

    }
}
