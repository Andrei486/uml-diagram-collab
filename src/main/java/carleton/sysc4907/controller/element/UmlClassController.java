package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class UmlClassController extends ResizableElementController{
    @FXML
    public Rectangle titleRectangle;
    @FXML
    public Rectangle fieldsRectangle;
    @FXML
    public Group fieldsLabel;
    @FXML
    public Rectangle methodsRectangle;
    @FXML
    public Group methodsLabel;
    @FXML
    public Group titleLabel;
    @FXML
    private EditableLabelController titleLabelController;
    @FXML
    private EditableLabelController methodsLabelController;
    @FXML
    private EditableLabelController fieldsLabelController;
    @FXML
    private Rectangle background;

    @FXML
    private StackPane bgStackPane;

    /**
     * Constructs a new UmlCommentController.
     *
     * @param previewCreator     a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory a MoveCommandFactory, used to create move commands
     * @param diagramModel       the DiagramModel for the current diagram
     */
    public UmlClassController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel,
            ResizeHandleCreator resizeHandleCreator,
            ResizePreviewCreator resizePreviewCreator,
            ResizeCommandFactory resizeCommandFactory) {
        super(previewCreator, moveCommandFactory, diagramModel, resizeHandleCreator, resizePreviewCreator, resizeCommandFactory);
    }

    /**
     * Initializes the comment, setting a default text.
     */
    @FXML
    public void initialize() {
        super.initialize();
        background.widthProperty().bind(element.maxWidthProperty());
        background.heightProperty().bind(element.maxHeightProperty());

        bgStackPane.maxWidthProperty().bind(element.maxWidthProperty());
        bgStackPane.maxHeightProperty().bind(element.maxHeightProperty());

        titleRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.2));
        titleLabelController.getHeightProperty().bind(titleRectangle.heightProperty());
        titleLabelController.getWidthProperty().bind(element.maxWidthProperty());

        fieldsRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.4));
        fieldsLabelController.getHeightProperty().bind(fieldsRectangle.heightProperty());
        fieldsLabelController.getWidthProperty().bind(element.maxWidthProperty());

        methodsRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.4));
        methodsLabelController.getHeightProperty().bind(methodsRectangle.heightProperty());
        methodsLabelController.getWidthProperty().bind(element.maxWidthProperty());

        setTitleText("UML Class");
        setFieldsText("Fields");
        setMethodsText("Methods");
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

    public StringProperty getMethodsTextProperty() {
        return methodsLabelController.getTextProperty();
    }

    public String getMethodsText() {
        return methodsLabelController.getText();
    }

    public void setMethodsText(String text) {
        methodsLabelController.setText(text);
    }

    public StringProperty getFieldsTextProperty() {
        return fieldsLabelController.getTextProperty();
    }

    public String getFieldsText() {
        return fieldsLabelController.getText();
    }

    public void setFieldsText(String text) {
        fieldsLabelController.setText(text);
    }
}


