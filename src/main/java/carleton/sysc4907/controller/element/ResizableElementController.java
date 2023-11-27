package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.scene.Node;
import java.util.LinkedList;
import java.util.List;

/**
 * FXML controller for a resizable diagram element.
 */
public abstract class ResizableElementController extends DiagramElementController {

    private final List<Node> resizeHandles;

    private final ResizeHandleCreator resizeHandleCreator;

    private double resizeDragStartX = 0;
    private double resizeDragStartY = 0;

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
            ResizeHandleCreator resizeHandleCreator) {
        super(previewCreator, moveCommandFactory, diagramModel);
        this.resizeHandleCreator = resizeHandleCreator;
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
            handle.setOnDragDetected(event -> {
                resizeDragging = true;
                resizeDragStartX = event.getSceneX();
                resizeDragStartY = event.getSceneY();
                event.consume();
            });
            handle.setOnMouseDragged(Event::consume);
            handle.setOnMouseReleased(event -> {
                if (!resizeDragging) {
                    return;
                }
                resize(isTop,
                        isRight,
                        resizeDragStartX,
                        resizeDragStartY,
                        event.getSceneX(),
                        event.getSceneY());
                resizeDragging = false;
                toggleShowResizeHandles(false);
                toggleShowResizeHandles(true);
                event.consume();
            });
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

    public void resize(
            boolean isTopAnchor,
            boolean isRightAnchor,
            double dragStartX,
            double dragStartY,
            double dragEndX,
            double dragEndY) {
        double widthChange = dragEndX - dragStartX;
        widthChange = isRightAnchor ? widthChange : -widthChange;
        widthChange = Math.max(widthChange, -element.getMaxWidth() + 20);
        double heightChange = dragEndY - dragStartY;
        heightChange = isTopAnchor ? -heightChange : heightChange;
        heightChange = Math.max(heightChange, -element.getMaxHeight() + 20);
        element.setMaxWidth(element.getMaxWidth() + widthChange);
        element.setMaxHeight(element.getMaxHeight() + heightChange);
        if (isTopAnchor) {
            element.setLayoutY(element.getLayoutY() - heightChange);
        }
        if (!isRightAnchor) {
            element.setLayoutX(element.getLayoutX() - widthChange);
        }
    }
}
