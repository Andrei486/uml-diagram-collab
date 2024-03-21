package carleton.sysc4907.view;

public enum EditingAreaLayer {

    ELEMENT(10.0), CONNECTOR(5.0);

    private final double viewOrder;
    EditingAreaLayer(double viewOrder)  { this.viewOrder = viewOrder; }
    public double getViewOrder() { return viewOrder; }
}
