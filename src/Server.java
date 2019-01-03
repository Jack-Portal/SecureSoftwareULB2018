import java.util.HashMap;
import java.util.Scanner;

/**
 * The Server class, is used to launch a server where registered users can upload, download and delete files securely.
 */
public class Server {

    //Server related attributes
    public static String ServerAdress;

    //User related attributes
    private HashMap<String, String> userToPassword;
    private HashMap<String, String> userToOtherAuthenticationParameter;     //Might need to add more of these!

    //Directories related attributes
    private HashMap<String, String> userToPersonalDirectoryLocation;
    private HashMap<String[], String> usersToSharedDirectoryLocation;

    /**
     * Initialises the server
     */
    private Server(){
        //TODO ask for server address
        this.userToPassword = null;
    }

    /**
     * Logs a user inside the server
     */
    private void login(){
        //TODO login
    }

    /**
     * Logs a user out of the server
     */
    private void logout(){
        //TODO logout
    }

    private boolean accessSharedRessources(){
        //TODO wait for other users to cooperate.
        return false;
    }

    /**
     * Loads a file into the client to give it to the user as a byte array.
     * @param fileLocation The location of the file in question.
     * @return returns the file as a byte array.
     */
    private byte[] loadFile(String fileLocation){
        //TODO load the file
        byte[] file = {};
        //TODO handle exceptions
        return file;
    }

    /**
     * saves a byte array as a file on the system.
     * @param fileName name of the file where the byte array will be saved.
     */
    private void saveFile(String fileName){
        //TODO save the file
    }

    private void deleteFile(String fileName){
        //TODO SECURELY delete the file (replace all the bytes by 0 then delete it, or command to securely delete!
    }

    /**
     * Prints a given string in the correct format for the server command line.
     * @param toPrint whatever needs to be printed
     */
    public static void serverPrint(String toPrint){for (String l : toPrint.split("\n")) System.out.println("....... " + l);}


    public static void handleCommands(){
        Scanner scanner = new Scanner(System.in);
        String[] command = {};
        while(command.length <2) {
            System.out.print("server$ ");
            command = scanner.nextLine().split(" ");
        }
        switch (command[0]) {
            //commands for private files:
            // TODO all the p commands
            // commands for shared files:
            //TODO all the s commands

            // other cases:
            case "help":
                serverPrint("Help");
            default:
                serverPrint("Wrong use of the commands.");
        }
    }

    /**
     * The main function of the server, is used to launch the server.
     * @param args default arguments of the main function passed from the command prompt.
     */
    public static void main(String[] args){

    }
}
