package carleton.sysc4907.controller.element.pathing;

import javafx.scene.shape.Path;

/**
 * Interface representing a Strategy used for creating connector paths given two points.
 */
public interface PathingStrategy {

    /**
     * Clears the given path, replacing it with a path from the start point to the end point.
     * @param path the Path object to set up
     * @param startX the X (horizontal) coordinate of the start point
     * @param startY the Y (vertical) coordinate of the start point
     * @param isStartHorizontal true if the line should start horizontally at the start point, otherwise vertical. Used for snapping.
     * @param endX the X (horizontal) coordinate of the end point
     * @param endY the Y (vertical) coordinate of the end point
     * @param isEndHorizontal true if the line should start horizontally at the end point, otherwise vertical. Used for snapping.
     */
    void makePath(
        Path path,
        double startX, double startY, boolean isStartHorizontal,
        double endX, double endY, boolean isEndHorizontal
    );

    /**
     * Returns true if the path is a direct path (straight line from point A to B), false if the path is orthogonal
     * (follows horizontal/vertical directions instead of straight line).
     * @return true if path is direct, false if path is orthogonal
     */
    boolean isDirectPath();
}
