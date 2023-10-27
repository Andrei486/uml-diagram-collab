package carleton.sysc4907.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TextFormattingModel {

    private final FontOptionsFinder fontFinder;

    private final ObservableList<String> fontFamilyNames;

    public TextFormattingModel(FontOptionsFinder fontFinder) {
        this.fontFinder = fontFinder;
        fontFamilyNames = FXCollections.observableList(fontFinder.getFontFamilyNames());
    }

    public ObservableList<String> getFontFamilyNames() {
        return fontFamilyNames;
    }
}
