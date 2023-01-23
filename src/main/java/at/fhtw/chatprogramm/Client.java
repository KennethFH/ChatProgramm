package at.fhtw.chatprogramm;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Die Clientklasse, um einen Clientanzulegen.
 */
//Clientklasse
public class Client extends Socket implements Runnable{

    /**
     * Der Name des Clientbenutzers
     */
    private final String nickname;
    /**
     * Die Nachricht die gesendet wird
     */
    private final OutputStream outputStream;
    /**
     * Die Nachricht die empfangen wird
     */
    private final InputStream inputStream;
    /**
     * Speicher für das Lesen
     */
    private byte[] readBuffer;
    /**
     * Speicher für das schreiben
     */
    private byte[] writeBuffer;
    /**
     * Das Fenster des Clients
     */
    private final Stage clientStage = new Stage();
    /**
     * Virtuelles Rechteck in dem alles für das Fenster drinnen ist
     */
    private final VBox outerVBox = new VBox();
    /**
     * Virtuelles Rechteck in dem alles fürs Senden für das Fenster drinnen ist
     */
    private final HBox innerHBox = new HBox();
    /**
     * Das Feld in dem die Nachricht hineingeschrieben wird
     */
    private final TextField messageField = new TextField();
    /**
     * Das Feld in dem die eingehenden und ausgehenden Nachrichten angezeigt werden.
     */
    private final TextArea chatArea = new TextArea();
    /**
     * Knopf um die Nachricht zu senden
     */
    private final Button sendButton = new Button("send");
    /**
     * Variable um zu wissen ob das Fenster schon geschlossen wurde oder es noch offen ist
     */
    private boolean isRunning = true;

    /**
     * Ein Client wird hergerichtet
     * @param host IP des Servers
     * @param port Port des Servers
     * @param nickname Name des Clientbenutzers
     * @param buffersize größte Größe der Sendenachricht
     * @throws IOException Exception für den Fall, dass beim senden etwas schiefläuft
     */
    public Client(String host, int port, String nickname, int buffersize) throws IOException {
        super(host, port);
        this.nickname = nickname;
        outputStream = this.getOutputStream();
        inputStream = this.getInputStream();
        readBuffer = new byte[buffersize];
        writeBuffer = new byte[buffersize];
        prepareStage();
        clientStage.setOnCloseRequest(e -> {
            try {
                sendMessage("Verlasse den Chat");
                isRunning = false;
                this.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Das Fenster für den Client wird erstellt
     */
    private void prepareStage(){
        double width = 300;
        double height = 200;
        sendButton.setPrefWidth(60);
        messageField.setPrefWidth(width -60);
        innerHBox.getChildren().addAll(messageField, sendButton);
        outerVBox.getChildren().addAll(innerHBox, chatArea);
        Scene scene = new Scene(outerVBox, width, height);
        clientStage.setTitle("ChatProgramm - Titel");
        clientStage.setScene(scene);
        clientStage.show();

        sendButton.setOnAction(event -> {
            if (this.isConnected()){
                try {
                    sendMessage(messageField.getText());
                    messageField.clear();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Endlosschliefe, Es wird immer wieder nachgeschaut ob schon eine Nachricht da ist
     */
    @Override
    public void run()
    {
        while (isRunning){
            try {
                checkForMessages();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Eine Nachricht wird gesendet
     * @param msg Die zu sendende Nachricht
     * @throws IOException Falls beim senden etwas schief läuft
     */
    private void sendMessage(String msg) throws IOException {
        writeBuffer = msg.getBytes();
        outputStream.write(writeBuffer);
    }

    /**
     * Es wird nachgesehen ob eine Nachricht geschickt wurde und diese auch eingelesen.
     * @throws IOException Falls beim lesen etwas schiefläuft
     */
    private void checkForMessages() throws IOException {
        if (inputStream.read(readBuffer) > 0){
            chatArea.appendText(new String(readBuffer, StandardCharsets.UTF_8) + "\n");
        }
    }
}
