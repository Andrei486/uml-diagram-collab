package carleton.sysc4907.command.args;

import java.io.Serializable;

/**
 * Arguments for the add command
 * @param elementType the type of element to create
 */
public record AddCommandArgs(String elementType, long elementId) implements Serializable {
}
