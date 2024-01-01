package carleton.sysc4907.command.args;

import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Node;

import java.util.List;

/**
 * Arguments for the remove command
 * @param elements the elements to remove
 */
public record RemoveCommandArgs(List<DiagramElement> elements) {
}
