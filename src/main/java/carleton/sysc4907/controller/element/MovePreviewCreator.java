package carleton.sysc4907.controller.element;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Class responsible for creating and deleting previews when a diagram element is dragged in the diagram.
 */
public class MovePreviewCreator {

    private final ElementIdManager elementIdManager;

    /**
     * Constructs a new MovePreviewCreator.
     */
    public MovePreviewCreator(ElementIdManager elementIdManager) {
        this.elementIdManager = elementIdManager;
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
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage img = element.snapshot(parameters, new WritableImage(
                (int) element.getBoundsInParent().getWidth() + 2,
                (int) element.getBoundsInParent().getHeight() + 2));

        ImageView preview = new ImageView(img);
        preview.setOpacity(0.5);
        preview.setLayoutX(element.getLayoutX());
        preview.setLayoutY(element.getLayoutY());

        Pane editingArea = EditingAreaProvider.getEditingArea();
        editingArea.getChildren().add(preview);
        preview.setUserData(elementIdManager.getNewId());
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