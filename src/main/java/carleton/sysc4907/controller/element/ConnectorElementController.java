package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.ConnectorMovePointCommandFactory;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.controller.element.pathing.PathingStrategy;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.view.DiagramElement;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;

import java.util.LinkedList;
import java.util.List;

public class ConnectorElementController extends DiagramElementController {

    private final double AUTO_DIRECTION_THRESHOLD = 4.0;
    private final ConnectorHandleCreator connectorHandleCreator;
    private final ConnectorMovePointPreviewCreator connectorMovePointPreviewCreator;
    private final ConnectorMovePointCommandFactory connectorMovePointCommandFactory;
    private final List<Node> handles;

    // All coordinates relative to the parent element (editing area)!
    protected final DoubleProperty startX = new SimpleDoubleProperty();
    protected final DoubleProperty startY = new SimpleDoubleProperty();
    protected final DoubleProperty endX = new SimpleDoubleProperty();
    protected final DoubleProperty endY = new SimpleDoubleProperty();

    protected final BooleanProperty isStartHorizontal = new SimpleBooleanProperty();

    private boolean isStartSnapping = false;
    protected final BooleanProperty isEndHorizontal = new SimpleBooleanProperty();

    private boolean isEndSnapping = false;

    @FXML
    private Path connectorPath;
    protected ObjectProperty<PathingStrategy> pathingStrategy = new SimpleObjectProperty<>();
    @FXML
    private Path pathHitbox;

    private boolean movePointDragging = false;
    private double dragStartX;
    private double dragStartY;

    private boolean repositioning = false;

    private ConnectorElementController previewController;

    /**
     * Constructs a new ConnectorElementController.
     *
     * @param previewCreator     a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory a MoveCommandFactory, used to create move commands
     * @param diagramModel       the DiagramModel for the current diagram
     */
    public ConnectorElementController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel,
            ConnectorHandleCreator connectorHandleCreator,
            ConnectorMovePointPreviewCreator connectorMovePointPreviewCreator,
            ConnectorMovePointCommandFactory connectorMovePointCommandFactory,
            PathingStrategy pathingStrategy) {
        super(previewCreator, moveCommandFactory, diagramModel);
        this.connectorHandleCreator = connectorHandleCreator;
        this.connectorMovePointPreviewCreator = connectorMovePointPreviewCreator;
        this.connectorMovePointCommandFactory = connectorMovePointCommandFactory;
        this.pathingStrategy.set(pathingStrategy);
        this.handles = new LinkedList<>();
        isStartHorizontal.set(true);
        isEndHorizontal.set(true);
        diagramModel.getSelectedElements().addListener((ListChangeListener<DiagramElement>) change -> {
            while (change.next()) {
                if (change.wasAdded() && change.getAddedSubList().contains(element)) {
                    toggleShowPointDragHandles(true);
                } else if (change.wasRemoved() && change.getRemoved().contains(element)) {
                    toggleShowPointDragHandles(false);
                }
            }
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        element.removeEventHandler(MouseEvent.ANY, mouseEventHandler);
        pathHitbox.addEventHandler(MouseEvent.ANY, mouseEventHandler);
        ChangeListener<Number> listener = (observableValue, number, t1) -> {
            reposition();
            recalculatePath();
            updateDirections();
        };
        ChangeListener<Boolean> booleanListener = (observableValue, bool, t1) -> {
            recalculatePath();
        };
        startX.addListener(listener);
        startY.addListener(listener);
        endX.addListener(listener);
        endY.addListener(listener);
        isStartHorizontal.addListener(booleanListener);
        isEndHorizontal.addListener(booleanListener);
        element.layoutXProperty().addListener((observableValue, number, t1) -> {
            if (repositioning) {
                return;
            }
            double deltaX = t1.doubleValue() - number.doubleValue();
            setStartX(deltaX + getStartX());
            setEndX(deltaX + getEndX());
        });
        element.layoutYProperty().addListener((observableValue, number, t1) -> {
            if (repositioning) {
                return;
            }
            double deltaY = t1.doubleValue() - number.doubleValue();
            setStartY(deltaY + getStartY());
            setEndY(deltaY + getEndY());
        });
        setEndX(100);
        setEndY(20);
    }

    @Override
    protected ImageView takeMovePreviewImage() {
        toggleShowPointDragHandles(false);
        var preview = super.takeMovePreviewImage();
        toggleShowPointDragHandles(true);
        return preview;
    }

    /**
     * Shows or hides handles which allow the endpoints of the connector to be moved.
     * @param showHandles true if the handles for moving points should be shown, false if they should be hidden
     */
    private void toggleShowPointDragHandles(boolean showHandles) {
        if (showHandles) {
            var handle = connectorHandleCreator.createMovePointHandle(element, startX, startY);
            handle.setOnDragDetected(this::handleDragDetectedStartMovePoint);
            handle.setOnMouseDragged(event -> handleMouseDraggedMovePreviewPoint(event, true));
            handle.setOnMouseReleased(event -> handleMouseReleasedResize(event, true));
            handles.add(handle);
            handle = connectorHandleCreator.createMovePointHandle(element, endX, endY);
            handle.setOnDragDetected(this::handleDragDetectedStartMovePoint);
            handle.setOnMouseDragged(event -> handleMouseDraggedMovePreviewPoint(event, false));
            handle.setOnMouseReleased(event -> handleMouseReleasedResize(event, false));
            handles.add(handle);
        } else {
            for (Node handle : handles) {
                connectorHandleCreator.deleteMovePointHandle(element, handle);
            }
            handles.clear();
        }
    }

    /**
     * Repositions the element so that neither of the endpoints have negative coordinates.
     * This will move the endpoints to compensate, so that they are at the same position on the diagram.
     */
    private void reposition() {
        repositioning = true;
        double leftmostX = Math.min(getStartX(), getEndX());
        double topmostY = Math.min(getStartY(), getEndY());
        element.setLayoutX(leftmostX);
        element.setLayoutY(topmostY);
        repositioning = false;
    }

    /**
     * Uses this connector's pathing strategy to re-make the connector's path. Overwrites the previous path.
     */
    protected void recalculatePath() {
        pathingStrategy.get().makePath(
                connectorPath,
                adjustX(getStartX()), adjustY(getStartY()), isStartHorizontal.get(),
                adjustX(getEndX()), adjustY(getEndY()), isEndHorizontal.get()
        );
        pathingStrategy.get().makePath(
                pathHitbox,
                adjustX(getStartX()), adjustY(getStartY()), isStartHorizontal.get(),
                adjustX(getEndX()), adjustY(getEndY()), isEndHorizontal.get()
        );
    }

    /**
     * Updates this connector's start and end directions, i.e. whether the path should start and end
     * horizontally or vertically.
     * Paths that are close to horizontal or vertical will be set to be in that direction.
     * Endpoints that are marked as "snapped" will not have their directions changed.
     */
    public void updateDirections() {
        var deltaX = Math.abs(getEndX() - getStartX()) + 0.01; // add 0.01 to avoid division by 0 errors
        var deltaY = Math.abs(getEndY() - getStartY()) + 0.01;

        if (isStartSnapping && isEndSnapping) {
            return; // can't update anything
        }

        if (isStartSnapping) {
            if (deltaX / deltaY > AUTO_DIRECTION_THRESHOLD) {
                isEndHorizontal.set(true);
            } else if (deltaY / deltaX > AUTO_DIRECTION_THRESHOLD) {
                // mostly vertical
                isEndHorizontal.set(false);
            } else {
                isEndHorizontal.set(isStartHorizontal.get());
            }
        } else if (isEndSnapping) {
            if (deltaX / deltaY > AUTO_DIRECTION_THRESHOLD) {
                isStartHorizontal.set(true);
            } else if (deltaY / deltaX > AUTO_DIRECTION_THRESHOLD) {
                // mostly vertical
                isStartHorizontal.set(false);
            } else {
                isStartHorizontal.set(isEndHorizontal.get());
            }
        } else {
            if (deltaX / deltaY > AUTO_DIRECTION_THRESHOLD) {
                // mostly horizontal
                isStartHorizontal.set(true);
                isEndHorizontal.set(true);
            } else if (deltaY / deltaX > AUTO_DIRECTION_THRESHOLD) {
                // mostly vertical
                isStartHorizontal.set(false);
                isEndHorizontal.set(false);
            } else if (deltaX > deltaY){
                // diagonal enough, arbitrary
                isStartHorizontal.set(true);
                isEndHorizontal.set(false);
            } else {
                isStartHorizontal.set(false);
                isEndHorizontal.set(true);
            }
        }
    }

    /**
     * Handler for the endpoint move handles, starts a drag operation.
     * @param event the mouse event
     */
    private void handleDragDetectedStartMovePoint(MouseEvent event) {
        movePointDragging = true;
        previewController = connectorMovePointPreviewCreator.createMovePointPreview(this);
        dragStartX = event.getSceneX();
        dragStartY = event.getSceneY();
        event.consume();
    }

    private void handleMouseDraggedMovePreviewPoint(MouseEvent event, boolean isStart) {
        // Handle left click only
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        if (previewController != null) {
            double startX;
            double startY;
            if (isStart) {
                startX = dragStartX - getStartX();
                startY = dragStartY - getStartY();
            } else {
                startX = dragStartX - getEndX();
                startY = dragStartY - getEndY();
            }
            var args = new ConnectorMovePointCommandArgs(
                    isStart,
                    startX,
                    startY,
                    event.getSceneX(),
                    event.getSceneY(),
                    previewController.element.getElementId());
            var command = connectorMovePointCommandFactory.create(args);
            command.execute();
        }
    }

    /**
     * Handler for the endpoint move handlers. Moves the dragged endpoint to where the mouse was released.
     * @param event the mouse release event
     * @param isStart true if the start point was the one dragged, false if it was the end point
     */
    private void handleMouseReleasedResize(MouseEvent event, boolean isStart) {
        if (!movePointDragging) {
            return;
        }
        movePointDragging = false;
        connectorMovePointPreviewCreator.deleteMovePreview(element, previewController);
        previewController = null;
        double startX;
        double startY;
        if (isStart) {
            startX = dragStartX - getStartX();
            startY = dragStartY - getStartY();
        } else {
            startX = dragStartX - getEndX();
            startY = dragStartY - getEndY();
        }
        var args = new ConnectorMovePointCommandArgs(
                isStart,
                startX,
                startY,
                event.getSceneX(),
                event.getSceneY(),
                element.getElementId());
        var command = connectorMovePointCommandFactory.createTracked(args);
        command.execute();
        event.consume();
    }

    /**
     * Sets the pathing strategy of this connector.
     * @param strategy the new PathingStrategy to use when calculating paths
     */
    public void setPathingStrategy(PathingStrategy strategy) {
        this.pathingStrategy.set(strategy);
        recalculatePath();
    }

    public PathingStrategy getPathingStrategy() {
        return this.pathingStrategy.get();
    }

    public double getStartX() {
        return startX.get();
    }

    public void setStartX(double x) {
        startX.set(x);
    }

    public double getStartY() {
        return startY.get();
    }

    public void setStartY(double y) {
        startY.set(y);
    }

    public double getEndX() {
        return endX.get();
    }

    public void setEndX(double x) {
        endX.set(x);
    }

    public double getEndY() {
        return endY.get();
    }

    public void setEndY(double y) {
        endY.set(y);
    }

    public boolean getIsStartHorizontal() {
        return isStartHorizontal.get();
    }

    public void setIsStartHorizontal(boolean isHorizontal) {
        isStartHorizontal.set(isHorizontal);
    }

    public boolean getIsEndHorizontal() {
        return isEndHorizontal.get();
    }

    public void setIsEndHorizontal(boolean isHorizontal) {
        isEndHorizontal.set(isHorizontal);
    }

    public void setSnapStart(boolean isSnap) {
        isStartSnapping = isSnap;
    }

    public boolean getSnapStart() {
        return this.isStartSnapping;
    }

    public void setSnapEnd(boolean isSnap) {
        isEndSnapping = isSnap;
    }

    public boolean getSnapEnd() {
        return this.isEndSnapping;
    }

    protected double adjustX(double x) {
        return x - element.getLayoutX();
    }

    protected double adjustY(double y) {
        return y - element.getLayoutY();
    }
}
