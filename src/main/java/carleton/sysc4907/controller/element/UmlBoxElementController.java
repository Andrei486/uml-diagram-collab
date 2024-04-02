package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 * An abstract class for uml boxes with a title and one box for enumerators, methods, etc.
 * Used for interfaces and enumerations.
 */
public abstract class UmlBoxElementController extends ResizableElementController {

    @FXML
    public Rectangle titleRectangle;
    @FXML
    public Rectangle entriesRectangle;
    @FXML
    public Group entriesLabel;
    @FXML
    public Group titleLabel;
    @FXML
    protected Rectangle background;
    @FXML
    protected StackPane bgStackPane;
    @FXML
    protected EditableLabelController titleLabelController;
    @FXML
    protected EditableLabelController entriesLabelController;

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
    public UmlBoxElementController(MovePreviewCreator previewCreator, MoveCommandFactory moveCommandFactory, DiagramModel diagramModel, ResizeHandleCreator resizeHandleCreator, ResizePreviewCreator resizePreviewCreator, ResizeCommandFactory resizeCommandFactory) {
        super(previewCreator, moveCommandFactory, diagramModel, resizeHandleCreator, resizePreviewCreator, resizeCommandFactory);
    }

    @FXML
    public void initialize() {
        super.initialize();
        Insets textMargins = new Insets(3,3,3,3);
        background.widthProperty().bind(element.maxWidthProperty());
        background.heightProperty().bind(element.maxHeightProperty());

        bgStackPane.maxWidthProperty().bind(element.maxWidthProperty());
        bgStackPane.maxHeightProperty().bind(element.maxHeightProperty());

        //Set properties for title section
        titleRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.4));
        titleRectangle.widthProperty().bind(element.maxWidthProperty());
        titleLabelController.getHeightProperty().bind(titleRectangle.heightProperty());
        titleLabelController.getWidthProperty().bind(element.maxWidthProperty());
        titleLabelController.getLabel().setPadding(textMargins);

        //Set properties for fields section
        entriesRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.6));
        entriesRectangle.widthProperty().bind(element.maxWidthProperty());
        entriesLabelController.getHeightProperty().bind(entriesRectangle.heightProperty());
        entriesLabelController.getWidthProperty().bind(element.maxWidthProperty());
        entriesLabelController.getLabel().setPadding(textMargins);
        entriesLabelController.getLabel().setAlignment(Pos.TOP_LEFT);
        entriesLabelController.getLabel().setTextAlignment(TextAlignment.LEFT);
    }


    protected abstract void setDefaultText();

    public StringProperty getTitleTextProperty() {
        return titleLabelController.getTextProperty();
    }

    public String getTitleText() {
        return titleLabelController.getText();
    }

    public void setTitleText(String text) {
        titleLabelController.setText(text);
    }

    public StringProperty getFieldsTextProperty() {
        return entriesLabelController.getTextProperty();
    }

    public String getFieldsText() {
        return entriesLabelController.getText();
    }

    public void setFieldsText(String text) {
        entriesLabelController.setText(text);
    }

    public String getEntriesText() {
        return entriesLabelController.getText();
    }
}
