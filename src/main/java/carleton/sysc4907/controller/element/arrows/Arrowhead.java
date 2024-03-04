package carleton.sysc4907.controller.element.arrows;

import javafx.scene.shape.Path;

public interface Arrowhead {
    void makeArrowheadPath(
            Path path,
            double startX, double startY,
            double endX, double endY,
            boolean isEndHorizontal, boolean isDirectPath
    );

    void setSize(double size);
}
