package at.fhtw.chatprogramm;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//Serverklasse
public class Server implements Runnable, SocketAcceptedEvent {

    private byte[] readBuffer;
    private final Stage serverStage = new Stage();
    private final ConnectionHandler connectionHandler;
    List<Socket> clients = new ArrayList<>();
    List<OutputStream> outputStreams = new ArrayList<>();
    List<InputStream> inputStreams = new ArrayList<>();
    private boolean isRunning = true;
    private final int buffersize;

    public Server(int buffersize) throws IOException {
        connectionHandler = new ConnectionHandler(new ServerSocket(4711));
        connectionHandler.addSocketAcceptedEventListener(this);
        new Thread(connectionHandler).start();
        this.buffersize = buffersize;
        readBuffer = new byte[buffersize];
        prepareStage();
        serverStage.setOnCloseRequest(e -> {
            try {
                isRunning = false;
                connectionHandler.setRunning(false);
                connectionHandler.getSocket().close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void prepareStage(){

    }

    @Override
    public void run(){
        while (isRunning)
        {
            try {
                for (int i = 0; i < inputStreams.size(); i++){
                    readBuffer = new byte[buffersize];
                    if (inputStreams.get(i).available() > 0 && inputStreams.get(i).read(readBuffer) > 0){
                        for (int o = 0; o < outputStreams.size(); o++){
                            outputStreams.get(o).write(readBuffer);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onSocketAccepted(Socket client) throws IOException {
        /*for (OutputStream outputStream : outputStreams) {
            outputStream.write("A new Client has connected!".getBytes(StandardCharsets.UTF_8));
        }*/
        clients.add(client);
        inputStreams.add(client.getInputStream());
        outputStreams.add(client.getOutputStream());
    }
}
