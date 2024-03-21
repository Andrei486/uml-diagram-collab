package carleton.sysc4907.controller.element.arrows;

import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InheritanceArrowheadTest extends AbstractArrowheadTest {

    @Override
    public AbstractArrowhead constructArrowhead() {
        return new InheritanceArrowhead();
    }

    @Test
    public void testMakeArrowheadPathDirect() {
        var arrowhead = constructArrowhead();
        arrowhead.setSize(20);
        var path = new Path();
        path.getElements().add(new MoveTo(0, 0)); // this element will be removed

        arrowhead.makeArrowheadPath(
                path,
                0, 50,
                100, 50,
                false, true
        );

        var elements = path.getElements();
        assertEquals(4, elements.size());

        MoveTo move = (MoveTo) elements.get(0);
        assertEquals(100, move.getX());
        assertEquals(50, move.getY());

        LineTo line = (LineTo) elements.get(1);
        assertTrue(almostEqual(60, line.getY()));
        assertTrue(almostEqual(76, line.getX()));

        line = (LineTo) elements.get(2);
        assertTrue(almostEqual(40, line.getY()));
        assertTrue(almostEqual(76, line.getX()));

        assertTrue(elements.get(3) instanceof ClosePath);
        assertSame(path.getFill(), Color.WHITE);
    }

    @Test
    public void testMakeArrowheadPathEndHorizontal() {
        var arrowhead = constructArrowhead();
        arrowhead.setSize(10);
        var path = new Path();

        arrowhead.makeArrowheadPath(
                path,
                0, 0,
                100, 100,
                true, false
        );

        var elements = path.getElements();
        assertEquals(4, elements.size());

        MoveTo move = (MoveTo) elements.get(0);
        assertEquals(100, move.getX());
        assertEquals(100, move.getY());

        LineTo line = (LineTo) elements.get(1);
        assertTrue(almostEqual(105, line.getY()));
        assertTrue(almostEqual(88, line.getX()));

        line = (LineTo) elements.get(2);
        assertTrue(almostEqual(95, line.getY()));
        assertTrue(almostEqual(88, line.getX()));

        assertTrue(elements.get(3) instanceof ClosePath);
        assertSame(path.getFill(), Color.WHITE);
    }

    @Test
    public void testMakeArrowheadPathEndVertical() {
        var arrowhead = constructArrowhead();
        arrowhead.setSize(10);
        var path = new Path();

        arrowhead.makeArrowheadPath(
                path,
                0, 0,
                100, 200,
                false, false
        );

        var elements = path.getElements();
        assertEquals(4, elements.size());

        MoveTo move = (MoveTo) elements.get(0);
        assertEquals(100, move.getX());
        assertEquals(200, move.getY());

        LineTo line = (LineTo) elements.get(1);
        assertTrue(almostEqual(95, line.getX()));
        assertTrue(almostEqual(188, line.getY()));

        line = (LineTo) elements.get(2);
        assertTrue(almostEqual(105, line.getX()));
        assertTrue(almostEqual(188, line.getY()));

        assertTrue(elements.get(3) instanceof ClosePath);
        assertSame(path.getFill(), Color.WHITE);
    }
}
