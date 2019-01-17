import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

import AES.AES;
import RSA.PrivKey;
import RSA.PubKey;
import RSA.RSA;

/**
 * The User class
 */


public class ClientConnectionHandler {
    //TODO never send the user name, the server has to verify the user!

	public final static int PORT_CLIENT = 5000;
	public final static int PORT_SERVEUR = 5001;

    // Information (without an 's' because it is already plural) about the user.
    public String userName;
    private String password;
    private String oneTimePassword;
    private String serverAddress;

    // Different Directories the User has access to.
    private String personalDirectory;
    private String[] sharedDirectories;

    // Files names of the files either fully or partially owned by the user.
    private String[] personalFiles;
    private String[] sharedfiles;

    /**
     * This is the initialisation function for a user.
     * It can be used by both the server and the client for authentication of users and file management.
     */
    public ClientConnectionHandler(String serverAdress){
        this.serverAddress = serverAdress;

        //TODO add an initialisation function
        Scanner scanner = new Scanner(System.in);

        System.out.println("Are you a new user? y or n");
        String newUser = scanner.nextLine();
        if (newUser.equals("y")){
            //TODO communicate with the server
            System.out.print("Username: " );
            String userName = scanner.next();

            String password;
            String confirmation;
            do {
                System.out.print("User Password: " );
                password = scanner.next();

                System.out.print("Confirm Password: " );
                confirmation = scanner.next();
            } while (!password.equals(confirmation));

            this.userName = userName;
            this.password = password;

            // Generating new pair of keys
            // need to Store keys in a file on the client
            RSA akg = new RSA(1024);
            PubKey publicKey = akg.getPublicKey();
            PrivKey privateKey = akg.getPrivateKey();
            storeRSAKeys(publicKey,privateKey);

            publicKeyServer = loadServerRSAKey();
            authentifyServer("test_authentify",publicKeyServer);
            register(userName,password,publicKey);

        }else if (newUser.equals("n")){
            System.out.print("Username: " );
            String userName = scanner.next();

            System.out.print("User Password: " );
            String password = scanner.next();

            this.userName = userName;
            this.password = password;

            // generate Symmetric AES key
            String AESKey = AES.GenerateKey();
            login(userName,password,AESKey);

        }
    }



    public static void storeRSAKeys(PubKey publicKey,PrivKey privateKey){

    }

    public static PubKey loadServerRSAKey() {

    	return publicKeyServer
    }


    public boolean authentifyServer(String message, PubKey publicKeyServer) {

		try {
			DatagramSocket ss = new DatagramSocket(PORT_CLIENT);


            BigInteger tt = new BigInteger(message.getBytes());
            BigInteger enc = RSA.encrypte(publicKeyServer, tt);
            byte[] messageCrypte = enc.toByteArray();
			DatagramPacket dataSent = new DatagramPacket(messageCrypte,messageCrypte.length,InetAddress.getByName(this.serverAddress),PORT_SERVEUR);
			DatagramPacket dataReceived = new DatagramPacket(new byte[1024],1024);
			ss.send(dataSent);
			ss.receive(dataReceived);
		} catch (Exception e) {
			e.printStackTrace();
		}




    }

    public void register(String userName, String password, String publicKey) {


    }


    public void login(String username, String password, String AESKey) {

    }

    /**
     * This function returns true if the user has the access rights to a directory.
     * @return boolean
     */
    public boolean checkAccessRights(String fileOwners){
        //TODO check access rights
        return false;
    }

    /**
     * This function is used to add a file to the server
     * @param file The file in byte array form
     */
    public void addFile(byte[] file){
        //TODO check access rights
        //TODO add the file to the NAS
    }

    /**
     * This function is used to download a file from the server and give it to the client.
     * @param fileName      The name of the file that needs to be downloaded.
     * @param fileLocation  The location of the file that needs to be downloaded.
     * @return The file as a byte array.
     */
    public byte[] downloadFile(String fileName, String fileLocation){
        byte[] file = {};
        //TODO check access rights

        //TODO download the file from the NAS

        byte[] fakeFile = "this is not a file".getBytes();

        return fakeFile;
    }

    /**
     * This function calls the download file function and provides a default file location in case it is needed
     * @param fileName The name of the file that has to be downloaded by the user.
     */
    public byte[] downloadFile(String fileName){
        //TODO check if this is needed.
        return downloadFile(fileName, "./");
    }

    /**
     * This function deletes a file at the user's request stored on the server.
     * @param fileName The file that needs to be deleted.
     */
    public void deleteFile(String fileName){
        //TODO check access rights
        //TODO delete the file in question
    }

    /**
     * This function gets a list of all the first layer files the user has and returns it for the client to return them.
     * @return the list of files the user has and their file type.
     */
    public String listPrivateDirectory(){
        //TODO check access rights
        //TODO print a list of all the files and dictionaries
        String lssResult = "personalDir:\n  aFile.txt\n  anotherFile.pdf";
        return lssResult;
    }

    /**
     * This function gets a list of all the first layer files the user has and returns it for the client to return them.
     * @return the list of files the user has and their file type.
     */
    public String listSharedDirectories(){
        //TODO check access rights
        //TODO print a list of all the files and dictionaries
        String lssResult = "personalDir:\n  aFile.txt\n  anotherFile.pdf";
        return lssResult;
    }
}
