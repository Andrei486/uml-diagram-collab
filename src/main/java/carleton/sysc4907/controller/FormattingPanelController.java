package carleton.sysc4907.controller;

import carleton.sysc4907.command.ChangeTextStyleCommand;
import carleton.sysc4907.command.ChangeTextStyleCommandFactory;
import carleton.sysc4907.command.Command;
import carleton.sysc4907.command.TextStyleProperty;
import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.controller.element.EditableLabelController;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.EditableLabelTracker;
import carleton.sysc4907.model.TextFormattingModel;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.EventListener;
import java.util.Objects;

/**
 * Controller for the formatting panel, the panel on the diagram editor page that provides text formatting options.
 */
public class FormattingPanelController {

    @FXML
    private Spinner<Integer> fontSize;
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

    //use this for seeing if selected element is bold etc?
    private final DiagramModel diagramModel;

    private final EditableLabelTracker editableLabelTracker;

    private final ElementIdManager elementIdManager;

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
        // deselect and disable all formatting options
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

    private void onSelectedElementChange() {
        if(diagramModel.getSelectedElements().isEmpty()) {
            editableLabelTracker.setIdLastEditedLabel(null);
        }
    }

    private void onSelectedLabelChanged(Number idNum) {
        if (idNum == null) {
            boldButton.setSelected(false);
            italicsButton.setSelected(false);
            underlineButton.setSelected(false);
            return;
        }
        Long id = (Long) idNum;
        Node labelNode = elementIdManager.getElementById(id);
        if (labelNode == null) {
            return;
        }
        Label label = (Label) labelNode;
        var styleClass = label.getStyleClass();
        //these *will* trigger the change listeners and make a new command
        boldButton.setSelected(styleClass.contains("bolded"));
        italicsButton.setSelected(styleClass.contains("italicized"));
        underlineButton.setSelected(styleClass.contains("underlined"));
    }

    private void onFontSizeChanged(Integer val) {
        if (editableLabelTracker.getIdLastEditedLabel() == null) {
            return;
        }
        System.out.println("The font size has been changed! Val = " + val);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.SIZE, val, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    private void onUnderlineButtonToggled(Boolean t1) {
        if (editableLabelTracker.getIdLastEditedLabel() == null) {
            return;
        }
        //should do a check here to see if the value is the same 
        System.out.println("The underline button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.UNDERLINE, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    private void onItalicsButtonToggled(Boolean t1) {
        if (editableLabelTracker.getIdLastEditedLabel() == null) {
            return;
        }
        System.out.println("The italics button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.ITALICS, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    private void onBoldButtonToggled(Boolean t1) {
        if (editableLabelTracker.getIdLastEditedLabel() == null) {
            return;
        }
        System.out.println("The bold button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.BOLD, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    private void onFontFamilyChanged(String newFont) {
        if (editableLabelTracker.getIdLastEditedLabel() == null) {
            return;
        }
        System.out.println("The font family has been changed! Val = " + newFont);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.FONT_FAMILY, newFont, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }



}