package carleton.sysc4907.command.args;

import java.io.Serializable;

public record EditTextCommandArgs(String text, long elementId) implements Serializable, CommandArgs {
    @Override
    public long[] getElementIds() {
        return new long[] {elementId};
    }
}
