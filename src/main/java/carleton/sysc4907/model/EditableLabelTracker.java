package carleton.sysc4907.model;

import javafx.beans.property.*;

public class EditableLabelTracker {

    private ObjectProperty<Long> idLastEditedLabel = new SimpleObjectProperty<Long>();
    public Long getIdLastEditedLabel() {return idLastEditedLabel.get();}

    public void setIdLastEditedLabel(Long val) {idLastEditedLabel.set(val);}
    public ObjectProperty<Long> idLastEditedLabelProperty() {return idLastEditedLabel;}

    public EditableLabelTracker() {

    }

}