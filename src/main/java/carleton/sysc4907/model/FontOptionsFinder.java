package carleton.sysc4907.model;

import javafx.scene.text.Font;

import java.util.List;

/**
 * A class responsible for finding font options for the current system.
 */
public class FontOptionsFinder {



    public FontOptionsFinder() {

    }

    /**
     * Gets the list of font families supported by the system.
     * @return a List of font family names supported
     */
    public List<String> getFontFamilyNames() {
        return Font.getFamilies();
    }
}
