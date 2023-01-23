package at.fhtw.chatprogramm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {

    private final ServerSocket socket;
    private List<SocketAcceptedEvent> listeners;
    private boolean isRunning = true;

    public ConnectionHandler(ServerSocket socket){
        this.socket = socket;
        listeners = new ArrayList<>();
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void addSocketAcceptedEventListener(SocketAcceptedEvent e){
        listeners.add(e);
    }

    public ServerSocket getSocket() {
        return socket;
    }
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
