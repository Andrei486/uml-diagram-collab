package carleton.sysc4907.command.args;

import javafx.scene.Node;

public record ResizeCommandArgs(boolean isTopAnchor,
                                boolean isRightAnchor,
                                double dragStartX,
                                double dragStartY,
                                double dragEndX,
                                double dragEndY,
                                Node element) {

}