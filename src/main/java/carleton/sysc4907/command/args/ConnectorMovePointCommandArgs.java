package carleton.sysc4907.command.args;

import java.io.Serializable;

public record ConnectorMovePointCommandArgs(boolean isStart, double deltaX, double deltaY, long elementId)
        implements Serializable, CommandArgs {
    @Override
    public long[] getElementIds() {
        return new long[] {elementId};
    }
}
