package carleton.sysc4907.command;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class AddCommand implements Command<AddCommandArgs> {

    private final AddCommandArgs args;
    private final DiagramModel diagramModel;
    private final DependencyInjector elementInjector;

    public AddCommand(AddCommandArgs args, DiagramModel diagramModel, DependencyInjector elementInjector) {
        this.args = args;
        this.diagramModel = diagramModel;
        this.elementInjector = elementInjector;
    }

    @Override
    public void execute() {
        Parent obj;
        Pane editingArea = EditingAreaProvider.getEditingArea();
        try {
            obj = elementInjector.load(args.fxmlPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        editingArea.getChildren().add(obj);
        DiagramElement element = (DiagramElement) obj;
        diagramModel.getElements().add(element);
        diagramModel.getSelectedElements().clear();
        diagramModel.getSelectedElements().add(element);
    }
}
