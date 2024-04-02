package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.SnapHandleProvider;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.view.EditingAreaLayer;
import carleton.sysc4907.view.SnapHandle;
import javafx.collections.ListChangeListener;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.*;

import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;

/**
 * The base controller for a movable diagram element.
 */
public abstract class DiagramElementController {

    enum SnapHandlePosition {
        WEST, NORTH, EAST, SOUTH
    }

    protected final String SELECTED_STYLE_CLASS = "selected-element";

    @FXML
    protected DiagramElement element;

    protected final ArrayList<SnapHandle> snapHandles;

    @FXML
    private SnapHandle westSnapHandle;
    @FXML
    private SnapHandle eastSnapHandle;
    @FXML
    private SnapHandle southSnapHandle;
    @FXML
    private SnapHandle northSnapHandle;

    private ImageView preview;
    private double dragStartX = 0;
    private double dragStartY = 0;

    private boolean dragging = false;

    private final Map<EventType<MouseEvent>, List<EventHandler<MouseEvent>>> mouseHandlers;

    private final MovePreviewCreator previewCreator;

    private final MoveCommandFactory moveCommandFactory;

    protected final DiagramModel diagramModel;

    protected final SnapHandleProvider snapHandleProvider;

    protected EventHandler<MouseEvent> mouseEventHandler;

    /**
     * Constructs a new DiagramElementController.
     *
     * @param previewCreator     a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory a MoveCommandFactory, used to create move commands
     * @param diagramModel       the DiagramModel for the current diagram
     */
    public DiagramElementController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel) {
        this.previewCreator = previewCreator;
        this.moveCommandFactory = moveCommandFactory;
        this.diagramModel = diagramModel;
        mouseHandlers = new HashMap<>();
        addMouseHandler(MouseEvent.DRAG_DETECTED, this::handleDragDetectedMove);
        addMouseHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDraggedMovePreview);
        addMouseHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleasedDeletePreview);
        addMouseHandler(MouseEvent.MOUSE_PRESSED, this::handleSelect);
        addMouseHandler(MouseEvent.MOUSE_PRESSED, this::handleCancelDragOperations);
        addMouseHandler(MouseEvent.MOUSE_PRESSED, (evt) -> element.requestFocus());
        snapHandles = new ArrayList<>();
        snapHandleProvider = SnapHandleProvider.getSingleton();
    }

    /**
     * Initializes the diagram element, adding movement handlers.
     */
    @FXML
    public void initialize() {
        // Adds registered mouse handlers to the element.
        mouseEventHandler = mouseEvent -> {
            List<EventHandler<MouseEvent>> existingHandlers = mouseHandlers.get(mouseEvent.getEventType());
            if (existingHandlers != null) {
                existingHandlers.forEach(handler -> handler.handle(mouseEvent));
            }
            mouseEvent.consume(); // Make it so only one element receives the event
        };
        element.addEventHandler(MouseEvent.ANY, mouseEventHandler);
        diagramModel.getSelectedElements().addListener((ListChangeListener<DiagramElement>) change -> {
            while (change.next()) {
                if (change.wasAdded() && change.getAddedSubList().contains(element)) {
                    element.getStyleClass().add(SELECTED_STYLE_CLASS);
                } else if (change.wasRemoved() && change.getRemoved().contains(element)) {
                    element.getStyleClass().removeAll(SELECTED_STYLE_CLASS);
                }
            }
        });
        bindSnapHandle(westSnapHandle, SnapHandlePosition.WEST);
        bindSnapHandle(northSnapHandle, SnapHandlePosition.NORTH);
        bindSnapHandle(eastSnapHandle, SnapHandlePosition.EAST);
        bindSnapHandle(southSnapHandle, SnapHandlePosition.SOUTH);
        snapHandleProvider.getSnapHandleList().addAll(snapHandles);
        element.setViewOrder(EditingAreaLayer.ELEMENT.getViewOrder());
    }

    private void bindSnapHandle(SnapHandle handle, SnapHandlePosition position) {
        if (handle == null) {
            return;
        }
        // Bind vertical position within element
        if (position == SnapHandlePosition.NORTH) {
            handle.layoutYProperty().set(0);
        } else if (position == SnapHandlePosition.SOUTH) {
            handle.layoutYProperty().bind(element.maxHeightProperty());
        } else {
            handle.layoutYProperty().bind(element.maxHeightProperty().divide(2));
        }
        // Bind horizontal position within element
        if (position == SnapHandlePosition.WEST) {
            handle.layoutXProperty().set(0);
        } else if (position == SnapHandlePosition.EAST) {
            handle.layoutXProperty().bind(element.maxWidthProperty());
        } else {
            handle.layoutXProperty().bind(element.maxWidthProperty().divide(2));
        }
        // Set horizontal/vertical snap
        handle.setHorizontal(position != SnapHandlePosition.NORTH && position != SnapHandlePosition.SOUTH);
        // Add bound handle to list
        snapHandles.add(handle);
    }

    /**
     * Deletes all previews related to this element. Should be called when this element is deleted.
     * Subclasses should override this if they add any previews that aren't placed as children of the element itself.
     * Does nothing if there are no previews for this element.
     */
    public void deletePreviews() {
        previewCreator.deleteMovePreview(element, preview);
    }

    /**
     * Creates a move preview by taking a screenshot of the element. Also handles any preparation before and after it,
     * to ensure that the preview is the same size as the actual element.
     * @return the preview element as an ImageView
     */
    protected ImageView takeMovePreviewImage() {
        boolean wereSnapHandlesVisible = snapHandles.isEmpty() || snapHandles.get(0).isHandleVisible(); // assume handles are all visible or not
        element.getStyleClass().removeAll(SELECTED_STYLE_CLASS);
        snapHandles.forEach(snapHandle -> snapHandle.setHandleVisible(false));
        var preview = previewCreator.createMovePreview(element, dragStartX, dragStartY);
        element.getStyleClass().add(SELECTED_STYLE_CLASS);
        snapHandles.forEach(snapHandle -> snapHandle.setHandleVisible(wereSnapHandlesVisible));
        return preview;
    }

    protected void addMouseHandler(EventType<MouseEvent> type, EventHandler<MouseEvent> handler) {
        List<EventHandler<MouseEvent>> existingHandlers = mouseHandlers.computeIfAbsent(type, k -> new LinkedList<>());
        existingHandlers.add(handler);
    }

    protected void removeMouseHandler(EventType<MouseEvent> type, EventHandler<MouseEvent> handler) {
        List<EventHandler<MouseEvent>> existingHandlers = mouseHandlers.get(type);
        if (existingHandlers == null) {
            return; // no handler to remove
        }
        existingHandlers.remove(handler);
    }

    protected void handleSelect(MouseEvent event) {
        // Handle left click only
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        if (event.isControlDown()) {
            if (!diagramModel.getSelectedElements().contains(element)) {
                diagramModel.getSelectedElements().add(element);
            } else {
                diagramModel.getSelectedElements().remove(element);
            }
        } else {
            if (!diagramModel.getSelectedElements().contains(element)) {
                diagramModel.getSelectedElements().clear();
                diagramModel.getSelectedElements().add(element);
            }
        }
    }

    protected void handleCancelDragOperations(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            dragging = false;
            previewCreator.deleteMovePreview(element, preview);
        }
    }

    protected void handleDragDetectedMove(MouseEvent event) {
        // Handle left click only
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        dragging = true;
        dragStartX = event.getSceneX() - element.getLayoutX();
        dragStartY = event.getSceneY() - element.getLayoutY();
        previewCreator.deleteMovePreview(element, preview);
        preview = takeMovePreviewImage();
    }

    protected void handleMouseDraggedMovePreview(MouseEvent event) {
        // Handle left click only
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        if (preview != null) {
            double dragEndX = event.getSceneX();
            double dragEndY = event.getSceneY();
            MoveCommandArgs args = new MoveCommandArgs(
                    dragStartX, dragStartY,
                    dragEndX, dragEndY,
                    (Long) preview.getUserData());
            //This is not a tracked command, the preview is local only
            var command = moveCommandFactory.create(args);
            command.execute();
        }
    }

    protected void handleMouseReleasedDeletePreview(MouseEvent event) {
        if (!dragging) {
            return; // Don't move the element on a single click after a drag
        }
        dragging = false;
        previewCreator.deleteMovePreview(element, preview);
        double dragEndX = event.getSceneX();
        double dragEndY = event.getSceneY();
        // these two lines need to be put into a factory ASAP
        MoveCommandArgs args = new MoveCommandArgs(
                dragStartX, dragStartY,
                dragEndX, dragEndY,
                element.getElementId());
        var command = moveCommandFactory.createTracked(args);
        command.execute();
    }
}