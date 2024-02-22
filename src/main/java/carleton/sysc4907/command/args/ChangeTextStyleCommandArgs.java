package carleton.sysc4907.command.args;

import carleton.sysc4907.command.TextStyleProperty;

import java.io.Serializable;
import java.util.HashMap;

public record ChangeTextStyleCommandArgs(HashMap<TextStyleProperty, String> properties, long elementId) implements Serializable {
}
