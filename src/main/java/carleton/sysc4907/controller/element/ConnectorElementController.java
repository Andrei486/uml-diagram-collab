package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.ConnectorMovePointCommandFactory;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.controller.element.pathing.PathingStrategy;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.view.DiagramElement;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.LinkedList;
import java.util.List;

public class ConnectorElementController extends DiagramElementController {

    private final double AUTO_DIRECTION_THRESHOLD = 4.0;
    private final ConnectorHandleCreator connectorHandleCreator;
    private final ConnectorMovePointCommandFactory connectorMovePointCommandFactory;
    private final List<Node> handles;

    // All coordinates relative to the parent element (editing area)!
    private final DoubleProperty startX = new SimpleDoubleProperty();
    private final DoubleProperty startY = new SimpleDoubleProperty();
    private final DoubleProperty endX = new SimpleDoubleProperty();
    private final DoubleProperty endY = new SimpleDoubleProperty();

    private final BooleanProperty isStartHorizontal = new SimpleBooleanProperty();

    private boolean isStartSnapping = false;
    private final BooleanProperty isEndHorizontal = new SimpleBooleanProperty();

    private boolean isEndSnapping = false;

    @FXML
    private Path connectorPath;

    private PathingStrategy pathingStrategy;

    private boolean movePointDragging = false;
    private double dragStartX;
    private double dragStartY;

    private boolean repositioning = false;

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
            ConnectorMovePointCommandFactory connectorMovePointCommandFactory,
            PathingStrategy pathingStrategy) {
        super(previewCreator, moveCommandFactory, diagramModel);
        this.connectorHandleCreator = connectorHandleCreator;
        this.connectorMovePointCommandFactory = connectorMovePointCommandFactory;
        this.pathingStrategy = pathingStrategy;
        this.handles = new LinkedList<>();
        isStartHorizontal.set(true);
        isEndHorizontal.set(false);
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

    private void toggleShowPointDragHandles(boolean showHandles) {
        if (showHandles) {
            var handle = connectorHandleCreator.createMovePointHandle(element, startX, startY);
            handle.setOnDragDetected(this::handleDragDetectedStartMovePoint);
            handle.setOnMouseReleased(event -> handleMouseReleasedResize(event, true));
            handles.add(handle);
            handle = connectorHandleCreator.createMovePointHandle(element, endX, endY);
            handle.setOnDragDetected(this::handleDragDetectedStartMovePoint);
            handle.setOnMouseReleased(event -> handleMouseReleasedResize(event, false));
            handles.add(handle);
        } else {
            for (Node handle : handles) {
                connectorHandleCreator.deleteMovePointHandle(element, handle);
            }
            handles.clear();
        }
    }

    private void reposition() {
        repositioning = true;
        double leftmostX = Math.min(getStartX(), getEndX());
        double topmostY = Math.min(getStartY(), getEndY());
        element.setLayoutX(leftmostX);
        element.setLayoutY(topmostY);
        repositioning = false;
    }

    private void recalculatePath() {
        pathingStrategy.makePath(
                connectorPath,
                adjustX(getStartX()), adjustY(getStartY()), isStartHorizontal.get(),
                adjustX(getEndX()), adjustY(getEndY()), isEndHorizontal.get()
        );
    }

    private void updateDirections() {
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

    private void handleDragDetectedStartMovePoint(MouseEvent event) {
        movePointDragging = true;
        dragStartX = event.getSceneX();
        dragStartY = event.getSceneY();
        event.consume();
    }

    private void handleMouseReleasedResize(MouseEvent event, boolean isStart) {
        if (!movePointDragging) {
            return;
        }
        movePointDragging = false;
        var args = new ConnectorMovePointCommandArgs(
                isStart,
                event.getSceneX() - dragStartX,
                event.getSceneY() - dragStartY,
                element.getElementId());
        var command = connectorMovePointCommandFactory.createTracked(args);
        command.execute();
        event.consume();
    }

    public void setPathingStrategy(PathingStrategy strategy) {
        this.pathingStrategy = strategy;
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

    public void setSnapStart(boolean isSnap) {
        isStartSnapping = isSnap;
    }

    public void setSnapEnd(boolean isSnap) {
        isEndSnapping = isSnap;
    }

    private double adjustX(double x) {
        return x - element.getLayoutX();
    }

    private double adjustY(double y) {
        return y - element.getLayoutY();
    }
}
