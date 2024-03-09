package carleton.sysc4907.controller;

import carleton.sysc4907.command.ChangeTextStyleCommandFactory;
import carleton.sysc4907.command.Command;
import carleton.sysc4907.command.TextStyleProperty;
import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.EditableLabelTracker;
import carleton.sysc4907.model.TextFormattingModel;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.Objects;

/**
 * Controller for the formatting panel, the panel on the diagram editor page that provides text formatting options.
 */
public class FormattingPanelController {

    @FXML
    private Spinner<Double> fontSize;
    @FXML
    private ComboBox<String> fontName;
    @FXML
    private TitledPane titledPane;
    @FXML
    private ToggleButton underlineButton;
    @FXML
    private ToggleButton italicsButton;
    @FXML
    private ToggleButton boldButton;

    private final TextFormattingModel textFormattingModel;

    private final ChangeTextStyleCommandFactory changeTextStyleCommandFactory;

    private final DiagramModel diagramModel;

    private final EditableLabelTracker editableLabelTracker;

    private final ElementIdManager elementIdManager;

    private Long selectedLabelId = null;

    /**
     * Constructs a FormattingPanelController.
     * @param textFormattingModel the TextFormattingModel containing text formatting information
     */
    public FormattingPanelController(TextFormattingModel textFormattingModel,
                                     ChangeTextStyleCommandFactory changeTextStyleCommandFactory,
                                     DiagramModel diagramModel,
                                     EditableLabelTracker editableLabelTracker,
                                     ElementIdManager elementIdManager) {
        this.textFormattingModel = textFormattingModel;
        this.changeTextStyleCommandFactory = changeTextStyleCommandFactory;
        this.diagramModel = diagramModel;
        this.editableLabelTracker = editableLabelTracker;
        this.elementIdManager = elementIdManager;
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        fontName.setItems(textFormattingModel.getFontFamilyNames());

        //Toggle buttons do not trigger any events, so we need to use a change listener
        boldButton.selectedProperty().addListener((observableValue, number, t1) -> onBoldButtonToggled(t1));
        italicsButton.selectedProperty().addListener((observableValue, number, t1) -> onItalicsButtonToggled(t1));
        underlineButton.selectedProperty().addListener((observableValue, number, t1) -> onUnderlineButtonToggled(t1));
        fontName.valueProperty().addListener((observableValue, number, t1) -> onFontFamilyChanged(t1));
        fontSize.valueProperty().addListener((observableValue, number, t1) -> onFontSizeChanged(t1));
        editableLabelTracker.idLastEditedLabelProperty().addListener((observableValue, number, t1) -> onSelectedLabelChanged(t1));
        diagramModel.getSelectedElements().addListener((ListChangeListener<DiagramElement>) change -> onSelectedElementChange());
    }

    /**
     * Handler for when the selected element changes.
     */
    private void onSelectedElementChange() {
        if(diagramModel.getSelectedElements().isEmpty()) {
            editableLabelTracker.setIdLastEditedLabel(null);
        }
    }

    /**
     * Handler for when the selected label changes.
     * @param idNum the ID of the selected label.
     */
    private void onSelectedLabelChanged(Number idNum) {
        //if no label is selected, disable all buttons and set them to the default state.
        if (idNum == null) {
            boldButton.setSelected(false);
            italicsButton.setSelected(false);
            underlineButton.setSelected(false);
            fontName.valueProperty().setValue("");
            selectedLabelId = null;

            fontSize.setDisable(true);
            fontName.setDisable(true);
            boldButton.setDisable(true);
            italicsButton.setDisable(true);
            underlineButton.setDisable(true);
            return;
        }

        Long id = (Long) idNum;
        Node labelNode = elementIdManager.getElementById(id);
        if (labelNode == null) {
            selectedLabelId = null; // could also set to id but both are no longer valid values for new selections
            return;
        }

        //if the ID is not for a label, return without enabling the buttons
        if (!(labelNode instanceof Label label)) {
            return;
        }
        var styleClass = label.getStyleClass();

        //enable buttons
        fontSize.setDisable(false);
        fontName.setDisable(false);
        boldButton.setDisable(false);
        italicsButton.setDisable(false);
        underlineButton.setDisable(false);

        //set buttons to the style of the currently selected label.
        boldButton.setSelected(styleClass.contains("bolded"));
        italicsButton.setSelected(styleClass.contains("italicized"));
        underlineButton.setSelected(styleClass.contains("underlined"));
        fontName.valueProperty().setValue(label.getFont().getFamily());
        fontSize.getValueFactory().setValue(label.getFont().getSize());
        selectedLabelId = id;
    }

    /**
     * Handler for changing the font size of some text.
     * @param val the new font size.
     */
    private void onFontSizeChanged(Double val) {
        if (editableLabelTracker.getIdLastEditedLabel() == null || !Objects.equals(editableLabelTracker.getIdLastEditedLabel(), selectedLabelId)) {
            return;
        }
        System.out.println("The font size has been changed! Val = " + val);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.SIZE, val, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    /**
     * Handler for changing the underline of some text.
     * @param t1 true if the text should be underlined, false otherwise.
     */
    private void onUnderlineButtonToggled(Boolean t1) {
        if (editableLabelTracker.getIdLastEditedLabel() == null || !Objects.equals(editableLabelTracker.getIdLastEditedLabel(), selectedLabelId)) {
            return;
        }
        System.out.println("The underline button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.UNDERLINE, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    /**
     * Handler for changing if the text is italic.
     * @param t1 true if the text should be italic, false otherwise.
     */
    private void onItalicsButtonToggled(Boolean t1) {
        if (editableLabelTracker.getIdLastEditedLabel() == null || !Objects.equals(editableLabelTracker.getIdLastEditedLabel(), selectedLabelId)) {
            return;
        }
        System.out.println("The italics button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.ITALICS, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    /**
     * Handler for changing if the text is bold.
     * @param t1 true if the text should be bolded, false otherwise.
     */
    private void onBoldButtonToggled(Boolean t1) {
        if (editableLabelTracker.getIdLastEditedLabel() == null || !Objects.equals(editableLabelTracker.getIdLastEditedLabel(), selectedLabelId)) {
            return;
        }
        System.out.println("The bold button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.BOLD, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    /**
     * Handler for changing the font of some text.
     * @param newFont the new font to apply.
     */
    private void onFontFamilyChanged(String newFont) {
        if (editableLabelTracker.getIdLastEditedLabel() == null || !Objects.equals(editableLabelTracker.getIdLastEditedLabel(), selectedLabelId)) {
            return;
        }
        System.out.println("The font family has been changed! Val = " + newFont);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.FONT_FAMILY, newFont, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }
}