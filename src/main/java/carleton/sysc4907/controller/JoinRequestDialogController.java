package carleton.sysc4907.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class JoinRequestDialogController extends Dialog<Boolean>{
    public ButtonType allowButtonType;
    public ButtonType denyButtonType;
    public JoinRequestDialogController(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/carleton/sysc4907/view/JoinRequestDialogView.fxml"));
            loader.setController(this);
            DialogPane dialogPane = loader.load();
            dialogPane.lookupButton(allowButtonType).addEventFilter(ActionEvent.ANY, this::onAllowBtnClick);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setTitle("Join Request Received");
            setDialogPane(dialogPane);

            setResultConverter(buttonType -> {
                if(!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                    return false;
                }

                return true;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onAllowBtnClick(ActionEvent actionEvent) {
    }
}
