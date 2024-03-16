package carleton.sysc4907.view;

import carleton.sysc4907.controller.element.DiagramElementController;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;

public class SnapHandle extends Circle {

    private boolean active = true;

    private boolean isHorizontal;

    private final Set<Long> snappedConnectorIds;

    public SnapHandle() {
        super();
        setRadius(8);
        getStyleClass().add("snap-handle");
        setHandleVisible(false);
        this.snappedConnectorIds = new HashSet<>();
    }

    public Long getParentElementId() {
        var parent = (DiagramElement) getParent();
        return parent.getElementId();
    }

    public Set<Long> getSnappedConnectorIds() {
        return snappedConnectorIds;
    }

    public DoubleBinding getCenterXBinding() {
        return layoutXProperty().add(getParent().layoutXProperty());
    }

    public DoubleBinding getCenterYBinding() {
        return layoutYProperty().add(getParent().layoutYProperty());
    }

    public boolean isHandleVisible() {
        return active;
    }

    public void setHandleVisible(boolean visible) {
        active = visible;
        setVisible(visible);
    }

    /**
     * Gets whether connectors snapped to this handle should snap horizontally or not.
     * @return true if connectors should snap horizontally, false if vertically
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Sets whether connectors snapped to this handle should snap horizontally or not.
     * @param horizontal true if connectors should snap horizontally, false if vertically
     */
    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public long getElementId() {
        return (Long) getUserData();
    }
}
