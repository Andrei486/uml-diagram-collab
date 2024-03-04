package carleton.sysc4907.controller.element.arrows;

import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class CompositionArrowhead extends AbstractArrowhead {

    @Override
    protected void drawArrowhead(
            Path path,
            double endX, double endY,
            double mainDirectionX, double mainDirectionY,
            double orthogonalDirectionX, double orthogonalDirectionY) {
        path.getElements().add(new MoveTo(endX, endY));
        var size = this.size.get();
        var pointX = endX - 0.7*size*mainDirectionX + 0.5*size*orthogonalDirectionX;
        var pointY = endY - 0.7*size*mainDirectionY + 0.5*size*orthogonalDirectionY;
        path.getElements().add(new LineTo(pointX, pointY));
        pointX = endX - 1.4*size*mainDirectionX;
        pointY = endY - 1.4*size*mainDirectionY;
        path.getElements().add(new LineTo(pointX, pointY));
        pointX = endX - 0.7*size*mainDirectionX - 0.5*size*orthogonalDirectionX;
        pointY = endY - 0.7*size*mainDirectionY - 0.5*size*orthogonalDirectionY;
        path.getElements().add(new LineTo(pointX, pointY));
        path.getElements().add(new ClosePath());
        path.setFill(Color.BLACK);
    }
}
