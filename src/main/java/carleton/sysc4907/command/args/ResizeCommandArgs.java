package carleton.sysc4907.command.args;

import javafx.scene.layout.Pane;

public record ResizeCommandArgs(boolean isTopAnchor,
                                boolean isRightAnchor,
                                double dragStartX,
                                double dragStartY,
                                double dragEndX,
                                double dragEndY,
                                Pane element) {

}