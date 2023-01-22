package at.fhtw.chatprogramm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {

    private final ServerSocket socket;
    private List<SocketAcceptedEvent> listeners;

    public ConnectionHandler(ServerSocket socket){
        this.socket = socket;
        listeners = new ArrayList<>();
    }

    public void addSocketAcceptedEventListener(SocketAcceptedEvent e){
        listeners.add(e);
    }

    public ServerSocket getSocket() {
        return socket;
    }
    @Override
    public void run() {
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
