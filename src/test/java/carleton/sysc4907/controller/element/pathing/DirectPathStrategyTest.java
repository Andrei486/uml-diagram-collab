package carleton.sysc4907.controller.element.pathing;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

public class DirectPathStrategyTest {

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
        var strategy = new DirectPathStrategy();
        strategy.makePath(
                path,
                startX, startY, isStartHorizontal,
                endX, endY, isEndHorizontal);

        var elements = path.getElements();
        Assertions.assertEquals(2, elements.size());
        LineTo line = (LineTo) elements.get(1);
        Assertions.assertEquals(endX, line.getX());
        Assertions.assertEquals(endY, line.getY());
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
        var strategy = new DirectPathStrategy();
        strategy.makePath(
                path,
                startX, startY, isStartHorizontal,
                endX, endY, isEndHorizontal);

        var elements = path.getElements();
        Assertions.assertEquals(2, elements.size());
        LineTo line = (LineTo) elements.get(1);
        Assertions.assertEquals(endX, line.getX());
        Assertions.assertEquals(endY, line.getY());
    }
}
