package carleton.sysc4907.processing;

import carleton.sysc4907.EditingAreaProvider;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

public class EditingAreaCoordinateProvider {

    private Scene scene;

    private final String SCROLL_PANE_ID = "scrollPane";

    public EditingAreaCoordinateProvider(Scene scene) {
        this.scene = scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public double getCenterVisibleX() {
        var editingAreaScrollPane = (ScrollPane) scene.lookup("#" + SCROLL_PANE_ID);
        var viewportCenter = editingAreaScrollPane.getViewportBounds().getWidth() / 2;
        System.out.println(editingAreaScrollPane.getViewportBounds().getWidth());
        System.out.println(editingAreaScrollPane.getHvalue());
        var maxScrollDistance = EditingAreaProvider.getEditingArea().getBoundsInLocal().getWidth() - editingAreaScrollPane.getViewportBounds().getWidth();
        var viewportOffset = editingAreaScrollPane.getHvalue() * maxScrollDistance;
        System.out.println(viewportOffset);

        return viewportCenter + viewportOffset;
    }

    public double getCenterVisibleY() {
        var editingAreaScrollPane = (ScrollPane) scene.lookup("#" + SCROLL_PANE_ID);
        var viewportCenter = editingAreaScrollPane.getViewportBounds().getHeight() / 2;
        var maxScrollDistance = EditingAreaProvider.getEditingArea().getBoundsInLocal().getHeight() - editingAreaScrollPane.getViewportBounds().getHeight();
        var viewportOffset = editingAreaScrollPane.getVvalue() * maxScrollDistance;

        return viewportCenter + viewportOffset;
    }
}
