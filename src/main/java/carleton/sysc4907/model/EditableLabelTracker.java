package carleton.sysc4907.model;

import carleton.sysc4907.processing.ElementIdManager;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Keeps track of the last edited EditableLabel's ID and style properties.
 */
public class EditableLabelTracker {

    private final ObjectProperty<Long> idLastEditedLabel = new SimpleObjectProperty<Long>();
    private final BooleanProperty isBoldProperty = new SimpleBooleanProperty();
    private final BooleanProperty isItalicProperty = new SimpleBooleanProperty();
    private final BooleanProperty isUnderlinedProperty = new SimpleBooleanProperty();
    private final DoubleProperty fontSizeProperty = new SimpleDoubleProperty();
    private final StringProperty fontFamilyProperty = new SimpleStringProperty();

    private final ElementIdManager elementIdManager;

    /**
     * Constructor for the EditableLabelTracker.
     */
    public EditableLabelTracker(ElementIdManager elementIdManager) {
        this.elementIdManager = elementIdManager;
    }

    public Long getIdLastEditedLabel() {return idLastEditedLabel.get();}

    public void setIdLastEditedLabel(Long id) {
        idLastEditedLabel.set(id);
        Node labelNode = elementIdManager.getElementById(id);

        if (!(labelNode instanceof Label label)) {
            return;
        }

        var styleClass = label.getStyleClass();
        isBoldProperty.set(styleClass.contains("bolded"));
        isItalicProperty.set(styleClass.contains("italicized"));
        isUnderlinedProperty.set(styleClass.contains("underlined"));
        System.out.println("font family in tracker: " + label.getFont().getFamily());
        fontFamilyProperty.set(label.getFont().getFamily());
        fontSizeProperty.set(label.getFont().getSize());
    }

    public ReadOnlyObjectProperty<Long> idLastEditedLabelProperty() {return idLastEditedLabel;}

    public BooleanProperty getIsBoldedProperty() {return isBoldProperty;}

    public BooleanProperty getIsUnderlinedProperty() {return isUnderlinedProperty;}

    public BooleanProperty getIsItalicizedProperty() {return isItalicProperty;}

    public DoubleProperty getFontSizeProperty() {return fontSizeProperty;}

    public StringProperty getFontFamilyProperty() {return fontFamilyProperty;}


}
