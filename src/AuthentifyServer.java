import RSA.RSA;

import RSA.PrivKey;
import RSA.PubKey;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

    public void run(){
        try {
            //TODO load the server public key from stored file
            FileReader fileReader = new FileReader("./ServerKeys");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String publicKey = bufferedReader.readLine();
            String privateKey = bufferedReader.readLine();
            bufferedReader.close();
            fileReader.close();

            //TODO convert string obtained above to big int
            PubKey pbk  = null;
            PrivKey pvk = null;


            //TODO decrypt the cipheredMessage
            BigInteger dec = RSA.decrypte( pvk , this.cipheredMessage);
            String decrytpedMessage = new String(dec.toByteArray());


            //TODO change the client address
            byte[] bytesToSend = decrytpedMessage.getBytes();
            DatagramPacket dataSent = new DatagramPacket(bytesToSend, bytesToSend.length,InetAddress.getByName(this.clientAddress),PORT_SERVEUR);
            this.ss.send(dataSent);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
