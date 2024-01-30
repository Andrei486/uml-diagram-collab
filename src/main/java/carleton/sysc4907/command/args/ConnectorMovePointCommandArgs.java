package carleton.sysc4907.command.args;

import java.io.Serializable;

public record ConnectorMovePointCommandArgs(boolean isStart, double deltaX, double deltaY, long elementId) implements Serializable {
}
