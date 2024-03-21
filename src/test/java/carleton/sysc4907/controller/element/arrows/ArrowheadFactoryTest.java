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
        var arrowhead = arrowheadFactory.createArrowhead(ConnectorType.AGGREGATION);
        assertTrue(arrowhead instanceof AggregationArrowhead);
    }

    @Test
    public void testCreateArrowheadComposition() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ConnectorType.COMPOSITION);
        assertTrue(arrowhead instanceof CompositionArrowhead);
    }

    @Test
    public void testCreateArrowheadAssociation() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ConnectorType.ASSOCIATION);
        assertTrue(arrowhead instanceof AssociationArrowhead);
    }

    @Test
    public void testCreateArrowheadInheritance() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ConnectorType.INHERITANCE);
        assertTrue(arrowhead instanceof InheritanceArrowhead);
    }

    @Test
    public void testCreateArrowheadNone() {
        var arrowheadFactory = new ArrowheadFactory();
        var arrowhead = arrowheadFactory.createArrowhead(ConnectorType.NONE);
        assertNull(arrowhead);
    }
}
