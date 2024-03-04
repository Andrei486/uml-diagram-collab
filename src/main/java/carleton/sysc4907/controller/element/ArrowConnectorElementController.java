package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.ConnectorMovePointCommandFactory;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.controller.element.arrows.Arrowhead;
import carleton.sysc4907.controller.element.arrows.ArrowheadFactory;
import carleton.sysc4907.controller.element.arrows.ArrowheadType;
import carleton.sysc4907.controller.element.pathing.PathingStrategy;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.shape.Path;

public class ArrowConnectorElementController extends ConnectorElementController {

    private final ArrowheadFactory arrowheadFactory;

    private Arrowhead arrowhead;

    @FXML
    private Path arrowheadPath;

    public ArrowConnectorElementController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel,
            ConnectorHandleCreator connectorHandleCreator,
            ConnectorMovePointCommandFactory connectorMovePointCommandFactory,
            PathingStrategy pathingStrategy,
            ArrowheadFactory arrowheadFactory) {
        super(
                previewCreator,
                moveCommandFactory,
                diagramModel,
                connectorHandleCreator,
                connectorMovePointCommandFactory,
                pathingStrategy
        );
        this.arrowheadFactory = arrowheadFactory;

        InvalidationListener listener = observable -> {
            arrowhead.makeArrowheadPath(
                    arrowheadPath,
                    adjustX(getStartX()), adjustY(getStartY()),
                    adjustX(getEndX()), adjustY(getEndY()),
                    isEndHorizontal.get(), true
            );
        };

        startX.addListener(listener);
        startY.addListener(listener);
        endX.addListener(listener);
        endY.addListener(listener);
        isEndHorizontal.addListener(listener);
        this.pathingStrategy.addListener(listener);
    }

    public void setArrowheadType(ArrowheadType type) {
        arrowhead = arrowheadFactory.createArrowhead(type);
        arrowhead.makeArrowheadPath(
                arrowheadPath,
                adjustX(getStartX()), adjustY(getStartY()),
                adjustX(getEndX()), adjustY(getEndY()),
                isEndHorizontal.get(), true
        );
    }

    @Override
    protected void recalculatePath() {
        super.recalculatePath();
        arrowhead.makeArrowheadPath(
                arrowheadPath,
                adjustX(getStartX()), adjustY(getStartY()),
                adjustX(getEndX()), adjustY(getEndY()),
                isEndHorizontal.get(), true
        );
    }

    @Override
    public void initialize() {
        super.initialize();
        setArrowheadType(ArrowheadType.AGGREGATION); // default arrowhead
    }
}
