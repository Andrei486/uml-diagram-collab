package carleton.sysc4907.command;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import javafx.scene.layout.Pane;

public class RemoveCommand implements Command<RemoveCommandArgs> {

    private final RemoveCommandArgs args;
    private final DiagramModel diagramModel;

    public RemoveCommand(RemoveCommandArgs args, DiagramModel diagramModel) {
        this.args = args;
        this.diagramModel = diagramModel;

    }

    @Override
    public void execute() {
        Pane editingArea = EditingAreaProvider.getEditingArea();
        editingArea.getChildren().removeAll(args.elements());
        diagramModel.getElements().removeAll(args.elements());
        diagramModel.getSelectedElements().clear();
    }

}
