package carleton.sysc4907.model;

import javafx.scene.text.Font;

import java.util.List;

public class FontOptionsFinder {



    public FontOptionsFinder() {

    }

    public List<String> getFontFamilyNames() {
        return Font.getFamilies();
    }
}
