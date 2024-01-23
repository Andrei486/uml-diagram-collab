package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.collections.ListChangeListener;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.event.EventHandler;

/**
 * The base controller for a movable diagram element.
 */
public abstract class DiagramElementController {

    protected final String SELECTED_STYLE_CLASS = "selected-element";

    @FXML
    protected DiagramElement element;

    private ImageView preview;
    private double dragStartX = 0;
    private double dragStartY = 0;

    private boolean dragging = false;

    private final Map<EventType<MouseEvent>, List<EventHandler<MouseEvent>>> mouseHandlers;

    private final MovePreviewCreator previewCreator;

    private final MoveCommandFactory moveCommandFactory;

    protected final DiagramModel diagramModel;

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
    }

    /**
     * Initializes the diagram element, adding movement handlers.
     */
    @FXML
    public void initialize() {
        // Adds registered mouse handlers to the element.
        element.addEventHandler(MouseEvent.ANY, mouseEvent -> {
            List<EventHandler<MouseEvent>> existingHandlers = mouseHandlers.get(mouseEvent.getEventType());
            if (existingHandlers != null) {
                existingHandlers.forEach(handler -> handler.handle(mouseEvent));
            }
            mouseEvent.consume(); // Make it so only one element receives the event
        });
        diagramModel.getSelectedElements().addListener((ListChangeListener<DiagramElement>) change -> {
            while (change.next()) {
                if (change.wasAdded() && change.getAddedSubList().contains(element)) {
                    element.getStyleClass().add(SELECTED_STYLE_CLASS);
                } else if (change.wasRemoved() && change.getRemoved().contains(element)) {
                    element.getStyleClass().removeAll(SELECTED_STYLE_CLASS);
                }
            }
        });
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

    protected void handleDragDetectedMove(MouseEvent event) {
        // Handle left click only
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        dragging = true;
        dragStartX = event.getSceneX();
        dragStartY = event.getSceneY();
        previewCreator.deleteMovePreview(element, preview);
        element.getStyleClass().removeAll(SELECTED_STYLE_CLASS);
        preview = previewCreator.createMovePreview(element, dragStartX, dragStartY);
        element.getStyleClass().add(SELECTED_STYLE_CLASS);
    }

    protected void handleMouseDraggedMovePreview(MouseEvent event) {
        // Handle left click only
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        if (preview != null) {
            double dragEndX = event.getSceneX() + element.getLayoutX() - preview.getLayoutX();
            double dragEndY = event.getSceneY() + element.getLayoutY() - preview.getLayoutY();
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