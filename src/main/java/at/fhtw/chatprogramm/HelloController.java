package at.fhtw.chatprogramm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Stage stage;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("hello-view2.fxml"));
        Scene scene2 = new Scene(fxmlLoader2.load(), 320, 240);
        stage.setScene(scene2);
        stage.show();
    }
}