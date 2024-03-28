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
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class UmlEnumElementController extends ResizableElementController {

    @FXML
    public Rectangle titleRectangle;
    @FXML
    public Rectangle enumeratorsRectangle;
    @FXML
    public Group enumeratorsLabel;
    @FXML
    public Group titleLabel;
    @FXML
    private Rectangle background;
    @FXML
    private StackPane bgStackPane;
    @FXML
    private EditableLabelController titleLabelController;
    @FXML
    private EditableLabelController enumeratorsLabelController;

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
        Insets textMargins = new Insets(3,3,3,3);
        background.widthProperty().bind(element.maxWidthProperty());
        background.heightProperty().bind(element.maxHeightProperty());

        bgStackPane.maxWidthProperty().bind(element.maxWidthProperty());
        bgStackPane.maxHeightProperty().bind(element.maxHeightProperty());

        //Set properties for title section
        titleRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.3));
        titleRectangle.widthProperty().bind(element.maxWidthProperty());
        titleLabelController.getHeightProperty().bind(titleRectangle.heightProperty());
        titleLabelController.getWidthProperty().bind(element.maxWidthProperty());
        titleLabelController.getLabel().setPadding(textMargins);

        //Set properties for fields section
        enumeratorsRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.7));
        enumeratorsRectangle.widthProperty().bind(element.maxWidthProperty());
        enumeratorsLabelController.getHeightProperty().bind(enumeratorsRectangle.heightProperty());
        enumeratorsLabelController.getWidthProperty().bind(element.maxWidthProperty());
        enumeratorsLabelController.getLabel().setPadding(textMargins);
        enumeratorsLabelController.getLabel().setAlignment(Pos.TOP_LEFT);
        enumeratorsLabelController.getLabel().setTextAlignment(TextAlignment.LEFT);

        //Set default text
        setTitleText("<<enumeration>>");
        setFieldsText("Enumerators");

    }

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
        return enumeratorsLabelController.getTextProperty();
    }

    public String getFieldsText() {
        return enumeratorsLabelController.getText();
    }

    public void setFieldsText(String text) {
        enumeratorsLabelController.setText(text);
    }
}
