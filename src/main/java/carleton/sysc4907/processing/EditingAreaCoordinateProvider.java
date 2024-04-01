package carleton.sysc4907.processing;

import carleton.sysc4907.EditingAreaProvider;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

/**
 * Class to handle finding the coordinates of the visible screen, in diagram space.
 */
public class EditingAreaCoordinateProvider {

    private Scene scene;

    private final String SCROLL_PANE_ID = "scrollPane";

    /**
     * Constructs a new EditingAreaCoordinateProvider with the given Scene.
     * @param scene the Scene that contains the editing area
     */
    public EditingAreaCoordinateProvider(Scene scene) {
        this.scene = scene;
    }

    /**
     * Sets the scene to look for the editing area in.
     * @param scene the Scene that contains the editing area
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Gets the X (horizontal) coordinate of the center of the visible area of the diagram.
     * The coordinate returned is in the coordinate space of the editing area, so it can be passed to setLayoutX
     * for diagram elements.
     * @return the X-coordinate of the center of the visible diagram area
     */
    public double getCenterVisibleX() {
        if (scene == null) {
            throw new IllegalStateException("EditingAreaCoordinateProvider scene must be set!");
        }
        var editingAreaScrollPane = (ScrollPane) scene.lookup("#" + SCROLL_PANE_ID);
        var viewportCenter = editingAreaScrollPane.getViewportBounds().getWidth() / 2;
        System.out.println(editingAreaScrollPane.getViewportBounds().getWidth());
        System.out.println(editingAreaScrollPane.getHvalue());
        var maxScrollDistance = EditingAreaProvider.getEditingArea().getBoundsInLocal().getWidth() - editingAreaScrollPane.getViewportBounds().getWidth();
        var viewportOffset = editingAreaScrollPane.getHvalue() * maxScrollDistance;
        System.out.println(viewportOffset);

        return viewportCenter + viewportOffset;
    }

    /**
     * Gets the Y (vertical) coordinate of the center of the visible area of the diagram.
     * The coordinate returned is in the coordinate space of the editing area, so it can be passed to setLayoutY
     * for diagram elements.
     * @return the Y-coordinate of the center of the visible diagram area
     */
    public double getCenterVisibleY() {
        if (scene == null) {
            throw new IllegalStateException("EditingAreaCoordinateProvider scene must be set!");
        }
        var editingAreaScrollPane = (ScrollPane) scene.lookup("#" + SCROLL_PANE_ID);
        var viewportCenter = editingAreaScrollPane.getViewportBounds().getHeight() / 2;
        var maxScrollDistance = EditingAreaProvider.getEditingArea().getBoundsInLocal().getHeight() - editingAreaScrollPane.getViewportBounds().getHeight();
        var viewportOffset = editingAreaScrollPane.getVvalue() * maxScrollDistance;

        return viewportCenter + viewportOffset;
    }
}
