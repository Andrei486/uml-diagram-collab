package carleton.sysc4907.command.args;

import java.io.Serializable;

public record ConnectorSnapCommandArgs(
        long connectorElementId,
        boolean isStart,
        boolean isUnsnap,
        long snapHandleId) implements Serializable, CommandArgs {

    @Override
    public long[] getElementIds() {
        return new long[] {connectorElementId};
    }
}
