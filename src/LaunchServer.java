import RSA.RSA;
import RSA.PrivKey;
import RSA.PubKey;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class LaunchServer {

    //Server related attributes
    public static String ServerAddress;
    public static String UserFileLocation;
    public static String RequestFileLocation;
    public static String FrozenAccountsFileLocation;
    public static String ServerKeysFileLocation;
    public static String AdminUserName;
    public static String AdminHashedPassword;
    public static String Salt;
    public static PrivKey pvk;
    public static PubKey pbk;

    //User related attributes
    private HashMap<String, String> userNameToAddress;


    public static boolean verifyAdminCredentials() throws NoSuchAlgorithmException {
        System.out.println("Checking Admin credentials:");
        boolean goodUserName = false;
        boolean goodPassword = false;
        System.out.println("Please enter Admin User Name:");
        Scanner scanner = new Scanner(System.in);
        String UN = scanner.nextLine();
        System.out.println("Please enter Admin password:");
        scanner = new Scanner(System.in);
        String HP = Encryption.sha256(scanner.nextLine() + Salt);
        if (UN.equals(AdminUserName)) {
            goodUserName = true;
        }
        if (HP.equals(AdminHashedPassword)) {
            goodPassword = true;
        }

        return goodUserName && goodPassword;
    }


    public static void storeRSAKeys(PubKey publicKey, PrivKey privateKey){
        File File1 = new File(ServerKeysFileLocation);
        try {
            String publickey = publicKey.e.toString() + "@" + publicKey.n.toString();
            String privatekey = privateKey.d.toString() + "@" + privateKey.n.toString();
            String fileContent = publickey + "\n" + privatekey;
            Files.write(File1.toPath(),fileContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
//https://unix.stackexchange.com/questions/455013/how-to-create-a-file-that-only-sudo-can-read

        // try to launch config file
        String configFileReaderLocation = "./ServerConfig";
        File previousServerConfig = new File(configFileReaderLocation);
        if (previousServerConfig.exists()) {
            FileReader configFileReader = new FileReader(configFileReaderLocation);
            BufferedReader bufferedReader = new BufferedReader(configFileReader);

            // loads all the Server Information
            ServerAddress = bufferedReader.readLine();
            UserFileLocation = bufferedReader.readLine();
            RequestFileLocation = bufferedReader.readLine();
            FrozenAccountsFileLocation = bufferedReader.readLine();
            ServerKeysFileLocation = bufferedReader.readLine();
            Salt = bufferedReader.readLine();
            AdminUserName = bufferedReader.readLine();
            AdminHashedPassword = bufferedReader.readLine();

            // Checks that the server IP address is the same as the host IP address.
            InetAddress inetAddress = InetAddress.getLocalHost();
            String serverAddress = inetAddress.getHostAddress();
            if (!serverAddress.equals(ServerAddress)) {
                throw new Exception("IP address of the host is not the same as the Server Address. Cannot launch the server.");
            }

            //TODO should encrypt the keys with the admin password. AES
            // Checks that the server's keys are accessible.
            configFileReader.close();
            bufferedReader.close();
            File serverKeys = new File(ServerKeysFileLocation);
            if (!serverKeys.exists()) {
                throw new IOException("Server Keys not found, please add the ServerKey file back in the directory where the server is launched.");
            }
            // load the server public key from stored file
            FileReader fileReader = new FileReader(ServerKeysFileLocation);
            bufferedReader = new BufferedReader(fileReader);
            String[] publicKey = bufferedReader.readLine().split("@");
            String[] privateKey = bufferedReader.readLine().split("@");
            bufferedReader.close();
            fileReader.close();

            // convert string obtained above to big int
            //This is how they are stored:
            //String publickey = publicKey.e.toString() + "@" + publicKey.n.toString();
            //String privatekey = privateKey.d.toString() + "@" + privateKey.n.toString();
            pbk  = new PubKey(new BigInteger(publicKey[0]), new BigInteger(publicKey[1]));
            pvk = new PrivKey(new BigInteger((privateKey[0])), new BigInteger(privateKey[1]));
        } else {
            System.out.println("Creating new server:");
            // create a server
            InetAddress inetAddress = InetAddress.getLocalHost();
            ServerAddress = inetAddress.getHostAddress();
            System.out.println("ServerIPAddress: " + ServerAddress);
            System.out.println("Please enter Admin User Name:");
            Scanner scanner = new Scanner(System.in);
            AdminUserName = scanner.nextLine();
            AdminHashedPassword = "2";
            String ConfirmedPassword = "1";
            while (!AdminHashedPassword.equals(ConfirmedPassword)) {
                System.out.println("Please enter Admin password:");
                scanner = new Scanner(System.in);
                AdminHashedPassword = Encryption.sha256(scanner.nextLine() + Salt);
                System.out.println("Please confirm Admin password:");
                scanner = new Scanner(System.in);
                ConfirmedPassword = Encryption.sha256(scanner.nextLine() + Salt);
            }
            //TODO implement this
            //System.out.println("Do you wish to provide custom file location for files related to user information");
            //scanner = new Scanner(System.in);
            //AdminUserName = scanner.nextLine();
            //if ()
            //else
            UserFileLocation = "./Users";
            RequestFileLocation = "./Requests";
            FrozenAccountsFileLocation = "./FrozenAccounts";
            ServerKeysFileLocation = "./ServerKeys";
            System.out.println("Storing server information for further later use. (./ServerConfig)");
            FileWriter fileWriter = new FileWriter("./ServerConfig");
            fileWriter.append(ServerAddress + "\n");
            fileWriter.append(UserFileLocation + "\n");
            fileWriter.append(RequestFileLocation + "\n");
            fileWriter.append(FrozenAccountsFileLocation + "\n");
            fileWriter.append(ServerKeysFileLocation + "\n");
            fileWriter.append(Salt + "\n");
            fileWriter.append(AdminUserName + "\n");
            fileWriter.append(AdminHashedPassword + "\n");
            fileWriter.close();

            RSA akg = new RSA(128);
            pbk = akg.getPublicKey();
            pvk = akg.getPrivateKey();
            storeRSAKeys(pbk, pvk);

        }
        // ask for admin login
        if (verifyAdminCredentials()) {
            System.out.println("Credentials correct");
            System.out.println("please give the following information to clients:");
            System.out.println("Server Address: " + ServerAddress);
            System.out.println("Public key: " + pbk.toString());

            // launch threads that can handle user connections / interaction
            //ServerConnectionHandler
            ServerConnectionHandler server = new ServerConnectionHandler();
            server.run();

        }
        else {
            System.out.println("Login Failed.");
        }


    }
}
