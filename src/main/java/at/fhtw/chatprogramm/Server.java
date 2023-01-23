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

/**
 * Klasse für den Server
 * @see ConnectionHandler
 * @see SocketAcceptedEvent
 */
public class Server implements Runnable, SocketAcceptedEvent {

    /**
     * Speicher für eingehende Nachrichten
     */
    private byte[] readBuffer;
    /**
     * Fenster, des Servers
     */
    private final Stage serverStage = new Stage();
    /**
     * schaut dauerhaft ob sich neue Clients verbinden
     */
    private final ConnectionHandler connectionHandler;
    /**
     * schon verbundene Clients
     */
    List<Socket> clients = new ArrayList<>();
    /**
     * Nachrichten zum Senden an die Clients
     */
    List<OutputStream> outputStreams = new ArrayList<>();
    /**
     * Nachrichten zum lesen von den Clients
     */
    List<InputStream> inputStreams = new ArrayList<>();
    /**
     * Ein-/Ausgeschalten
     */
    private boolean isRunning = true;
    /**
     * größte Größe der größten Nachricht
     */
    private final int buffersize;

    /**
     * der Server wird hergerichtet
     * @param buffersize größte Größe der größten Nachricht
     * @throws IOException falls beim lesen/schreiben was schiefläuft
     */
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

    /**
     * Das Fenster für den Server wird hergerichtet
     */
    private void prepareStage(){

    }

    /**
     * Endlosschleife. Wenn etwas geschrieben wird, wird es an alle anderen Clients verteilt
     */
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

    /**
     * Fügt Clients hinzu
     * @param client der Client der sich neuverbunden hat
     * @throws IOException falls etwas schiefläuft
     */
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
