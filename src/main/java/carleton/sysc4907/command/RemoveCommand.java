package carleton.sysc4907.command;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.layout.Pane;

import java.util.LinkedList;
import java.util.List;

/**
 * Command for the remove element operation. Multiple remove operations can be done with one command
 */
public class RemoveCommand implements Command<RemoveCommandArgs> {

    private final RemoveCommandArgs args;
    private final DiagramModel diagramModel;

    private final ElementIdManager elementIdManager;

    public RemoveCommand(RemoveCommandArgs args, DiagramModel diagramModel, ElementIdManager elementIdManager) {
        this.args = args;
        this.diagramModel = diagramModel;
        this.elementIdManager = elementIdManager;

    }

    @Override
    public void execute() {
        Pane editingArea = EditingAreaProvider.getEditingArea();
        List<DiagramElement> elements = new LinkedList<>();
        for (long id : args.elementIds()) {
            var element = (DiagramElement) elementIdManager.getElementById(id);
            if (element != null) {
                elements.add(element);
            }
        }
        if (elements.isEmpty()) {
            return;
        }
        editingArea.getChildren().removeAll(elements);
        diagramModel.getElements().removeAll(elements);
        diagramModel.getSelectedElements().clear();
    }

    @Override
    public RemoveCommandArgs getArgs() {
        return args;
    }

}
