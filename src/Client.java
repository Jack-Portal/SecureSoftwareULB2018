import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The client class, command line interface allowing users to log in, download, upload and delete files stored
 * on a server.
 */
public class Client {

    public static String ServerAddress;

    private User user;

    /**
     * Initialises the clients
     */
    public Client(String serverAddress){
        this.ServerAddress = serverAddress;
        this.user = new User();
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

    /**
     * Loads a file into the client to give it to the user as a byte array.
     * @param fileLocation The location of the file in question.
     * @return returns the file as a byte array.
     */
    private byte[] loadFile(String fileLocation) throws NoSuchFileException {

        //load the file
        byte[] file = {};
        try{
            file = Files.readAllBytes(Paths.get(fileLocation));
        }

        // handle exceptions
        catch (IOException e) {
            throw new NoSuchFileException(fileLocation + ": This file could not be loaded by the Client");
        }
        return file;
    }

    /**
     * saves a byte array as a file on the system.
     * @param fileName name of the file where the byte array will be saved.
     */
    private void saveFile(String fileName){
        //TODO save the file
    }

    /**
     * Prints a given string in the correct format for the client command line.
     * @param toPrint whatever needs to be printed, usually a response from the server.
     * @param userName the name of the user that is using the client
     */
    public static void clientPrint(String toPrint, String userName){
        String dots = new String(new char[userName.length()+1]).replace('\0', '.');
        for (String l : toPrint.split("\n")) System.out.println(dots+ " " + l);
    }


    public static void handleCommands(User user){
        Scanner scanner = new Scanner(System.in);
        String[] command = {};
        while(command.length <2) {
            System.out.print(">> ");
            command = scanner.nextLine().split(" ");
        }
        switch (command[0]) {
            //commands for private files:
            case "lsp":
                //TODO deal with ls
                break;
            case "adp":
                //TODO Add files
                break;
            case "dwp":
                //TODO Download files
                break;
            case "rmp":
                //TODO Delete files
                break;

            // commands for shared files:
            case "lss":
                //TODO list shared directories
                break;
            case "ads":
                //TODO Add file
                break;
            case "dws":
                //TODO Download file
                break;
            case "rms":
                //TODO Delete File
                break;

            // other cases:
            case "help":
                clientPrint("Help", user.userName);
            default:
                clientPrint("Wrong use of the commands.", user.userName);
        }
    }


    /**
     * The main function of the client, is used to launch the client.
     * @param args default arguments of the main function passed from the command prompt.
     */
    public static void main(String[] args){
        // ask for server address
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);

                //  prompt for the user's name
                System.out.print("Please provide the following details ");
                System.out.print("Server address: ");
                String serverAddress = scanner.next();
                //TODO add address verification function and probably a while loop

                // try create user
                Client client = null;
                try {
                    //TODO make sure none of the action done in this try statement can be half done if exception.
                    client = new Client(serverAddress);
                    System.out.println("Successfully logged in!");
                } catch (Exception e) {
                    System.out.println("Failed to log the user in, please check your credentials.");
                    System.out.println("If your credentials are correct, it might be an error with the server");
                }

                //TODO different commands:
                handleCommands(client.user);

                //TODO list personal directory
                //TODO Add files
                //TODO Download files
                //TODO Delete files

                //TODO list shared directories
                //TODO cooperate with other users
                //TODO list shared directories
                //TODO Add file
                //TODO Delete File
                //TODO Download file

            } catch (Exception e){
                //TODO catch bad login exception
                //TODO make exception that is caught when the server cannot be joined #404

                //TODO while not quit / ^C

            }
        }
    }
}
