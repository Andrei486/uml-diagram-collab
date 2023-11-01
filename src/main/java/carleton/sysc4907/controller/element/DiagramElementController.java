package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommand;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramElement;
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

    @FXML
    protected DiagramElement element;

    private ImageView preview;
    private double dragStartX = 0;
    private double dragStartY = 0;

    private final Map<EventType<MouseEvent>, List<EventHandler<MouseEvent>>> mouseHandlers;

    private final MovePreviewCreator previewCreator;

    /**
     * Constructs a new DiagramElementController.
     * @param previewCreator a MovePreviewCreator
     */
    public DiagramElementController(MovePreviewCreator previewCreator) {
        this.previewCreator = previewCreator;
        mouseHandlers = new HashMap<>();
        addMouseHandler(MouseEvent.DRAG_DETECTED, this::handleDragDetectedMove);
        addMouseHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDraggedMovePreview);
        addMouseHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleasedDeletePreview);
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

    protected void handleDragDetectedMove(MouseEvent event) {
        dragStartX = event.getSceneX() - element.getLayoutX();
        dragStartY = event.getSceneY() - element.getLayoutY();
        previewCreator.deleteMovePreview(element, preview);
        preview = previewCreator.createMovePreview(element, dragStartX, dragStartY);
    }

    protected void handleMouseDraggedMovePreview(MouseEvent event) {
        if (preview != null) {
            double dragEndX = event.getSceneX();
            double dragEndY = event.getSceneY();
            // these two lines need to be put into a factory ASAP
            MoveCommandArgs args = new MoveCommandArgs(
                    dragStartX, dragStartY,
                    dragEndX, dragEndY,
                    preview);
            MoveCommand command = new MoveCommand(args);
            command.execute();
        }
    }

    protected void handleMouseReleasedDeletePreview(MouseEvent event) {
        previewCreator.deleteMovePreview(element, preview);
        double dragEndX = event.getSceneX();
        double dragEndY = event.getSceneY();
        // these two lines need to be put into a factory ASAP
        MoveCommandArgs args = new MoveCommandArgs(
                dragStartX, dragStartY,
                dragEndX, dragEndY,
                element);
        MoveCommand command = new MoveCommand(args);
        command.execute();
    }
}
