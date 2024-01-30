package carleton.sysc4907.controller.element.pathing;

import javafx.scene.shape.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CurvedPathStrategyTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void makePathSameDirections(int isHorizontal) {
        boolean isStartHorizontal = isHorizontal == 0;
        boolean isEndHorizontal = isStartHorizontal;
        var startX = 100.0;
        var startY = 120.0;
        var endX = 200.0;
        var endY = 220.0;
        Path path = new Path();
        var strategy = new CurvedPathStrategy();
        strategy.makePath(
                path,
                startX, startY, isStartHorizontal,
                endX, endY, isEndHorizontal);

        var elements = path.getElements();
        Assertions.assertEquals(2, elements.size());
        CubicCurveTo line = (CubicCurveTo) elements.get(1);
        Assertions.assertEquals(endX, line.getX());
        Assertions.assertEquals(endY, line.getY());
        if (isStartHorizontal) {
            Assertions.assertEquals(startY, line.getControlY1());
            Assertions.assertEquals(endY, line.getControlY2());
        } else {
            Assertions.assertEquals(startX, line.getControlX1());
            Assertions.assertEquals(endX, line.getControlX2());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void makePathDifferentDirections(int isStartHorizontalFlag) {
        boolean isStartHorizontal = isStartHorizontalFlag == 0;
        boolean isEndHorizontal = !isStartHorizontal;
        var startX = 100.0;
        var startY = 120.0;
        var endX = 200.0;
        var endY = 220.0;
        Path path = new Path();
        var strategy = new CurvedPathStrategy();
        strategy.makePath(
                path,
                startX, startY, isStartHorizontal,
                endX, endY, isEndHorizontal);

        var elements = path.getElements();
        Assertions.assertEquals(2, elements.size());
        QuadCurveTo line = (QuadCurveTo) elements.get(1);
        Assertions.assertEquals(endX, line.getX());
        Assertions.assertEquals(endY, line.getY());
        if (isStartHorizontal) {
            Assertions.assertEquals(endX, line.getControlX());
            Assertions.assertEquals(startY, line.getControlY());
        } else {
            Assertions.assertEquals(startX, line.getControlX());
            Assertions.assertEquals(endY, line.getControlY());
        }
    }
}
