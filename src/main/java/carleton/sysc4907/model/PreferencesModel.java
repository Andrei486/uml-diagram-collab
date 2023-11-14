package carleton.sysc4907.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PreferencesModel {
    private StringProperty username = new SimpleStringProperty();
    public final String getUsername() {return username.get();}
    public final void setUsername(String val) {username.set(val);}
    public StringProperty usernameProperty() {return username;}

    public PreferencesModel() {

    }

}
