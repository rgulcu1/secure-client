package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerComm {

    //get the localhost IP address, if server is running on some other IP, you need to use that
    InetAddress host;
    {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    Socket socket = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;



    protected String communicateWithServer(Object request) {
        try {
            socket = new Socket(host.getHostName(), 9876);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(request);

            ois = new ObjectInputStream(socket.getInputStream());
            String response = (String) ois.readObject();
            //close resources
            ois.close();
            oos.close();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
