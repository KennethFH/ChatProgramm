package at.fhtw.chatprogramm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die schaut ob ein Client sich mit dem Server verbinden möchte
 * @see Server
 */
public class ConnectionHandler implements Runnable {

    /**
     * Socket des Servers
     */
    private final ServerSocket socket;
    /**
     * Liste der Verbindungen
     */
    private List<SocketAcceptedEvent> listeners;
    /**
     * Variable zum Beenden
     */
    private boolean isRunning = true;

    /**
     * Erstellt alles wichtige zum Clientannehmen
     * @param socket Socket des Servers
     */
    public ConnectionHandler(ServerSocket socket){
        this.socket = socket;
        listeners = new ArrayList<>();
    }

    /**
     * schaltet den Listener ein/aus
     * @param running Ein-/Ausschalter
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Fügt den Client zur liste hinzu
     * @param e Event
     */
    public void addSocketAcceptedEventListener(SocketAcceptedEvent e){
        listeners.add(e);
    }

    /**
     * gibt den Socket zurück
     * @return gibt den Socket zurück
     */
    public ServerSocket getSocket() {
        return socket;
    }

    /**
     * Endlosschleife. Hört dauerhaft darauf ob, sich ein Client verbinden möchte
     */
    @Override
    public void run() {
        while (isRunning){
            Socket s;
            try {
                s = socket.accept();
                if (s == null){
                    return;
                }
                for (SocketAcceptedEvent e : listeners) {
                    e.onSocketAccepted(s);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
