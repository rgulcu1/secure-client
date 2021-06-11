import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class client {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;


        while (true) {
            socket = new Socket(host.getHostName(), 9876);

            Scanner sc = new Scanner(System.in);
            String input = sc.next();
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(input);

            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //close resources
            ois.close();
            oos.close();


        }

    }
}
