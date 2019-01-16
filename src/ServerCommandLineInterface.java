import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * The Server class, is used to launch a server where registered users can upload, download and delete files securely.
 */
public class ServerCommandLineInterface {

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
    private ServerCommandLineInterface(){
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

    /**
     * This function deletes a file
     * @param fileName name of the file to be deleted.
     */
    private void deleteFile(String fileName){
        //TODO SECURELY delete the file (replace all the bytes by 0 then delete it, or command to securely delete!
    }

    /**
     * this function displays the pending requests and lets the admin accept them.
     */
    private static void accept(){
        //TODO check admin
        //TODO load the file with all the pending requests
        //TODO print them
        //TODO accept the ones scanned
        String state = "";
        try {
            state = "Reading Request file.";
            FileReader requestFile = new FileReader("./Request.txt");
            BufferedReader bufferedReader = new BufferedReader(requestFile);

            List<String> rFile = new ArrayList<>();

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                serverPrint(line);
                rFile.add(line);
            }

            requestFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names to accept.";
            serverPrint("\n" + "Please enter the UserName of the User you wish to accept: (separated by spaces)");

            Scanner scanner = new Scanner(System.in);
            String[] usersToAccept = scanner.next().split(" ");

            state = "Adding new users to the User file.";
            FileWriter fileWriter = new FileWriter("./Users");
            for (String user : usersToAccept) {
                for (String userDetail : rFile) {
                    if (userDetail.contains(user)) {
                        fileWriter.append(userDetail);
                    }
                }
            }

            serverPrint("Added the users specified to the User file!");

            scanner.close();
            fileWriter.close();
            state = "DONE!";
        }
        catch (Exception e){
            serverPrint("ERROR while "+state);
        }
    }

    /**
     * This function displays the pending requests and lets the admin refuse them.
     */
    private static void refuse(){
        //TODO check admin
        //TODO load the file with all the pending requests
        //TODO print them
        //TODO refuse the ones scanned
        String state = "";
        try {
            state = "Opening request file to print pending requests.";
            //opens the file
            FileReader requestFile = new FileReader("./Request.txt");
            BufferedReader bufferedReader = new BufferedReader(requestFile);

            List<String> rFile = new ArrayList<>();

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                serverPrint(line);
                rFile.add(line);
            }
            requestFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from requests to reject.";
            //asks the Admin for which requests to reject
            serverPrint("\n" + "Please enter the UserName of the User you wish to refuse: (separated by spaces)");

            Scanner scanner = new Scanner(System.in);
            String[] usersToAccept = scanner.next().split(" ");
            scanner.close();

            for (String user : usersToAccept) {
                for (String userDetail : rFile) {
                    if (userDetail.contains(user)) {
                        rFile.remove(userDetail);
                    }
                }
            }

            state = "Checking new for new requests.";
            // check that there are no new request
            requestFile = new FileReader("./Request.txt");
            bufferedReader = new BufferedReader(requestFile);

            line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if ((!rFile.contains(line))) {
                    boolean toDelete = false;
                    for (String u : usersToAccept) {
                        if (!line.contains(u)) {
                            toDelete = true;
                        }
                    }
                    if (!toDelete) {
                        rFile.add(line);
                    }
                }
            }
            requestFile.close();
            bufferedReader.close();


            state = "ReWriting the updated request file.";
            // removes the "lines" from the rFile variable
            FileWriter fileWriter = new FileWriter("./Requests");
            //writes the new request list into the request file
            fileWriter.write("");
            for (String request : rFile) {
                fileWriter.append(request);
            }

            serverPrint("Added the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
        }
        catch (Exception e){
            serverPrint("ERROR while "+ state);
        }
    }

    /**
     * This function displays all the users and lets the admin delete them.
     */
    private void deleteuser(){
        //TODO check admin
        //TODO load the file with all existing users
        //TODO print them
        //TODO delete the ones scanned
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader("./Users.txt");
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> rFile = new ArrayList<>();

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                ServerCommandLineInterface.serverPrint(line);
                rFile.add(line);
            }
            UsersFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from Users to delete.";
            //asks the Admin for which Users to reject
            ServerCommandLineInterface.serverPrint("\n" + "Please enter the UserName of the User you wish to delete: (separated by spaces)");

            Scanner scanner = new Scanner(System.in);
            String[] usersToAccept = scanner.next().split(" ");
            scanner.close();

            for (String user : usersToAccept) {
                for (String userDetail : rFile) {
                    if (userDetail.contains(user)) {
                        rFile.remove(userDetail);
                    }
                }
            }

            state = "Checking new for new Users.";
            // check that there are no new Users
            UsersFile = new FileReader("./Users.txt");
            bufferedReader = new BufferedReader(UsersFile);

            line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if ((!rFile.contains(line))) {
                    boolean toDelete = false;
                    for (String u : usersToAccept) {
                        if (!line.contains(u)) {
                            toDelete = true;
                        }
                    }
                    if (!toDelete) {
                        rFile.add(line);
                    }
                }
            }
            UsersFile.close();
            bufferedReader.close();


            state = "ReWriting the updated Users file.";
            // removes the "lines" from the rFile variable
            FileWriter fileWriter = new FileWriter("./Users");
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String Users : rFile) {
                fileWriter.append(Users);
            }

            ServerCommandLineInterface.serverPrint("Added the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
        }
        catch (Exception e){
            ServerCommandLineInterface.serverPrint("ERROR while "+ state);
        }
    }

    /**
     * Prints a given string in the correct format for the server command line.
     * @param toPrint whatever needs to be printed
     */
    public static void serverPrint(String toPrint){for (String l : toPrint.split("\n")) System.out.println("....... " + l);}


    /**
     * This function handles the user input or the command line interface.
     */
    public static void handleCommands(){
        Scanner scanner = new Scanner(System.in);
        String[] command = {};
        while(command.length <2) {
            System.out.print("server$ ");
            command = scanner.nextLine().split(" ");
        }
        switch (command[0]) {
            // new accounts
            case "accept":
                //TODO print all the queued requests
                //TODO scanner and accept all the ones entered
                accept();
            case "refuse":
                //TODO print all the queued requests
                //TODO scanner and refuse all the ones entered
                refuse();

            case "delete":
                //TODO print all the accounts existing
                //TODO scan for all the accounts that have to be deleted
                //TODO delete everything from these accounts.

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

        //TODO try to launch config file

        //TODO if it fails
        //TODO ask for server details
        //TODO ask for admin details
        //TODO launch the server

        //TODO ask for admin login

        //TODO launch threads that can handle user connections / interaction

        //TODO while:
        //TODO launch admin interface where an admin (log in required) can accept / refuse new user and delete users.
    }
}
