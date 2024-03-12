package carleton.sysc4907.controller.element.arrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArrowheadFactoryTest {

    @Test
    public void testCreateArrowheadAggregation() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ArrowheadType.AGGREGATION);
        assertTrue(arrowhead instanceof AggregationArrowhead);
    }

    @Test
    public void testCreateArrowheadComposition() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ArrowheadType.COMPOSITION);
        assertTrue(arrowhead instanceof CompositionArrowhead);
    }

    @Test
    public void testCreateArrowheadAssociation() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ArrowheadType.ASSOCIATION);
        assertTrue(arrowhead instanceof AssociationArrowhead);
    }

    @Test
    public void testCreateArrowheadInheritance() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ArrowheadType.INHERITANCE);
        assertTrue(arrowhead instanceof InheritanceArrowhead);
    }

    @Test
    public void testCreateArrowheadNone() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ArrowheadType.NONE);
        assertNull(arrowhead);
    }
}
