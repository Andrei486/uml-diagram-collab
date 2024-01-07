package carleton.sysc4907.command.args;

import java.io.Serializable;

/**
 * Arguments for the remove command
 * @param elementIds the IDs for the elements to remove
 */
public record RemoveCommandArgs(long[] elementIds) implements Serializable {
}
