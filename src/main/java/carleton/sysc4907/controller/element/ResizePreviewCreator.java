package carleton.sysc4907.controller.element;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

/**
 * Class responsible for creating and deleting previews when a diagram element is dragged to resize in the diagram.
 */
public class ResizePreviewCreator {

    private final ElementIdManager elementIdManager;

    /**
     * Constructs a new ResizePreviewCreator.
     */
    public ResizePreviewCreator(ElementIdManager elementIdManager) {
        this.elementIdManager = elementIdManager;
    }

    public Pane createResizePreview(DiagramElement element) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResizeHandleCreator.class.getResource("/carleton/sysc4907/view/element/ResizePreviewOutline.fxml"));
        Pane preview = null;
        try {
            preview = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        preview.setMaxWidth(element.getMaxWidth());
        preview.setMaxHeight(element.getMaxHeight());
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
    public void deleteResizePreview(DiagramElement element, Pane preview) {
        if (preview == null) {
            return;
        }
        ((Pane) element.getParent()).getChildren().remove(preview);
    }
}
