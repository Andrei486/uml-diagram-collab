package carleton.sysc4907.controller;

import carleton.sysc4907.model.Diagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {

    private ScrollPane scrollPane;
    private final Diagram diagram;

    public MenuController() {
        diagram = Diagram.getSingleInstance();
    }

    public void setExportScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }
    @FXML
    public void exportImage(ActionEvent actionEvent) {
        // the content of scrollPane is saved as a JPEG file.
        WritableImage img = scrollPane.snapshot(new SnapshotParameters(), null);
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        // File fileToSave = chooser.getSelectedFile();//Remove this line.
        BufferedImage img2 = SwingFXUtils.fromFXImage(img, null);
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToSave = chooser.getSelectedFile();
                ImageIO.write(img2, "png", fileToSave);
            } catch (IOException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
