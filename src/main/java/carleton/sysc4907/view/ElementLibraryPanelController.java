package carleton.sysc4907.view;

import carleton.sysc4907.model.Diagram;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;

public class ElementLibraryPanelController {

    @FXML
    private FlowPane elementsPane;
    @FXML
    private TitledPane titledPane;
    private final Diagram diagram;

    public ElementLibraryPanelController() {
        diagram = Diagram.getSingleInstance();
    }

    @FXML
    public void initialize() {

    }
}
