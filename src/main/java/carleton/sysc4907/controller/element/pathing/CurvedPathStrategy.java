package carleton.sysc4907.controller.element.pathing;

import javafx.scene.shape.*;

/**
 * Pathing strategy used for creating curved paths.
 * Uses a quadratic curve for diagonal paths or a cubic curve for straighter paths.
 */
public class CurvedPathStrategy implements PathingStrategy {

    /**
     * Clears the given path, replacing it with a path from the start point to the end point.
     *
     * @param path              the Path object to set up
     * @param startX            the X (horizontal) coordinate of the start point
     * @param startY            the Y (vertical) coordinate of the start point
     * @param isStartHorizontal true if the line should start horizontally at the start point, otherwise vertical. Used for snapping.
     * @param endX              the X (horizontal) coordinate of the end point
     * @param endY              the Y (vertical) coordinate of the end point
     * @param isEndHorizontal   true if the line should start horizontally at the end point, otherwise vertical. Used for snapping.
     */
    @Override
    public void makePath(
            Path path,
            double startX, double startY, boolean isStartHorizontal,
            double endX, double endY, boolean isEndHorizontal) {
        path.getElements().clear();
        if (isStartHorizontal != isEndHorizontal) {
            makeBentPath(path, startX, startY, isStartHorizontal, endX, endY, isEndHorizontal);
        } else {
            makeElbowPath(path, startX, startY, isStartHorizontal, endX, endY, isEndHorizontal);
        }
    }

    protected void makeBentPath(
            Path path,
            double startX, double startY, boolean isStartHorizontal,
            double endX, double endY, boolean isEndHorizontal
    ) {
        double cornerX = isStartHorizontal ? endX : startX;
        double cornerY = isEndHorizontal ? endY : startY;
        path.getElements().add(new MoveTo(startX, startY));

        path.getElements().add(new QuadCurveTo(cornerX, cornerY, endX, endY));
    }

    protected void makeElbowPath(
            Path path,
            double startX, double startY, boolean isStartHorizontal,
            double endX, double endY, boolean isEndHorizontal
    ) {
        path.getElements().add(new MoveTo(startX, startY));
        if (isStartHorizontal) {
            double ctrlX = (startX + endX) / 2;
            path.getElements().add(new CubicCurveTo(ctrlX, startY, ctrlX, endY, endX, endY));
        } else {
            double ctrlY = (startY + endY) / 2;
            path.getElements().add(new CubicCurveTo(startX, ctrlY, endX, ctrlY, endX, endY));
        }
    }
}
