package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ResizeCommandArgs;

public class ResizeCommandFactory implements CommandFactory<Command<ResizeCommandArgs>, ResizeCommandArgs> {

    @Override
    public Command create(ResizeCommandArgs args) {
        return new ResizeCommand(args);
    }
}