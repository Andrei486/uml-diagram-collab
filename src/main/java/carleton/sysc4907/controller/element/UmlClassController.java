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
    private Rectangle background;
    @FXML
    private StackPane bgStackPane;

    //Label controllers. Note these are not manually set
    @FXML
    private EditableLabelController titleLabelController;
    @FXML
    private EditableLabelController methodsLabelController;
    @FXML
    private EditableLabelController fieldsLabelController;



    /**
     * Constructs a new UmlClassController.
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
     * Initializes class box, setting default text.
     */
    @FXML
    public void initialize() {
        super.initialize();
        Insets textMargins = new Insets(3,3,3,3);
        background.widthProperty().bind(element.maxWidthProperty());
        background.heightProperty().bind(element.maxHeightProperty());

        bgStackPane.maxWidthProperty().bind(element.maxWidthProperty());
        bgStackPane.maxHeightProperty().bind(element.maxHeightProperty());

        //Set properties for title section
        titleRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.2));
        titleRectangle.widthProperty().bind(element.maxWidthProperty());
        titleLabelController.getHeightProperty().bind(titleRectangle.heightProperty());
        titleLabelController.getWidthProperty().bind(element.maxWidthProperty());
        titleLabelController.getLabel().setPadding(textMargins);

        //Set properties for fields section
        fieldsRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.4));
        fieldsRectangle.widthProperty().bind(element.maxWidthProperty());
        fieldsLabelController.getHeightProperty().bind(fieldsRectangle.heightProperty());
        fieldsLabelController.getWidthProperty().bind(element.maxWidthProperty());
        fieldsLabelController.getLabel().setAlignment(Pos.TOP_LEFT);
        fieldsLabelController.getLabel().setTextAlignment(TextAlignment.LEFT);
        fieldsLabelController.getLabel().setPadding(textMargins);

        //Set properties for methods section
        methodsRectangle.heightProperty().bind(element.maxHeightProperty().multiply(0.4));
        methodsRectangle.widthProperty().bind(element.maxWidthProperty());
        methodsLabelController.getHeightProperty().bind(methodsRectangle.heightProperty());
        methodsLabelController.getWidthProperty().bind(element.maxWidthProperty());
        methodsLabelController.getLabel().setAlignment(Pos.TOP_LEFT);
        methodsLabelController.getLabel().setTextAlignment(TextAlignment.LEFT);
        methodsLabelController.getLabel().setPadding(textMargins);

        //Set default text
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


