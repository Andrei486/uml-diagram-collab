package carleton.sysc4907.command.args;

import java.io.Serializable;

public record EditTextCommandArgs(String text, long elementId) implements Serializable {
}
