package carleton.sysc4907.communications;

import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.command.args.ResizeCommandArgs;
import javafx.application.Platform;
import javafx.scene.Parent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MessageInterpreter {

    private final Map<Class<?>, CommandFactory> commandFactories;

    public MessageInterpreter(
        AddCommandFactory addCommandFactory,
        RemoveCommandFactory removeCommandFactory,
        MoveCommandFactory moveCommandFactory,
        ResizeCommandFactory resizeCommandFactory
    ) {
        this.commandFactories = new HashMap<>();
        commandFactories.put(AddCommandArgs.class, addCommandFactory);
        commandFactories.put(RemoveCommandArgs.class, removeCommandFactory);
        commandFactories.put(MoveCommandArgs.class, moveCommandFactory);
        commandFactories.put(ResizeCommandArgs.class, resizeCommandFactory);
    }

    public void interpret(Message message) {
        switch (message.type()) {
            case UPDATE -> interpretUpdate(message);
        }
    }

    private void interpretUpdate(Message message) {
        Object args = message.payload();
        Class<?> argType = args.getClass();
        var factory = commandFactories.get(argType);
        if (factory == null) {
            throw new IllegalArgumentException("The given message did not correspond to a known type of command arguments.");
        }
        Command<?> command = factory.create(argType.cast(args));

        Platform.runLater(command::execute);
    }
}
