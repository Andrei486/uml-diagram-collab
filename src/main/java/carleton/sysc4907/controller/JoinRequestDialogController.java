package carleton.sysc4907.controller;
import com.sun.javafx.stage.WindowHelper;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class JoinRequestDialogController extends Dialog<Boolean>{
    public ButtonType allowButtonType;
    public ButtonType denyButtonType;

    @FXML
    DialogPane dialogPane;

    private final int MARGIN_SIZE = 8;

    private final int DEFAULT_WIDTH = 375;
    private final int DEFAULT_HEIGHT = 215;

    private Window owner;
    @FXML
    public Label title_label;
    public JoinRequestDialogController(Window owner, String username) {
        try {
            FXMLLoader loader = new FXMLLoader();
            this.owner = owner;
            loader.setLocation(getClass().getResource("/carleton/sysc4907/view/JoinRequestDialogView.fxml"));
            loader.setController(this);
            DialogPane dialogPane = loader.load();
            dialogPane.lookupButton(allowButtonType).addEventFilter(ActionEvent.ANY, this::onAllowBtnClick);
            initOwner(owner);
            initModality(Modality.NONE);
            initStyle(StageStyle.UNDECORATED);
            setTitle("Join Request Received");
            title_label.setText(username + " has requested to join!");
            setHeight(DEFAULT_HEIGHT);
            setWidth(DEFAULT_WIDTH);

            //must use default width and height here because the dialog is not initialized yet
            setY(owner.getY() + owner.getHeight() - DEFAULT_HEIGHT - MARGIN_SIZE);
            setX(owner.getX() + owner.getWidth() - DEFAULT_WIDTH - MARGIN_SIZE);

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

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                setY(owner.getY() + owner.getHeight() - dialogPane.getHeight() - MARGIN_SIZE);
                setX(owner.getX() + owner.getWidth() - dialogPane.getWidth() - MARGIN_SIZE);
        };

        owner.widthProperty().addListener(stageSizeListener);
        owner.heightProperty().addListener(stageSizeListener);
        owner.xProperty().addListener(stageSizeListener);
        owner.yProperty().addListener(stageSizeListener);
    }

    @FXML
    public void initialize() {
        setY(owner.getY() + owner.getHeight() - dialogPane.getHeight() - MARGIN_SIZE);
        setX(owner.getX() + owner.getWidth() - dialogPane.getWidth() - MARGIN_SIZE);
    }

    private void onAllowBtnClick(ActionEvent actionEvent) {
    }
}
