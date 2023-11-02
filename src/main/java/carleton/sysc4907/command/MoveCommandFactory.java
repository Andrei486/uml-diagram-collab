package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;

public class MoveCommandFactory implements CommandFactory<Command<MoveCommandArgs>, MoveCommandArgs> {

    @Override
    public Command create(MoveCommandArgs args) {
        return new MoveCommand(args);
    }
}
