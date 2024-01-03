package carleton.sysc4907.model;

import carleton.sysc4907.processing.FontOptionsFinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A model containing information about text formatting, including fonts.
 */
public class TextFormattingModel {

    private final FontOptionsFinder fontFinder;

    private final ObservableList<String> fontFamilyNames;

    /**
     * Constructs a new TextFormattingModel.
     * @param fontFinder the FontOptionsFinder used to get font options from the system
     */
    public TextFormattingModel(FontOptionsFinder fontFinder) {
        this.fontFinder = fontFinder;
        fontFamilyNames = FXCollections.observableList(fontFinder.getFontFamilyNames());
    }

    /**
     * Gets an ObservableList containing all supported font families for listeners to bind to.
     * @return an ObservableList containing all supported font family names
     */
    public ObservableList<String> getFontFamilyNames() {
        return fontFamilyNames;
    }
}
