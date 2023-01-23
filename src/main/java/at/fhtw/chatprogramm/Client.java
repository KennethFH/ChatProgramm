package at.fhtw.chatprogramm;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//Clientklasse
public class Client extends Socket implements Runnable{

    private final String nickname;
    private byte[] readBuffer;
    private byte[] writeBuffer;
    private final Stage clientStage = new Stage();
    private final VBox outerVBox = new VBox();
    private final HBox innerHBox = new HBox();
    private final TextField messageField = new TextField();
    private final TextArea chatArea = new TextArea();
    private final Button sendButton = new Button("send");
    private boolean isRunning = true;

    public Client(String host, int port, String nickname, int buffersize) throws IOException {
        super(host, port);
        this.nickname = nickname;
        readBuffer = new byte[buffersize];
        writeBuffer = new byte[buffersize];
        prepareStage();
        clientStage.setOnCloseRequest(e -> {
            try {
                sendMessage(nickname + " disconnected.");
                isRunning = false;
                this.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void prepareStage(){
        double width = 600;
        double height = 400;
        sendButton.setPrefWidth(60);
        messageField.setPrefWidth(width - 60);
        innerHBox.getChildren().addAll(messageField, sendButton);
        outerVBox.getChildren().addAll(innerHBox, chatArea);
        Scene scene = new Scene(outerVBox, width, height);
        clientStage.setTitle("ChatProgramm - Client");
        clientStage.setScene(scene);
        clientStage.show();

        sendButton.setOnAction(event -> {
            if (this.isConnected()){
                try {
                    sendMessageWithPrefix(messageField.getText());
                    messageField.clear();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && this.isConnected()) {
                try {
                    sendMessageWithPrefix(messageField.getText());
                    messageField.clear();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void run()
    {
        while (isRunning){
            try {
                if (this.isConnected()){
                    checkForMessages();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessage(String msg) throws IOException {
        String test = msg.replaceAll("\n", "").replaceAll("\r", "").strip();
        writeBuffer = test.getBytes(StandardCharsets.UTF_8);
        getOutputStream().write(writeBuffer);
    }

    private void sendMessageWithPrefix(String msg) throws IOException {
        if (!msg.isBlank()){
            sendMessage(nickname + ": " + msg);
        }
    }

    private void checkForMessages() throws IOException {
        if (getInputStream().available() > 0 && getInputStream().read(readBuffer) > 0){
            chatArea.appendText(new String(readBuffer, StandardCharsets.UTF_8) + "\n");
        }
    }
}
