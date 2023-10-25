package carleton.sysc4907.controller;

import carleton.sysc4907.model.Diagram;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class ImageDisplayPanelController {

    @FXML
    private ImageView image;

    private final Diagram diagram;

    public ImageDisplayPanelController() {
        diagram = Diagram.getSingleInstance();
    }
}
