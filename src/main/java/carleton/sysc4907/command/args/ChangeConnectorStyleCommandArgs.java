package carleton.sysc4907.command.args;

import carleton.sysc4907.controller.element.arrows.ConnectorType;

import java.io.Serializable;

public record ChangeConnectorStyleCommandArgs(long elementId, ConnectorType type) implements Serializable, CommandArgs {
    @Override
    public long[] getElementIds() {
        return new long[] {elementId};
    }
}
