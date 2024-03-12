package carleton.sysc4907.controller.element.arrows;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Abstract superclass for all arrowheads, abstracting away some of the geometry and managing size.
 */
public abstract class AbstractArrowhead implements Arrowhead {

    protected final DoubleProperty size = new SimpleDoubleProperty();

    /**
     * Constructs an arrowhead with a default size.
     */
    public AbstractArrowhead() {
        this(12);
    }

    /**
     * Constructs an arrowhead with a specified size (in pixels).
     * Not inherited by default.
     * @param size the size of the arrowhead, in pixels. Refers to the width of the arrowhead.
     */
    public AbstractArrowhead(double size) {
        setSize(size);
    }

    /**
     * Sets the size of the arrowhead.
     * The size defines the width of the arrowhead, and affects the length proportionally.
     * @param size the new size
     */
    public void setSize(double size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Arrowhead size must be greater than 0.");
        }
        this.size.set(size);
    }

    /**
     * Gets the current size of the arrowhead, which is its width in pixels.
     * @return the arrowhead size
     */
    public double getSize() {
        return size.get();
    }

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
    public void makeArrowheadPath(
            Path path,
            double startX, double startY,
            double endX, double endY,
            boolean isEndHorizontal, boolean isDirectPath
    ) {
        path.getElements().clear();

        if (startX == endX && startY == endY) {
            return; // make no arrowhead, this would cause division by 0 and the path is a point
        }

        var mainX = (!isDirectPath && !isEndHorizontal) ? 0 : endX - startX;
        var mainY = (!isDirectPath && isEndHorizontal) ? 0 : endY - startY;

        var mainLength = Math.sqrt(mainX*mainX + mainY*mainY);
        mainX /= mainLength;
        mainY /= mainLength;

        var orthogonalX = -mainY;
        var orthogonalY = mainX;

        drawArrowhead(
                path,
                endX, endY,
                mainX, mainY,
                orthogonalX, orthogonalY
        );
    }

    /**
     * Draws the arrowhead given geometry information, and stores it in the given Path object.
     * @param path the Path object to store the arrowhead path in, which must be empty.
     * @param endX the X coordinate of the connector's end point
     * @param endY the Y coordinate of the connector's end point
     * @param mainDirectionX the X coordinate of a normalized vector pointing in the path's direction at the end (pointing away from the start)
     * @param mainDirectionY the Y coordinate of a normalized vector pointing in the path's direction at the end (pointing away from the start)
     * @param orthogonalDirectionX the X coordinate of a normalized vector perpendicular to the path's direction at the end
     * @param orthogonalDirectionY the Y coordinate of a normalized vector perpendicular to the path's direction at the end
     */
    protected abstract void drawArrowhead(
            Path path,
            double endX, double endY,
            double mainDirectionX, double mainDirectionY,
            double orthogonalDirectionX, double orthogonalDirectionY
    );
}
