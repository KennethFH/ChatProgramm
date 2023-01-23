package at.fhtw.chatprogramm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Die Hauptklasse mit dem Hauptthread in dem die Nebenthreads erstellt
 * werden für Clients und den Server
 * @see Client
 * @see Server
 */
public class HelloApplication extends Application {


    /**
     * Constructor von HelloApplication, damit auch der Constructor von HelloApplication einen Kommentar hat und Javadocs ruhe gibt!
     */
    HelloApplication()
    {}
    /**
     * Das Fenster für die Server/Clientwahl wird eingestellt, wo welcher Knopf, welches Textfeld liegt
     * und was beim Drücken eines Knopfes passiert.
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     */
    @Override
    public void start(Stage stage) {
        Text client = new Text("Client");
        client.setLineSpacing(144);

        TextField ipport = new TextField();
        ipport.setPromptText("IP:Port");

        TextField nickname = new TextField();
        nickname.setPromptText("Nickname");

        Button connectButton = new Button();
        connectButton.setText("Connect");
        connectButton.setPrefWidth(200);
        connectButton.setLineSpacing(12);

        VBox links = new VBox();
        links.setAlignment(Pos.TOP_CENTER);
        links.getChildren().addAll(client, ipport, nickname, connectButton);
        links.setSpacing(12);

        Text Server = new Text("Server");
        Server.setLineSpacing(144);

        Button hostButton = new Button();
        hostButton.setText("Host");
        hostButton.setPrefWidth(200);
        hostButton.setLineSpacing(12);

        VBox rechts = new VBox();
        rechts.setAlignment(Pos.TOP_CENTER);
        rechts.getChildren().addAll(Server, hostButton);
        rechts.setSpacing(12);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(links, rechts);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(12);
        int buffersize = 100;

        //Connect Button will start the client
        connectButton.setOnAction(e -> {
            if (!ipport.getText().isEmpty() && !nickname.getText().isEmpty()){
                String[] ipp = ipport.getText().split(":", 2);
                startClientWithInterface(ipp[0], ipp.length < 2 ? 4711 : Integer.parseInt(ipp[1]), nickname.getText(), buffersize);
            }
        });

        //Host Button will start the server
        hostButton.setOnAction(e -> {
            try {
                new Thread(new Server(buffersize)).start();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Scene scene = new Scene(hbox, 600, 400);
        stage.setTitle("Chat Application Hub");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Es wird ein Client erstellt
     * @param address Die Addresse mit der man sich verbinden möchte
     * @param port  Der Port mit dem man sich verbinden möchte
     * @param nickname Der eigene Name
     * @param buffersize Die größe der größten zu sendenden Nachricht.
     * @see Client
     */
    private void startClientWithInterface(String address, int port, String nickname, int buffersize) {
        Client client;
        try {
            client = new Client(address, port, nickname, buffersize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(client).start();
    }

    /**
     * Es wird alles gestartet.
     * @param args Parameter die an das Programm übergeben werden. (Werden hier nicht benötigt)
     * @see HelloApplication
     */
    public static void main(String[] args) {
        launch();
    }
}