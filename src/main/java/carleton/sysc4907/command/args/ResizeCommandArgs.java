package carleton.sysc4907.command.args;

import javafx.scene.layout.Pane;

import java.io.Serializable;

public record ResizeCommandArgs(boolean isTopAnchor,
                                boolean isRightAnchor,
                                double dragStartX,
                                double dragStartY,
                                double dragEndX,
                                double dragEndY,
                                long elementId) implements Serializable {

}