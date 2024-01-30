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

public class ConnectorMovePointCommand implements Command<ConnectorMovePointCommandArgs> {

    private final ConnectorMovePointCommandArgs args;
    private final ElementIdManager elementIdManager;

    public ConnectorMovePointCommand(ConnectorMovePointCommandArgs args, ElementIdManager elementIdManager) {
        this.args = args;
        this.elementIdManager = elementIdManager;
    }

    /**
     * Executes the command
     */
    @Override
    public void execute() {
        var element = elementIdManager.getElementById(args.elementId());
        if (element == null) return;
        ConnectorElementController controller = (ConnectorElementController) element.getProperties().get("controller");
        if (controller == null) throw new IllegalArgumentException();
        if (args.isStart()) {
            controller.setStartX(controller.getStartX() + args.deltaX());
            controller.setStartY(controller.getStartY() + args.deltaY());
        } else {
            controller.setEndX(controller.getEndX() + args.deltaX());
            controller.setEndY(controller.getEndY() + args.deltaY());
        }
    }

    @Override
    public ConnectorMovePointCommandArgs getArgs() {
        return null;
    }
}
