package at.fhtw.chatprogramm;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//Serverklasse
public class Server {

    public void start(){
        ServerSocket socket;
        Socket client;
        try {
            socket = new ServerSocket(4711);
            System.out.println("Server: waiting for connection");
            client = socket.accept();
            while (true)
            {
                InputStream eingang;
                try
                {
                    eingang = client.getInputStream();
                }
                catch(Exception e2)
                {
                    e2.printStackTrace();
                    break;
                }
                byte[]      buf     = new byte[100];
                try
                {
                    eingang.read(buf);
                }
                catch(Exception e2)
                {
                    e2.printStackTrace();
                    break;
                }
                String nachricht = new String(buf);
                String Nachricht = "ACK";
                byte[] abuf = Nachricht.getBytes();
                OutputStream ack = client.getOutputStream();
                ack.write(abuf);
                OutputStream loop = client.getOutputStream();
                loop.write(buf);
                System.out.println("Nachricht: " +  nachricht);
                System.out.println("Von: " + client.getInetAddress() + ": " + client.getPort());
                if(buf[0] == 't' && buf[1] == 's' && buf[2] == 'c' && buf[3] == 'h' && buf[4] == 'a' && buf[5] == 'u')
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
