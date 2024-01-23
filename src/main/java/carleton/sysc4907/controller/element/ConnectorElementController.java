package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.model.DiagramModel;

public class ConnectorElementController extends DiagramElementController {
    /**
     * Constructs a new DiagramElementController.
     *
     * @param previewCreator     a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory a MoveCommandFactory, used to create move commands
     * @param diagramModel       the DiagramModel for the current diagram
     */
    public ConnectorElementController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel) {
        super(previewCreator, moveCommandFactory, diagramModel);
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
