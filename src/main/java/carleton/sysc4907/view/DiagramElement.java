package carleton.sysc4907.view;

import javafx.scene.layout.Pane;

/**
 * Base class for all diagram elements to be rendered in the diagram.
 */
public class DiagramElement extends Pane {

    public long getElementId() {
        return (Long) getUserData();
    }
}