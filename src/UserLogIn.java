import RSA.PrivKey;
import RSA.RSA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class UserLogIn implements Runnable{


    public final static int PORT_CLIENT = 5000;
    public final static int PORT_SERVEUR = 5001;
    public String clientAddress;

    private String request;
    public DatagramSocket ss;


    private boolean checkUserName(String userName) throws IOException {
        //checking the requests:
        FileReader UsersFile = new FileReader("./Requests.txt");
        BufferedReader bufferedReader = new BufferedReader(UsersFile);
        String line = "";

        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith(userName + " ")){
                return false;
            }
        }

        //Checking the inactive users:
        UsersFile = new FileReader("./FrozenAccounts.txt");
        bufferedReader = new BufferedReader(UsersFile);

        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith(userName + " ")){
                return false;
            }
        }

        // If none of these files contain the user name then it is available:
        return true;
    }


    public UserLogIn(String request, DatagramSocket ss, String clientAddress){
        this.request = request;
        this.ss = ss;
        this.clientAddress = clientAddress;
    }

    @Override
    public void run(){
        try {
            PrivKey pvk = Encryption.loadPrivateKey();
            // split the request
            //This is how the request is made:
            //String keyRequest = "2" + "@" + userName + "@" + enc.toString();
            String[] requestData = request.split("@");
            String hashedPassword = Encryption.sha256(new String(RSA.decrypte(pvk, new BigInteger(requestData[2])).toByteArray()));
            //Checking the active users:
            FileReader UsersFile = new FileReader("./Users.txt");
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            boolean goodCredentials = false;
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(requestData[1] + " ")){
                    if (line.split(" ")[1].equals(hashedPassword)){
                        goodCredentials = true;
                    }
                }
            }
            if (goodCredentials) {
                //TODO respond with a session number
                byte[] bytesToSend = "Success.".getBytes();
                DatagramPacket dataSent = new DatagramPacket(bytesToSend, bytesToSend.length, InetAddress.getByName(this.clientAddress),PORT_SERVEUR);
                this.ss.send(dataSent);
            }
            else {
                if (checkUserName(requestData[1])) {
                    byte[] bytesToSend = "Fail, your account has either not been approved yet, or has been suspended.".getBytes();
                    DatagramPacket dataSent = new DatagramPacket(bytesToSend, bytesToSend.length, InetAddress.getByName(this.clientAddress), PORT_SERVEUR);
                    this.ss.send(dataSent);
                }
                else {
                    byte[] bytesToSend = "Fail, Try again!.".getBytes();
                    DatagramPacket dataSent = new DatagramPacket(bytesToSend, bytesToSend.length, InetAddress.getByName(this.clientAddress), PORT_SERVEUR);
                    this.ss.send(dataSent);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
