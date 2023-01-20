package at.fhtw.chatprogramm;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
//Clientklasse
public class Client {
    static Socket client;

    void start()
    {
        try
        {
            client = new Socket("localhost", 4711);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        while (client != null){
            clientloop();
        }
    }

    void clientloop()
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
            //client.close();
        }
        catch(Exception e2)
        {
            e2.printStackTrace();
        }
    }
}
