package carleton.sysc4907.command.args;

import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Node;

import java.util.List;

public record RemoveCommandArgs(List<DiagramElement> elements) {
}
