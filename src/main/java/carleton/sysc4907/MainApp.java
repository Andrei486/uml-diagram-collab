package carleton.sysc4907;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader mainScreenLoader = new FXMLLoader(getClass()
                .getResource("/fxml/MainScreen.fxml"));
        Scene scene = new Scene(mainScreenLoader.load(), 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Diagram View");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}