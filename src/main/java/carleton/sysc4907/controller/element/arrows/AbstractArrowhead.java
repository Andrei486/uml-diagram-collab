package carleton.sysc4907.controller.element.arrows;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public abstract class AbstractArrowhead implements Arrowhead {

    protected final DoubleProperty size = new SimpleDoubleProperty();

    public AbstractArrowhead() {
        this(12);
    }

    public AbstractArrowhead(double size) {
        setSize(size);
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

    protected abstract void drawArrowhead(
            Path path,
            double endX, double endY,
            double mainDirectionX, double mainDirectionY,
            double orthogonalDirectionX, double orthogonalDirectionY
    );
}
