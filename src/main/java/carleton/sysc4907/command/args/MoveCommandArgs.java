package carleton.sysc4907.command.args;

import javafx.scene.Node;

/**
 * Arguments for the move command
 * @param startX the starting X coordinate of the element
 * @param startY the starting Y coordinate of the element
 * @param endX the ending X coordinate of the element
 * @param endY the ending Y coordinate of the element
 * @param element the element to be moved
 */
public record MoveCommandArgs(double startX, double startY, double endX, double endY, Node element) {

}