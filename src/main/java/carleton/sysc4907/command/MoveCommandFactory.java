package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;

public class MoveCommandFactory implements CommandFactory<Command<MoveCommandArgs>, MoveCommandArgs> {

    public MoveCommandFactory() {
    }

    @Override
    public Command create(MoveCommandArgs args) {
        return new MoveCommand(args);
    }
}