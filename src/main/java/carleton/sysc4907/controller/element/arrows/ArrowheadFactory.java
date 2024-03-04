package carleton.sysc4907.controller.element.arrows;

import carleton.sysc4907.controller.element.ConnectorElementController;

public class ArrowheadFactory {

    public Arrowhead createArrowhead(ArrowheadType type) {
        switch (type) {
            case AGGREGATION -> {
                return new AggregationArrowhead();
            }
            case INHERITANCE -> {
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
