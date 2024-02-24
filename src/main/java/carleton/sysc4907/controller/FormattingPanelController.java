package carleton.sysc4907.controller;

import carleton.sysc4907.command.ChangeTextStyleCommand;
import carleton.sysc4907.command.ChangeTextStyleCommandFactory;
import carleton.sysc4907.command.Command;
import carleton.sysc4907.command.TextStyleProperty;
import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.model.DiagramModel;
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

    private final DiagramModel diagramModel;

    /**
     * Constructs a FormattingPanelController.
     * @param textFormattingModel the TextFormattingModel containing text formatting information
     */
    public FormattingPanelController(TextFormattingModel textFormattingModel, ChangeTextStyleCommandFactory changeTextStyleCommandFactory, DiagramModel diagramModel) {
        this.textFormattingModel = textFormattingModel;
        this.changeTextStyleCommandFactory = changeTextStyleCommandFactory;
        this.diagramModel = diagramModel;
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        // deselect and disable all formatting options
        fontName.setItems(textFormattingModel.getFontFamilyNames());
        //Toggle buttons do not trigger any events, so we need to use a change listener
        ChangeListener<Boolean> boldButtonToggledListener = ((observableValue, aBoolean, t1) -> onBoldButtonToggled(t1));
        boldButton.selectedProperty().addListener(boldButtonToggledListener);
    }

    private void onBoldButtonToggled(Boolean t1) {
        System.out.println("The bold button has been toggled! Val = " + t1);
        ObservableList<DiagramElement> selectedElems = diagramModel.getSelectedElements();
        for(int i = 0 ; i < selectedElems.toArray().length ; i++) {
            Command<ChangeTextStyleCommandArgs> command = changeTextStyleCommandFactory.createTracked(new ChangeTextStyleCommandArgs(TextStyleProperty.BOLD, t1.toString(), selectedElems.get(i).getElementId()));
            command.execute();
        }
    }



}