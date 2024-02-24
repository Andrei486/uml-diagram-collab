package carleton.sysc4907.communications;

import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.*;
import javafx.application.Platform;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Interprets messages received from other users
 */
public class MessageInterpreter {

    private final Map<Class<?>, CommandFactory> commandFactories;
    private Manager manager;
    private boolean isHost;
    private MessageConstructor messageConstructor;

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
        Command<?> command = factory.createRemote(argType.cast(args));

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
        if (isHost) {
            manager.validateClient(userId);
            messageConstructor.sendToInvalid(new Message(MessageType.JOIN_RESPONSE, null), userId);
        }
        System.out.println("Join Request Received");
    }

    /**
     * Interpret the Join Response received
     * @param message the message
     * @param userId the user id who sent the message
     */
    private void interpretJoinResponse(Message message, long userId){
        manager.validateClient(userId);
        System.out.println("Join Response Received");
    }
}
