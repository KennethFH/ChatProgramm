package at.fhtw.chatprogramm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;

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

        //Connect Button will start a client
        connectButton.setOnAction(e -> startClientWithInterface("localhost", 4711));

        //Host Button will start the server
        hostButton.setOnAction(e -> new Thread(new Server()).start());

        Scene scene = new Scene(vbox, 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void startClientWithInterface(String address, int port) {
        Client client;
        try {
            client = new Client(address, port, 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(client).start();
    }

    public static void main(String[] args) {
        launch();
    }
}