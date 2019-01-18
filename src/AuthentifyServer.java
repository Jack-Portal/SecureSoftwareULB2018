import RSA.RSA;

import RSA.PrivKey;
import RSA.PubKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AuthentifyServer implements Runnable {

    public final static int PORT_CLIENT = 5000;
    public final static int PORT_SERVEUR = 5001;
    public String clientAddress;

    private BigInteger cipheredMessage;
    public DatagramSocket ss;

    public AuthentifyServer(BigInteger cipheredMessage, DatagramSocket ss, String clientAddress){
        this.cipheredMessage = cipheredMessage;
        this.ss = ss;
        this.clientAddress = clientAddress;
    }

    @Override
    public void run(){
        try {
            // load the server public key from stored file
            PrivKey pvk = Encryption.loadPrivateKey(ServerCommandLineInterface.ServerKeysFileLocation);


            // decrypt the cipheredMessage
            BigInteger dec = RSA.decrypte( pvk , this.cipheredMessage);
            String decrytpedMessage = new String(dec.toByteArray());


            // change the client address
            byte[] bytesToSend = decrytpedMessage.getBytes();
            DatagramPacket dataSent = new DatagramPacket(bytesToSend, bytesToSend.length,InetAddress.getByName(this.clientAddress),PORT_SERVEUR);
            this.ss.send(dataSent);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
