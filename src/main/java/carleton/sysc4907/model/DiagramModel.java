package carleton.sysc4907.model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

/**
 * Model for the UML diagram
 */
public class DiagramModel {

    private final ObservableList<DiagramElement> elements;

    private final ObservableList<DiagramElement> selectedElements;

    private final BooleanProperty isElementSelected = new SimpleBooleanProperty();

    public DiagramModel() {
        elements = FXCollections.observableList(new LinkedList<>());
        selectedElements = FXCollections.observableList(new LinkedList<>());
        ListProperty<DiagramElement> listProperty = new SimpleListProperty<>();
        listProperty.setValue(selectedElements);
        isElementSelected.bind(listProperty.emptyProperty());
    }

    /**
     * Gets all elements currently in this diagram.
     * @return an ObservableList containing all diagram elements
     */
    public ObservableList<DiagramElement> getElements() {
        return elements;
    }

    /**
     * Gets the currently selected element.
     *
     * @return the selected DiagramElement, or null if none selected
     */
    public ObservableList<DiagramElement> getSelectedElements() {
        return selectedElements;
    }

    public ObservableValue<Boolean> getIsElementSelectedProperty() {
        return isElementSelected;
    }
}
