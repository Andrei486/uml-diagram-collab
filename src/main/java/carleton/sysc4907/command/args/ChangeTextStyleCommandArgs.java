package carleton.sysc4907.command.args;

import carleton.sysc4907.command.TextStyleProperty;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Arguments for a ChangeTextStyleCommand.
 * @param property the style property to change.
 * @param value the new value of the style property.
 * @param elementId the ID of the label to change the style of the text of.
 */
public record ChangeTextStyleCommandArgs(TextStyleProperty property, Object value, Long elementId) implements Serializable {
}
