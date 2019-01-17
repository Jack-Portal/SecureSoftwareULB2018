import RSA.PrivKey;
import RSA.PubKey;
import RSA.RSA;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;

/**
 * The User class
 */
public class ServerConnectionHandler implements Runnable{
    //TODO never send the user name, the server has to verify the user!

    public final static int PORT_CLIENT = 5000;
    public final static int PORT_SERVEUR = 5001;

    private PubKey PublicServerKey;

    @Override
    public void run(){
        while (true){
            try {
                DatagramSocket ss = new DatagramSocket(PORT_SERVEUR);
                DatagramPacket dataReceived = new DatagramPacket(new byte[1024],1024);
                ss.receive(dataReceived);
                String data = new String(dataReceived.getData());
                switch(data.charAt(0)){
                    case '0' :
                        //TODO verify server identity
                        AuthentifyServer thread = new AuthentifyServer(new BigInteger(data.split("@")[1]), ss, "TODOREPLACE");
                        thread.run();
                        break;
                    case '1' :
                        //TODO register the client
                        break;
                    case '2':
                        //TODO verify client credentials
                        break;
                    case '3':
                        //TODO other actions.... should have more cases
                        break;
                    default:
                        //this should not be possible.
                        //should be logged
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
