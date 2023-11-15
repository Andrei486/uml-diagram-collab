package carleton.sysc4907.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PreferencesModelTest {
    private PreferencesModel preferencesModel;

    @BeforeEach
    public void setup() {
        preferencesModel = new PreferencesModel();
    }

    @Test
    public void getUsernameTest() {
        String expectedUsername = "testUsername";
        preferencesModel.setUsername(expectedUsername);
        String actualUsername = preferencesModel.getUsername();
        assertEquals(expectedUsername, actualUsername);
    }

}
