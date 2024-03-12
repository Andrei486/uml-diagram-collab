package carleton.sysc4907.controller.element.arrows;

import carleton.sysc4907.controller.element.ConnectorElementController;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractArrowheadTest {

    public abstract AbstractArrowhead constructArrowhead();

    @Test
    public void sizeSetOnConstructor() {
        var arrowhead = constructArrowhead();
        assertEquals(12.0, arrowhead.getSize());
    }

    @Test
    public void setAndGetSize() {
        var arrowhead = constructArrowhead();
        arrowhead.setSize(20.5);
        assertEquals(20.5, arrowhead.getSize());
    }

    @Test
    public void testMakeArrowheadPathPoint() {
        var arrowhead = constructArrowhead();
        var path = new Path();
        path.getElements().add(new MoveTo(0, 0)); // this element will be removed

        arrowhead.makeArrowheadPath(
                path,
                200, -100,
                200, -100,
                false, false
        );

        assertEquals(0, path.getElements().size());
    }

    protected boolean almostEqual(double a, double b) {
        return almostEqual(a, b, 1);
    }

    protected boolean almostEqual(double a, double b, double differenceThreshold){
        System.out.println(a + " " + b);
        return Math.abs(a-b)<differenceThreshold;
    }
}
