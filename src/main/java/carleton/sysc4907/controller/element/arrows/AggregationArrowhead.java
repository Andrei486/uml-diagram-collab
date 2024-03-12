package carleton.sysc4907.controller.element.arrows;

import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class AggregationArrowhead extends AbstractArrowhead {

    private static final double ARROW_LENGTH_MULTIPLIER = 1.4;
    private static final double DIAMOND_WIDTH_MULTIPLIER = 0.5;

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
    @Override
    protected void drawArrowhead(
            Path path,
            double endX, double endY,
            double mainDirectionX, double mainDirectionY,
            double orthogonalDirectionX, double orthogonalDirectionY) {
        path.getElements().add(new MoveTo(endX, endY));
        var size = this.size.get();
        var pointX = endX - (ARROW_LENGTH_MULTIPLIER/2)*size*mainDirectionX + DIAMOND_WIDTH_MULTIPLIER *size*orthogonalDirectionX;
        var pointY = endY - (ARROW_LENGTH_MULTIPLIER/2)*size*mainDirectionY + DIAMOND_WIDTH_MULTIPLIER *size*orthogonalDirectionY;
        path.getElements().add(new LineTo(pointX, pointY));
        pointX = endX - ARROW_LENGTH_MULTIPLIER*size*mainDirectionX;
        pointY = endY - ARROW_LENGTH_MULTIPLIER*size*mainDirectionY;
        path.getElements().add(new LineTo(pointX, pointY));
        pointX = endX - (ARROW_LENGTH_MULTIPLIER/2)*size*mainDirectionX - DIAMOND_WIDTH_MULTIPLIER *size*orthogonalDirectionX;
        pointY = endY - (ARROW_LENGTH_MULTIPLIER/2)*size*mainDirectionY - DIAMOND_WIDTH_MULTIPLIER *size*orthogonalDirectionY;
        path.getElements().add(new LineTo(pointX, pointY));
        path.getElements().add(new ClosePath());
        path.setFill(Color.WHITE);
    }
}
