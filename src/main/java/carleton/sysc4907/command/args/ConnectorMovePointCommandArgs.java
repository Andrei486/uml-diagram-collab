package carleton.sysc4907.command.args;

import java.io.Serializable;

public record ConnectorMovePointCommandArgs(boolean isStart, double startX, double startY, double endX, double endY, long elementId)
        implements Serializable, CommandArgs {
    @Override
    public long[] getElementIds() {
        return new long[] {elementId};
    }
}
