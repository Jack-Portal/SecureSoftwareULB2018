import java.io.File;
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
        this.user = null;
    }


    /**
     * Logs a user inside the server
     */
    private static void login(Client client){
        //TODO login
        client.user = new User(client.ServerAddress);
    }

    /**
     * Logs a user out of the server
     */
    public static String logout(User user){
        String userName = user.userName;
        //TODO send notification to the server that the user logged out.
        user = null;
        return userName;
    }

    /**
     * Loads a file into the client to give it to the user as a byte array.
     * @param fileLocation The location of the file in question.
     * @return returns the file as a byte array.
     */
    private static byte[] loadFile(String fileLocation) throws NoSuchFileException {

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
    private static void saveFile(byte[] byteFile, String fileName) throws IOException {
        File theFile = new File("./"+fileName);

        boolean exists = theFile.exists();

        boolean wantToOverwrite = false;

        if (exists){
            System.out.println("The file already exists, type 'y' to overwrite the file ?");
            Scanner scanner = new Scanner(System.in);
            wantToOverwrite = scanner.next().contains("y");
        }

        if (wantToOverwrite||!exists) {

            Files.write(theFile.toPath(), byteFile);

        }
        else{
            while (theFile.exists()){
                System.out.println("What name do you want to give to this file? (type cancel to cancel)");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();
                if (userInput.equals("cancel")){
                    break;
                }
                theFile = new File("./" + userInput);
            }
            Files.write(theFile.toPath(), byteFile);
        }
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
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String[] command = {};
                while (command.length < 1) {
                    System.out.print(user.userName + "$ ");
                    command = scanner.nextLine().split(" ");
                }
                try {
                    switch (command[0]) {
                        //commands for private files:
                        case "lsp":
                            // deal with ls
                            String lspResult = user.listPrivateDirectory();
                            clientPrint(lspResult, user.userName);
                            break;
                        case "adp": // $ adp <fileToUpload>
                            // Add files
                            try {
                                byte[] byteFile = loadFile(command[1]);
                                user.addFile(byteFile);
                            } catch (Exception e) {
                                //TODO handle the exception
                            }
                            break;
                        case "dwp": // $ dwp <fileToDownload>
                            // Download files
                            try {
                                byte[] fakeFile = user.downloadFile(command[1]);
                                saveFile(fakeFile, command[1]);
                            } catch (Exception e) {
                                //TODO handle the exception
                            }
                            break;
                        case "rmp": // $ rmp <fileToRemove>
                            // Delete files securely
                            user.deleteFile(command[1]);
                            break;

                        // commands for shared files:
                        case "lss":
                            // list shared directories
                            String lssResult = user.listSharedDirectories();
                            clientPrint(lssResult, user.userName);
                            break;
                        case "ads": // $ ads <fileToUpload> <otherFileOwners>
                            // cooperation
                            user.checkAccessRights(command[2]);
                            // Add file
                            try {
                                byte[] sharedByteFile = loadFile(command[1]);
                                user.addFile(sharedByteFile);
                            } catch (Exception e) {
                                //TODO handle the exception
                            }
                            break;
                        case "dws": // $ dws <fileToDownload>
                            // cooperation
                            user.checkAccessRights(command[1]);
                            // Download file
                            try {
                                byte[] fakeFile = user.downloadFile(command[1]);
                                saveFile(fakeFile, command[1]);
                            } catch (Exception e) {
                                //TODO handle the exception
                            }
                            break;
                        case "rms": // $ rms <fileToRemove>
                            // cooperation
                            user.checkAccessRights(command[1]);
                            // Delete File
                            user.deleteFile("aFile.txt");
                            break;

                        // other cases:
                        case "help":
                            clientPrint("Help", user.userName);
                        case "logout":
                            throw new LogOutException();
                        default:
                            clientPrint("Wrong use of the commands.", user.userName);
                    }
                } catch (IndexOutOfBoundsException i ){
                    clientPrint("Wrong use of the commands.", user.userName);
                    continue;
                }
            }
        }
        catch (LogOutException l){
            String userName = logout(user);
            clientPrint(userName + " is logging out...", userName);
            clientPrint("DONE!", userName);
        }
        catch ( Exception e ){
            String userName = logout(user);
            clientPrint("Error occurred, user " + userName + " logged out.", userName);
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
                Client client = new Client(serverAddress);

                try {
                    //TODO make sure none of the action done in this try statement can be half done if exception.
                    login(client);
                } catch (Exception e) {
                    System.out.println("Failed to log the user in, please check your credentials.");
                    System.out.println("If your credentials are correct, it might be an error with the server");
                }

                // If the application crashes, or the User interrupts the process, the program will first log the user out.
                Runtime.getRuntime().addShutdownHook(new userInterruptHook(client.user));

                // This function handles different commands:
                handleCommands(client.user);

                String username = logout(client.user);
                clientPrint("Session terminated", username);


            } catch (Exception e){
                //TODO catch bad login exception
                //TODO make exception that is caught when the server cannot be joined #404
            }
        }
    }
}
