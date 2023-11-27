package carleton.sysc4907.command.args;

import javafx.scene.Node;

public record MoveCommandArgs(double startX, double startY, double endX, double endY, Node element) {

}