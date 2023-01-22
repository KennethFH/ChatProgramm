package at.fhtw.chatprogramm;

import java.io.IOException;
import java.net.Socket;

public interface SocketAcceptedEvent {
    void onSocketAccepted(Socket client) throws IOException;
}
