package carleton.sysc4907.model;

public class EditableLabelTracker {

    private long idLastEditedLabel;
    public EditableLabelTracker() {

    }

    /**
     * Gets the ID of the last edited editable label. May return null.
     * @return the ID of the last edited editable label.
     */
    public long getIdLastEditedLabel() {
        return idLastEditedLabel;
    }

    public void setIdLastEditedLabel(long idLastEditedLabel) {
        this.idLastEditedLabel = idLastEditedLabel;
    }
}
