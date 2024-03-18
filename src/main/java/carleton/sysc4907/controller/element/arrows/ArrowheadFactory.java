package carleton.sysc4907.controller.element.arrows;

/**
 * Factory for creating different types of arrowheads, to prevent direct dependencies to arrowheads.
 */
public class ArrowheadFactory {

    /**
     * Creates an arrowhead corresponding to the specified type.
     * Returns null if no arrowhead should be created.
     * @param type the type of arrowhead to create
     * @return the newly created Arrowhead
     */
    public Arrowhead createArrowhead(ConnectorType type) {
        switch (type) {
            case AGGREGATION -> {
                return new AggregationArrowhead();
            }
            case INHERITANCE, IMPLEMENTATION -> {
                return new InheritanceArrowhead();
            }
            case COMPOSITION -> {
                return new CompositionArrowhead();
            }
            case ASSOCIATION -> {
                return new AssociationArrowhead();
            }
            default -> {
                return null;
            }
        }
    }
}
