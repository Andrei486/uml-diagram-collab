package carleton.sysc4907.command.args;

import carleton.sysc4907.model.DiagramElement;

public record MoveCommandArgs(double startX, double startY, double endX, double endY, DiagramElement element) {

}
