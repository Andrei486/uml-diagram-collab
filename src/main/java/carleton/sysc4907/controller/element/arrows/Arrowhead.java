package carleton.sysc4907.controller.element.arrows;

import javafx.scene.shape.Path;

/**
 * Represents an arrowhead for a connector.
 */
public interface Arrowhead {

    /**
     * Draws the arrowhead's path given the connector information, and stores it into a given Path object.
     * The arrowhead will be drawn at the end point of the connector, in the direction of the path towards the start.
     * The Path object is overwritten.
     * @param path the Path to store the arrowhead path in. Will be overwritten.
     * @param startX the X coordinate of the connector's start point
     * @param startY the Y coordinate of the connector's start point
     * @param endX the X coordinate of the connector's end point
     * @param endY the Y coordinate of the connector's end point
     * @param isEndHorizontal true if the connector's end is horizontal, false if it is vertical
     * @param isDirectPath true if the path is direct, false otherwise. If true, isEndHorizontal may be ignored.
     */
    void makeArrowheadPath(
            Path path,
            double startX, double startY,
            double endX, double endY,
            boolean isEndHorizontal, boolean isDirectPath
    );

    /**
     * Sets the size of the arrowhead.
     * The size defines the width of the arrowhead, and affects the length proportionally.
     * @param size the new size
     */
    void setSize(double size);
}
