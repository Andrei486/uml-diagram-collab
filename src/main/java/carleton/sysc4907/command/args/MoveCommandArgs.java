package carleton.sysc4907.command.args;

import javafx.scene.Node;

import java.io.Serializable;

/**
 * Arguments for the move command
 * @param startX the starting X coordinate of the element
 * @param startY the starting Y coordinate of the element
 * @param endX the ending X coordinate of the element
 * @param endY the ending Y coordinate of the element
 * @param elementId the ID of the element to be moved
 */
public record MoveCommandArgs(double startX, double startY, double endX, double endY, long elementId)
        implements Serializable, CommandArgs {

    @Override
    public long[] getElementIds() {
        return new long[] {elementId};
    }
}