package carleton.sysc4907.command.args;

import java.io.Serializable;

public record ResizeCommandArgs(boolean isTopAnchor,
                                boolean isRightAnchor,
                                double dragStartX,
                                double dragStartY,
                                double dragEndX,
                                double dragEndY,
                                double elementX,
                                double elementY,
                                double width,
                                double height,
                                long elementId) implements Serializable, CommandArgs {

    @Override
    public long[] getElementIds() {
        return new long[] {elementId};
    }
}