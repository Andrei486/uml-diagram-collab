package carleton.sysc4907.model;

import carleton.sysc4907.view.SnapHandle;
import javafx.geometry.Bounds;

import java.util.LinkedList;
import java.util.List;

public class SnapHandleProvider {

    private final List<SnapHandle> snapHandleList;

    private static SnapHandleProvider singleton = null;

    public SnapHandleProvider() {
        this.snapHandleList = new LinkedList<>();
    }

    public List<SnapHandle> getSnapHandleList() {
        return snapHandleList;
    }

    public SnapHandle getSnapHandleAtPosition(double x, double y, List<SnapHandle> handlesToExclude) {
        var includedHandleList = new LinkedList<>(snapHandleList);
        includedHandleList.removeAll(handlesToExclude);
        for (var handle : includedHandleList) {
            var boundsInScene = handle.localToScene(handle.getBoundsInLocal());
            if (handle.isHandleVisible() && boundsInScene.contains(x, y)) {
                return handle;
            }
        }
        return null;
    }

    public void setAllHandlesVisible(boolean visible) {
        snapHandleList.forEach(handle -> handle.setHandleVisible(visible));
    }

    public static SnapHandleProvider getSingleton() {
        if (singleton == null) {
            singleton = new SnapHandleProvider();
        }
        return singleton;
    }
}
