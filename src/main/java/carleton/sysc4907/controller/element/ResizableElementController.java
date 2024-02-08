package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.command.args.ResizeCommandArgs;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.LinkedList;
import java.util.List;

/**
 * FXML controller for a resizable diagram element.
 */
public abstract class ResizableElementController extends DiagramElementController {

    private final List<Node> resizeHandles;

    private final ResizeHandleCreator resizeHandleCreator;
    private final ResizeCommandFactory resizeCommandFactory;
    private final ResizePreviewCreator resizePreviewCreator;
    private Pane preview = null;
    private double resizeDragStartX = 0;
    private double resizeDragStartY = 0;
    private double lastPreviewDragX = 0;
    private double lastPreviewDragY = 0;

    private boolean resizeDragging = false;

    /**
     * Constructs a new ResizableElementController.
     *
     * @param previewCreator     a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory a MoveCommandFactory, used to create move commands
     * @param diagramModel       the DiagramModel for the current diagram
     */
    public ResizableElementController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel,
            ResizeHandleCreator resizeHandleCreator,
            ResizePreviewCreator resizePreviewCreator,
            ResizeCommandFactory resizeCommandFactory) {
        super(previewCreator, moveCommandFactory, diagramModel);
        this.resizeHandleCreator = resizeHandleCreator;
        this.resizePreviewCreator = resizePreviewCreator;
        this.resizeCommandFactory = resizeCommandFactory;
        this.resizeHandles = new LinkedList<>();
        diagramModel.getSelectedElements().addListener((ListChangeListener<DiagramElement>) change -> {
            while (change.next()) {
                if (change.wasAdded() && change.getAddedSubList().contains(element)) {
                    toggleShowResizeHandles(true);
                } else if (change.wasRemoved() && change.getRemoved().contains(element)) {
                    toggleShowResizeHandles(false);
                }
            }
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        element.maxWidthProperty().addListener((observableValue, number, t1) -> {
            boolean showHandles = diagramModel.getSelectedElements().contains(element);
            toggleShowResizeHandles(!showHandles);
            toggleShowResizeHandles(showHandles);
        });
        element.maxHeightProperty().addListener((observableValue, number, t1) -> {
            boolean showHandles = diagramModel.getSelectedElements().contains(element);
            toggleShowResizeHandles(!showHandles);
            toggleShowResizeHandles(showHandles);
        });
    }

    @Override
    public void deletePreviews() {
        super.deletePreviews();
        resizePreviewCreator.deleteResizePreview(element, preview);
    }

    @Override
    protected ImageView takeMovePreviewImage() {
        toggleShowResizeHandles(false);
        var preview = super.takeMovePreviewImage();
        toggleShowResizeHandles(true);
        return preview;
    }

    private void createResizeHandles() {
        for (Node handle : resizeHandles) {
            resizeHandleCreator.deleteResizeHandle(element, handle);
        }
        resizeHandles.clear();
        for (int i = 0; i < 4; i++) {
            boolean isTop = i % 2 == 0;
            boolean isRight = i / 2 == 0;
            Node handle = resizeHandleCreator.createResizeHandle(element, isTop, isRight);
            handle.setOnDragDetected(this::handleDragDetectedStartResize);
            handle.setOnMouseDragged(event -> handleMouseDraggedResizePreview(event, isTop, isRight));
            handle.setOnMouseReleased(event -> handleMouseReleasedResize(event, isTop, isRight));
            handle.setOnMouseClicked(Event::consume);
            resizeHandles.add(handle);
        }
    }

    private void toggleShowResizeHandles(boolean showHandles) {
        if (showHandles) {
            createResizeHandles();
        } else {
            for (Node handle : resizeHandles) {
                resizeHandleCreator.deleteResizeHandle(element, handle);
            }
            resizeHandles.clear();
        }
    }

    private void handleDragDetectedStartResize(MouseEvent event) {
        resizeDragging = true;
        resizeDragStartX = event.getSceneX();
        resizeDragStartY = event.getSceneY();
        lastPreviewDragX = resizeDragStartX;
        lastPreviewDragY = resizeDragStartY;
        resizePreviewCreator.deleteResizePreview(element, preview);
        element.getStyleClass().removeAll(SELECTED_STYLE_CLASS);
        preview = resizePreviewCreator.createResizePreview(element);
        element.getStyleClass().add(SELECTED_STYLE_CLASS);
        event.consume();
    }

    private void handleMouseDraggedResizePreview(MouseEvent event, boolean isTop, boolean isRight) {
        // Handle left click only
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        if (preview != null) {
            // Reset the preview and resize it from there. This is to avoid issues where the preview becomes desynced
            // because of the minimum size. Alternatively we could make an entirely separate command for this, since
            // it isn't actually affecting the diagram.
            preview.setMaxWidth(element.getMaxWidth());
            preview.setMaxHeight(element.getMaxHeight());
            preview.setLayoutX(element.getLayoutX());
            preview.setLayoutY(element.getLayoutY());
            ResizeCommandArgs args = new ResizeCommandArgs(
                    isTop,
                    isRight,
                    resizeDragStartX,
                    resizeDragStartY,
                    event.getSceneX(),
                    event.getSceneY(),
                    (long) preview.getUserData()
            );
            // this is not a tracked command because it's the preview,
            // we do not want to send it over TCP or add it to the command stack
            var command = resizeCommandFactory.create(args);
            command.execute();
        }
        lastPreviewDragX = event.getSceneX();
        lastPreviewDragY = event.getSceneY();
        event.consume();
    }

    private void handleMouseReleasedResize(MouseEvent event, boolean isTop, boolean isRight) {
        if (!resizeDragging) {
            return;
        }
        resizeDragging = false;
        resizePreviewCreator.deleteResizePreview(element, preview);
        var command = resizeCommandFactory.createTracked(new ResizeCommandArgs(
                isTop,
                isRight,
                resizeDragStartX,
                resizeDragStartY,
                event.getSceneX(),
                event.getSceneY(),
                element.getElementId()
        ));
        command.execute();
        event.consume();
    }
}
