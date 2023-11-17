package carleton.sysc4907.controller.element;

import carleton.sysc4907.view.DiagramElement;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

/**
 * Class responsible for creating and deleting previews when a diagram element is dragged in the diagram.
 */
public class MovePreviewCreator {

    /**
     * Constructs a new MovePreviewCreator.
     */
    public MovePreviewCreator() {

    }

    /**
     * Creates a semi-transparent move preview for a given element and adds it to the diagram editing pane.
     * @param element the DiagramElement to preview the movement of
     * @param dragStartX the x coordinate where dragging started
     * @param dragStartY the y coordinate where dragging started
     * @return the created preview as an ImageView
     */
    public ImageView createMovePreview(DiagramElement element, double dragStartX, double dragStartY) {
        // create preview
        WritableImage img = element.snapshot(null, new WritableImage(
                (int) element.getBoundsInParent().getWidth(),
                (int) element.getBoundsInParent().getHeight()));

        ImageView preview = new ImageView(img);
        preview.setOpacity(0.5);
        preview.setLayoutX(element.getLayoutX());
        preview.setLayoutY(element.getLayoutY());
        ((Pane) element.getParent()).getChildren().add(preview);
        return preview;
    }

    /**
     * Deletes the specified move preview from the scene.
     * If the preview is null, does nothing.
     * @param element the DiagramElement associated with the move preview to delete
     * @param preview the preview to delete
     */
    public void deleteMovePreview(DiagramElement element, ImageView preview) {
        if (preview == null) {
            return;
        }
        ((Pane) element.getParent()).getChildren().remove(preview);
    }
}