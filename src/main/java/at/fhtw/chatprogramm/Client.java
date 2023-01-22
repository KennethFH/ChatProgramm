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

//Clientklasse
public class Client extends Socket implements Runnable{
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private byte[] readBuffer;
    private byte[] writeBuffer;
    private final Stage clientStage = new Stage();
    private final VBox outerVBox = new VBox();
    private final HBox innerHBox = new HBox();
    private final TextField messageField = new TextField();
    private final TextArea chatArea = new TextArea();
    private final Button sendButton = new Button("send");

    public Client(String host, int port, int buffersize) throws IOException {
        super(host, port);
        outputStream = this.getOutputStream();
        inputStream = this.getInputStream();
        readBuffer = new byte[buffersize];
        writeBuffer = new byte[buffersize];
        prepareStage();
        clientStage.setOnCloseRequest(e -> {
            try {
                sendMessage("Verlasse den Chat");
                this.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

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
                    sendMessage("test");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void run()
    {
        try {
            pollForMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String msg) throws IOException {
        writeBuffer = msg.getBytes();
        outputStream.write(writeBuffer);
    }

    private void pollForMessages() throws IOException {
        if (inputStream.read(readBuffer) != -1){
            chatArea.appendText(new String(readBuffer, StandardCharsets.UTF_8));
        }
    }

    /*void clientloop()
    {
        try
        {
            OutputStream stream  = client.getOutputStream();
            String       message = "Hallo";
            byte[]         buf    = message.getBytes();
            stream.write(buf);
            byte[] abuf = new byte[100];
            InputStream in = client.getInputStream();
            in.read(abuf);
            byte[] loop = new byte[100];
            InputStream loopback = client.getInputStream();
            loopback.read(loop);
            System.out.println("Nachricht: " + new String(abuf, 0, abuf.length) + "\nHier ist der Loopback " + new String(loop, 0, loop.length));
        }
        catch(Exception e2)
        {
            e2.printStackTrace();
        }
    }*/
}
