package carleton.sysc4907.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PreferencesModel {
    private final StringProperty username = new SimpleStringProperty();

    public PreferencesModel() {

    }

    public String getUsername() {return username.get();}
    public void setUsername(String val) {username.set(val);}
    public StringProperty usernameProperty() {return username;}

}
