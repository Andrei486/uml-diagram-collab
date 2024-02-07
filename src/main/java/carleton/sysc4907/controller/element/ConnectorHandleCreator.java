package carleton.sysc4907.controller.element;

import carleton.sysc4907.view.DiagramElement;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class ConnectorHandleCreator {

    private final double HANDLE_SIZE = 10;

    public ConnectorHandleCreator() {

    }

    public Node createMovePointHandle(DiagramElement element, DoubleProperty x, DoubleProperty y) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ConnectorHandleCreator.class.getResource("/carleton/sysc4907/view/element/ResizeHandle.fxml"));
        Rectangle resizeHandle = null;
        try {
            resizeHandle = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        element.getChildren().add(resizeHandle);
        resizeHandle.layoutXProperty().bind(x.subtract(HANDLE_SIZE / 2).subtract(element.layoutXProperty()));
        resizeHandle.layoutYProperty().bind(y.subtract(HANDLE_SIZE / 2).subtract(element.layoutYProperty()));
        return resizeHandle;
    }

    public void deleteMovePointHandle(DiagramElement element, Node handle) {
        if (handle == null) {
            return;
        }
        element.getChildren().remove(handle);
    }
}
