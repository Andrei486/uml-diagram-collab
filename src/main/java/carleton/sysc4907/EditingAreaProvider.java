package carleton.sysc4907;

import javafx.scene.layout.Pane;

public class EditingAreaProvider {

    private static Pane editingAreaPane;

    public static void init(Pane editingArea) {
        editingAreaPane = editingArea;
    }

    public static Pane getEditingArea() {
        return editingAreaPane;
    }


}
