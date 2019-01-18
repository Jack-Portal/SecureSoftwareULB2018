import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.sql.Savepoint;
import java.util.Scanner;

import AES.AES;
import RSA.PrivKey;
import RSA.PubKey;
import RSA.RSA;

/**
 * The Client class that handles connections with the server on the demand of the Client Command line interface / user.
 */


public class ClientConnectionHandler {

	public final static int PORT_CLIENT = 5000;
	public final static int PORT_SERVEUR = 5001;

	public String userName;
	private String password;
	private String cookie;
	private PubKey publicKey;
	private PrivKey privateKey;
	private String AESKey;

	private String serverAddress;
	private PubKey PublicServerKey;


	public ClientConnectionHandler(String serverAdress){
		this.serverAddress = serverAdress;

		Scanner scanner = new Scanner(System.in);

		System.out.println("Are you a new user? y or n");
		String newUser = scanner.nextLine();
		if (newUser.equals("y")){
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
			RSA akg = new RSA(128);
			this.publicKey = akg.getPublicKey();
			this.privateKey = akg.getPrivateKey();
			storeRSAKeys();

			if (authentifyServer()) {
				register();
			}

		}else if (newUser.equals("n")){
			System.out.print("Username: " );
			String userName = scanner.next();

			System.out.print("User Password: " );
			String password = scanner.next();

			this.userName = userName;
			this.password = password;

			// generate Symmetric AES key
			this.AESKey = AES.GenerateKey();
			login();

		}
	}



	public  void storeRSAKeys(){
		File File1 = new File("./pubKey");
		File File2 = new File("./privKey");
		try {
			String publickey = publicKey.e.toString() + "@" + publicKey.n.toString();
			String privatekey = privateKey.d.toString() + "@" + privateKey.n.toString();
			Files.write(File1.toPath(),publickey.getBytes());
			Files.write(File2.toPath(),privatekey.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	public boolean authentifyServer() {

		boolean authentified = false;

		try {
			DatagramSocket ss = new DatagramSocket(PORT_CLIENT);
			// Récupérer la clé publique du Serveur dans le fichier


			// Vérifier l'authenticité du Serveur
			String message = "test_authentication";
			BigInteger tt = new BigInteger(message.getBytes());
			BigInteger enc = RSA.encrypte(this.PublicServerKey, tt);
			byte[] messageCrypte = enc.toByteArray();
			DatagramPacket dataSent = new DatagramPacket(messageCrypte,messageCrypte.length,InetAddress.getByName(this.serverAddress),PORT_SERVEUR);
			DatagramPacket dataReceived = new DatagramPacket(new byte[1024],1024);
			ss.send(dataSent);
			ss.receive(dataReceived);
			String dataString = new String(dataReceived.getData());
			if (dataString.equals(message)) {
				authentified = true;
				System.out.println("Server authentified");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authentified;




	}

	public void register() {

		try {
			DatagramSocket ss = new DatagramSocket(PORT_CLIENT);
			BigInteger tt = new BigInteger(password.getBytes());
			BigInteger enc = RSA.encrypte(this.PublicServerKey, tt);
			String publickey = publicKey.e.toString() + "@" + publicKey.n.toString();
			String keyRequest = "1" + "@" + userName + "@" + publickey + "@" + enc.toString();
			byte [] keyrequest = keyRequest.getBytes();
			DatagramPacket dataSent = new DatagramPacket(keyrequest,keyrequest.length,InetAddress.getByName(this.serverAddress),PORT_SERVEUR);
			DatagramPacket dataReceived = new DatagramPacket(new byte[1024],1024);
			ss.send(dataSent);

			ss.receive(dataReceived);
			String dataString = new String(dataReceived.getData());
			String[] data = dataString.split("@");
			//data[0] = code de retour
			if (data[0].equals("0")){
				System.out.println("Demande de register prise en compte");
			}
			else {
				System.out.println("Register failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public void login() {
		try {
			DatagramSocket ss = new DatagramSocket(PORT_CLIENT);
			String request = password + "@" + AESKey;
			BigInteger tt = new BigInteger(request.getBytes());
			BigInteger enc = RSA.encrypte(this.PublicServerKey, tt);
			String keyRequest = "2" + "@" + userName + "@" + enc.toString();
			byte [] keyrequest = keyRequest.getBytes();
			DatagramPacket dataSent = new DatagramPacket(keyrequest,keyrequest.length,InetAddress.getByName(this.serverAddress),PORT_SERVEUR);
			DatagramPacket dataReceived = new DatagramPacket(new byte[1024],1024);
			ss.send(dataSent);

			ss.receive(dataReceived);
			byte[] dataString = AES.decrypt(dataReceived.getData(),this.AESKey.getBytes());
			String[] data = new String(dataString).split("@");
			//data[0] = code de retour ; data[1] = cookie
			if (data[0].equals("0")){
				this.cookie = data[1];
				System.out.println("Login successful");
			}
			else {
				System.out.println("Login failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * This function is used to add a file to the server
	 * @param file The file in byte array form
	 */
	public void addFile(byte[] file, String shared){

		try {
			DatagramSocket ss = new DatagramSocket(PORT_CLIENT);
			String request = this.cookie + "@" + file.toString();
			byte[] enc = AES.encrypt(request.getBytes(), this.AESKey.getBytes());
			String keyRequest = "3" + "@" + userName + "@" + "shared" + "@" +  enc.toString();
			byte [] keyrequest = keyRequest.getBytes();
			DatagramPacket dataSent = new DatagramPacket(keyrequest,keyrequest.length,InetAddress.getByName(this.serverAddress),PORT_SERVEUR);
			DatagramPacket dataReceived = new DatagramPacket(new byte[1024],1024);
			ss.send(dataSent);

			ss.receive(dataReceived);
			byte[] dataString = AES.decrypt(dataReceived.getData(),this.AESKey.getBytes());
			String[] data = new String(dataString).split("@");
			//data[0] = code de retour;
			if (data[0].equals("0")){
				System.out.println("Fichier ajoute");
			}
			else {
				System.out.println("Fichier refuse");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This function calls the download file function and provides a default file location in case it is needed
	 * @param fileName The name of the file that has to be downloaded by the user.
	 */
	public void downloadFile(String fileName){

		try {
			DatagramSocket ss = new DatagramSocket(PORT_CLIENT);
			String request = this.cookie + "@" + fileName;
			byte[] enc = AES.encrypt(request.getBytes(), this.AESKey.getBytes());
			String keyRequest = "4" + "@" + userName + "@" +  enc.toString();
			byte [] keyrequest = keyRequest.getBytes();
			DatagramPacket dataSent = new DatagramPacket(keyrequest,keyrequest.length,InetAddress.getByName(this.serverAddress),PORT_SERVEUR);
			DatagramPacket dataReceived = new DatagramPacket(new byte[1024],1024);
			ss.send(dataSent);

			ss.receive(dataReceived);
			byte[] dataString = AES.decrypt(dataReceived.getData(),this.AESKey.getBytes());
			String[] data = new String(dataString).split("@");
			//data[0] = code de retour ; data[1] = fichier téléchargé
			if (data[0].equals("0")){
				System.out.println("Fichier téléchargé");
				// Stocker le fichier sur le répertoire courant
				ClientCommandLineInterface.saveFile(data[1].getBytes(), fileName);
			}
			else {
				System.out.println("Impossible de télécharger le fichier");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function deletes a file at the user's request stored on the server.
	 * @param fileName The file that needs to be deleted.
	 */
	public void deleteFile(String fileName){


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