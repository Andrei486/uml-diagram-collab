package carleton.sysc4907.command.args;

import java.util.List;

/**
 * Arguments for the remove command
 * @param elementIds the IDs for the elements to remove
 */
public record RemoveCommandArgs(List<Long> elementIds) {
}
