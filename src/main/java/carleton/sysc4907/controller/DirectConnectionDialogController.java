package carleton.sysc4907.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class DirectConnectionDialogController extends Dialog<String>{
    public Button connectBtn;

    public javafx.scene.control.TextField ipPortField;

    public ButtonType connectButtonType;

    public ButtonType cancelButtonType;

    private ObjectProperty<String> ipPort = new SimpleObjectProperty<>(null);


    public DirectConnectionDialogController(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/carleton/sysc4907/view/DirectConnectionDialogView.fxml"));
            loader.setController(this);
            DialogPane dialogPane = loader.load();
            dialogPane.lookupButton(connectButtonType).addEventFilter(ActionEvent.ANY, this::onConnectBtnClick);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setTitle("Direct Connect");
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                if(!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                    return null;
                }

                return ipPortField.getText();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onConnectBtnClick(ActionEvent actionEvent) {
    }

    public void Show() {

    }
}
