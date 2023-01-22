package at.fhtw.chatprogramm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        Button connectButton = new Button();
        Button hostButton = new Button();
        connectButton.setText("Connect");
        connectButton.setPrefWidth(200);
        hostButton.setText("Host");
        hostButton.setPrefWidth(200);
        vbox.getChildren().addAll(connectButton, hostButton);

        connectButton.setOnAction(e -> {
            Stage clientStage = new Stage();
            VBox testbox = new VBox();
            Scene scene = new Scene(testbox, 600, 400);
            clientStage.setTitle("Client Application");
            clientStage.setScene(scene);
            clientStage.show();
            Client c = new Client();
            Thread t = new Thread(c);
            t.start();
        });

        hostButton.setOnAction(e -> {
            Stage serverStage = new Stage();
            VBox testbox = new VBox();
            Scene scene = new Scene(testbox, 600, 400);
            serverStage.setTitle("Server Application");
            serverStage.setScene(scene);
            serverStage.show();
            Server s = new Server();
            Thread t = new Thread(s);
            t.start();
        });

        Scene scene = new Scene(vbox, 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}