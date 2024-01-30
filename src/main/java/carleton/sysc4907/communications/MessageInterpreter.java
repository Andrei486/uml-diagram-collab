package carleton.sysc4907.communications;

import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.*;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;

public class MessageInterpreter {

    private final Map<Class<?>, CommandFactory> commandFactories;
    private Manager manager;
    private boolean isHost;
    private MessageConstructor messageConstructor;

    public MessageInterpreter(MessageConstructor messageConstructor) {
        this.commandFactories = new HashMap<>();
        this.messageConstructor = messageConstructor;
    }

    public void setManager(Manager manager, boolean isHost) {
        this.manager = manager;
        this.isHost = isHost;
    }

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

    public void interpret(Message message, long userId) {
        System.out.println(message.type() + " - " + message.payload());
        switch (message.type()) {
            case UPDATE -> interpretUpdate(message, userId);
            default -> System.out.println(message);
        }
    }

    private void interpretUpdate(Message message, long userId) {
        Object args = message.payload();
        Class<?> argType = args.getClass();
        System.out.println("Looking for type " + argType);
        var factory = commandFactories.get(argType);
        if (factory == null) {
            throw new IllegalArgumentException("The given message did not correspond to a known type of command arguments.");
        }
        Command<?> command = factory.create(argType.cast(args));

        Platform.runLater(command::execute);

        if (isHost) {
            messageConstructor.sendAllBut(message, userId);
        }
    }
}
