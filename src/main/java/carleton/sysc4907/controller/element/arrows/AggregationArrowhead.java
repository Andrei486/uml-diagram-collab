package carleton.sysc4907.controller.element.arrows;

import carleton.sysc4907.controller.element.ConnectorElementController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class AggregationArrowhead implements Arrowhead {

    private final DoubleProperty size = new SimpleDoubleProperty();

    public AggregationArrowhead() {
        this.size.set(12);
    }

    public void setSize(double size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Arrowhead size must be greater than 0.");
        }
        this.size.set(size);
    }

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

        var mainX = endX - startX;
        var mainY = endY - startY;
        var mainLength = Math.sqrt(mainX*mainX + mainY*mainY);
        mainX /= mainLength;
        mainY /= mainLength;

        var orthogonalX = -mainY;
        var orthogonalY = mainX;

        path.getElements().add(new MoveTo(endX, endY));
        var size = this.size.get();
        var pointX = endX - 0.7*size*mainX + 0.5*size*orthogonalX;
        var pointY = endY - 0.7*size*mainY + 0.5*size*orthogonalY;
        path.getElements().add(new LineTo(pointX, pointY));
        pointX = endX - 1.4*size*mainX;
        pointY = endY - 1.4*size*mainY;
        path.getElements().add(new LineTo(pointX, pointY));
        pointX = endX - 0.7*size*mainX - 0.5*size*orthogonalX;
        pointY = endY - 0.7*size*mainY - 0.5*size*orthogonalY;
        path.getElements().add(new LineTo(pointX, pointY));
        path.getElements().add(new ClosePath());
        path.setFill(Color.WHITE);
    }
}
