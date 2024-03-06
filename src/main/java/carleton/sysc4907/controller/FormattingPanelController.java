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
import carleton.sysc4907.view.DiagramElement;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.EventListener;

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

    /**
     * Constructs a FormattingPanelController.
     * @param textFormattingModel the TextFormattingModel containing text formatting information
     */
    public FormattingPanelController(TextFormattingModel textFormattingModel, ChangeTextStyleCommandFactory changeTextStyleCommandFactory, DiagramModel diagramModel, EditableLabelTracker editableLabelTracker) {
        this.textFormattingModel = textFormattingModel;
        this.changeTextStyleCommandFactory = changeTextStyleCommandFactory;
        this.diagramModel = diagramModel;
        this.editableLabelTracker = editableLabelTracker;
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        // deselect and disable all formatting options
        fontName.setItems(textFormattingModel.getFontFamilyNames());

        //Toggle buttons do not trigger any events, so we need to use a change listener
        ChangeListener<Boolean> boldButtonToggledListener = ((observableValue, aBoolean, val) -> onBoldButtonToggled(val));
        boldButton.selectedProperty().addListener(boldButtonToggledListener);

        ChangeListener<Boolean> italicsButtonToggledListener = ((observableValue, aBoolean, val) -> onItalicsButtonToggled(val));
        italicsButton.selectedProperty().addListener(italicsButtonToggledListener);

        ChangeListener<Boolean> underlineButtonToggledListener = ((observableValue, aBoolean, val) -> onUnderlineButtonToggled(val));
        underlineButton.selectedProperty().addListener(underlineButtonToggledListener);

        ChangeListener<String> fontFamilySelectionChangedListener = (((observableValue, aString, val) -> onFontFamilyChanged(val)));
        fontName.valueProperty().addListener(fontFamilySelectionChangedListener);

    }

    private void onUnderlineButtonToggled(Boolean t1) {
        System.out.println("The underline button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.UNDERLINE, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    private void onItalicsButtonToggled(Boolean t1) {
        System.out.println("The italics button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.ITALICS, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    private void onBoldButtonToggled(Boolean t1) {
        System.out.println("The bold button has been toggled! Val = " + t1);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.BOLD, t1, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }

    private void onFontFamilyChanged(String newFont) {
        System.out.println("The font family has been changed! Val = " + newFont);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.FONT_FAMILY, newFont, editableLabelTracker.getIdLastEditedLabel());
        Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(args);
        command.execute();
    }



}