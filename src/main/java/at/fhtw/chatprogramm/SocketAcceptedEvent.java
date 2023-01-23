package at.fhtw.chatprogramm;

import java.io.IOException;
import java.net.Socket;

/**
 * Fügt Clients hinzu
 * @see Server
 */
public interface SocketAcceptedEvent {
    /**
     * Fügt Clients hinzu
     * @param client der Client der sich neuverbunden hat
     * @throws IOException falls etwas schiefläuft
     */
    void onSocketAccepted(Socket client) throws IOException;
}
