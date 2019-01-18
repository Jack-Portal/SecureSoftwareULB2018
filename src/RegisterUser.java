import RSA.RSA;
import RSA.PubKey;
import RSA.PrivKey;

import java.io.*;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;

public class RegisterUser implements Runnable{

    public final static int PORT_CLIENT = 5000;
    public final static int PORT_SERVEUR = 5001;
    public String clientAddress;

    private String request;
    public DatagramSocket ss;

    public RegisterUser(String request, DatagramSocket ss, String clientAddress){
        this.request = request;
        this.ss = ss;
        this.clientAddress = clientAddress;
    }

    private boolean checkUserName(String userName) throws IOException {
        //Checking the active users:
        FileReader UsersFile = new FileReader("./Users.txt");
        BufferedReader bufferedReader = new BufferedReader(UsersFile);

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith(userName + " ")){
                return false;
            }
        }

        //checking the requests:
        UsersFile = new FileReader("./Requests.txt");
        bufferedReader = new BufferedReader(UsersFile);

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


    @Override
    public void run() {
        // load the server public key from stored file
        try {
            PrivKey pvk = Encryption.loadPrivateKey();

            // split the request
            //This is how the request is made:
            //String keyRequest = "1" + "@" + userName + "@" + publicKey + "@" + enc.toString();
            String[] requestData = request.split("@");


            // check that the username is not already taken
            try {
                if (checkUserName(requestData[1])){
                    // decrypt the password
                    String password = new String( RSA.decrypte(pvk, new BigInteger(requestData[3]) ).toByteArray());

                    // hash the password
                    String hashedPwd = Encryption.sha256(password);

                    // add the information to the Requests file
                    //TODO add address
                    String userDetail = requestData[1] + " " + hashedPwd + " " + requestData[2];
                    BufferedWriter writer =
                            Files.newBufferedWriter(Paths.get("./Users.txt"),
                                    StandardOpenOption.APPEND);
                    writer.append(userDetail);
                    writer.newLine();
                    writer.close();

                    // reply ok
                    byte[] bytesToSend = "Success.".getBytes();
                    DatagramPacket dataSent = new DatagramPacket(bytesToSend, bytesToSend.length, InetAddress.getByName(this.clientAddress),PORT_SERVEUR);
                    this.ss.send(dataSent);
                } else {
                    // reply not ok if not ok
                    byte[] bytesToSend = "Fail: user name already used.".getBytes();
                    DatagramPacket dataSent = new DatagramPacket(bytesToSend, bytesToSend.length, InetAddress.getByName(this.clientAddress),PORT_SERVEUR);
                    this.ss.send(dataSent);
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
