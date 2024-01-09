package carleton.sysc4907.command;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.layout.Pane;

/**
 * Command for the add element operation
 */
public class AddCommand implements Command<AddCommandArgs> {

    private final AddCommandArgs args;
    private final DiagramModel diagramModel;
    private final ElementCreator elementCreator;

    public AddCommand(AddCommandArgs args, DiagramModel diagramModel, ElementCreator elementCreator) {
        this.args = args;
        this.diagramModel = diagramModel;
        this.elementCreator = elementCreator;
    }

    @Override
    public void execute() {
        Pane editingArea = EditingAreaProvider.getEditingArea();
        DiagramElement element = elementCreator.create(args.elementType(), args.elementId(), true);
        if (element == null) return; // If the type of element to create is not recognized
        editingArea.getChildren().add(element);
        diagramModel.getElements().add(element);
    }
}
