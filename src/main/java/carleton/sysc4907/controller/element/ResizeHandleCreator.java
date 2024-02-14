package carleton.sysc4907.controller.element;

import carleton.sysc4907.view.DiagramElement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class ResizeHandleCreator {

    private final double HANDLE_SIZE = 10;

    public ResizeHandleCreator() {

    }

    public Node createResizeHandle(DiagramElement element, boolean isTop, boolean isRight) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResizeHandleCreator.class.getResource("/carleton/sysc4907/view/element/ResizeHandle.fxml"));
        Rectangle resizeHandle = null;
        try {
            resizeHandle = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        element.getChildren().add(resizeHandle);
        if (isRight) {
            resizeHandle.layoutXProperty().bind(element.maxWidthProperty().subtract(HANDLE_SIZE));
        } else {
            resizeHandle.layoutXProperty().set(0);
        }
        if (!isTop) {
            resizeHandle.layoutYProperty().bind(element.maxHeightProperty().subtract(HANDLE_SIZE));
        } else {
            resizeHandle.layoutYProperty().set(0);
        }
        return resizeHandle;
    }

    public void deleteResizeHandle(DiagramElement element, Node handle) {
        if (handle == null) {
            return;
        }
        element.getChildren().remove(handle);
    }
}
