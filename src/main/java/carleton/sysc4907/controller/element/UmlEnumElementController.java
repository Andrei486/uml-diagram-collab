package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;

public class UmlEnumElementController extends UmlBoxElementController{

    /**
     * Constructs a new ResizableElementController.
     *
     * @param previewCreator       a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory   a MoveCommandFactory, used to create move commands
     * @param diagramModel         the DiagramModel for the current diagram
     * @param resizeHandleCreator  creates resize handles
     * @param resizePreviewCreator creates resize previews
     * @param resizeCommandFactory creates resize commands
     */
    public UmlEnumElementController(MovePreviewCreator previewCreator, MoveCommandFactory moveCommandFactory, DiagramModel diagramModel, ResizeHandleCreator resizeHandleCreator, ResizePreviewCreator resizePreviewCreator, ResizeCommandFactory resizeCommandFactory) {
        super(previewCreator, moveCommandFactory, diagramModel, resizeHandleCreator, resizePreviewCreator, resizeCommandFactory);
    }

    @FXML
    public void initialize() {
        super.initialize();
        setDefaultText();
    }

    @Override
    protected void setDefaultText() {
        setTitleText("<<enumeration>>");
        setFieldsText("Enumerators");
    }


}
