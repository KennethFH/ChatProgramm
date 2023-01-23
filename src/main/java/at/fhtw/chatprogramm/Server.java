package at.fhtw.chatprogramm;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
     * Wer hat sich mit wem verbunden
     */
    private final TextArea console = new TextArea();
    /**
     * schaut dauerhaft ob sich neue Clients verbinden
     */
    private final ConnectionHandler connectionHandler;
    /**
     * schon verbundene Clients
     */
    List<Socket> clients = new CopyOnWriteArrayList<>();
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
        Scene scene = new Scene(console, 600,400);
        serverStage.setTitle("ChatProgramm - Server");
        serverStage.setScene(scene);
        serverStage.show();
    }
    /**
     * Endlosschleife. Wenn etwas geschrieben wird, wird es an alle anderen Clients verteilt
     */
    @Override
    public void run() {
        while (isRunning) {
            for (int i = 0; i < clients.size(); i++) {
                readBuffer = new byte[buffersize];
                try {
                    if (clients.get(i).getInputStream().available() > 0 && clients.get(i).getInputStream().read(readBuffer) > 0) {
                        for (int o = 0; o < clients.size(); o++) {
                            clients.get(o).getOutputStream().write(readBuffer);
                        }
                    }
                } catch (IOException e) {
                    console.appendText("" + clients.size() + "\n");
                    console.appendText(e.getMessage() + "\n");
                    throw new RuntimeException(e);
                }
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
        for (int o = 0; o < clients.size(); o++){
            clients.get(o).getOutputStream().write(("A new Client has connected! Clients: " + clients.size()+1).getBytes(StandardCharsets.UTF_8));
        }
        clients.add(client);
        console.appendText(client.getLocalAddress() + " connected! Clients: " + clients.size() + "\n");
    }
}
