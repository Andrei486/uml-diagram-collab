package carleton.sysc4907.controller.element.arrows;

import carleton.sysc4907.controller.element.ConnectorElementController;

public class ArrowheadFactory {

    public Arrowhead createArrowhead(ArrowheadType type) {
        switch (type) {
            case AGGREGATION -> {
                return new AggregationArrowhead();
            }
            default -> {
                return null;
            }
        }
    }
}
