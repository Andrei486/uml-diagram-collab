package carleton.sysc4907.command;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.Command;
import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Command for moving the start or end point of a connector.
 */
public class ConnectorMovePointCommand implements Command<ConnectorMovePointCommandArgs> {

    private final ConnectorMovePointCommandArgs args;
    private final ElementIdManager elementIdManager;

    public ConnectorMovePointCommand(ConnectorMovePointCommandArgs args, ElementIdManager elementIdManager) {
        this.args = args;
        this.elementIdManager = elementIdManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        var element = elementIdManager.getElementById(args.elementId());
        if (element == null) return;
        // Find connector controller from properties: this must be specified in FXML, see Controller.fxml
        ConnectorElementController controller = (ConnectorElementController) element.getProperties().get("controller");
        if (controller == null) throw new IllegalArgumentException();
        if (args.isStart()) {
            controller.unsnapFromHandle(true);
            controller.setStartX(args.endX() - args.startX());
            controller.setStartY(args.endY() - args.startY());
        } else {
            controller.unsnapFromHandle(false);
            controller.setEndX(args.endX() - args.startX());
            controller.setEndY(args.endY() - args.startY());
        }
    }

    @Override
    public ConnectorMovePointCommandArgs getArgs() {
        return args;
    }
}
