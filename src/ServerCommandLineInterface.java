import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        //TODO check user input and
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader("./Requests.txt");
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> userFile = new ArrayList<>();

            String line = "";
            ServerCommandLineInterface.serverPrint("All the pending requests:\n");
            while ((line = bufferedReader.readLine()) != null) {
                ServerCommandLineInterface.serverPrint(line);
                userFile.add(line);
            }
            UsersFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from Users to delete.";
            //asks the Admin for which Users to reject
            ServerCommandLineInterface.serverPrint("\n" + "Please enter the UserName of the User you wish to delete: (separated by spaces)");

            System.out.print("Server$ ");
            Scanner scanner = new Scanner(System.in);
            String userToAccept = scanner.nextLine();
            scanner.close();
            String[] usersToAccept = {};
            if (userToAccept.contains(" ")) {
                usersToAccept = userToAccept.split(" ");
            } else {
                usersToAccept = new String[]{userToAccept};
            }

            List<String> usersThatHaveNotBeenAccepted = new ArrayList<>();

            state = "Updating the users in local memory.";
            for (String userDetail : userFile) {
                boolean toAccept = false;
                for (String userName : usersToAccept) {
                    if (userDetail.startsWith(userName)) {
                        System.out.println(userDetail);
                        BufferedWriter writer =
                                Files.newBufferedWriter(Paths.get("./Users.txt"),
                                        StandardOpenOption.APPEND);
                        writer.append(userDetail);
                        writer.newLine();
                        writer.close();
                        toAccept = true;
                    }
                }
                if (!toAccept) {
                    usersThatHaveNotBeenAccepted.add(userDetail);
                }
            }

            state = "Checking new for new Users.";
            // check that there are no new Users
            UsersFile = new FileReader("./Requests.txt");
            bufferedReader = new BufferedReader(UsersFile);

            line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (!(userFile.contains(line))) {
                    usersThatHaveNotBeenAccepted.add(line);
                }
            }
            UsersFile.close();
            bufferedReader.close();


            state = "ReWriting the updated Users file.";
            // removes the "lines" from the userFile variable
            FileWriter fileWriter = new FileWriter("./Requests.txt");
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenAccepted) {
                System.out.println(User);
                fileWriter.append(User + "\n");
            }

            ServerCommandLineInterface.serverPrint("Accepted the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
        } catch (Exception e) {
            ServerCommandLineInterface.serverPrint("ERROR while " + state);
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
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader("./Requests.txt");
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> userFile = new ArrayList<>();

            String line = "";
            ServerCommandLineInterface.serverPrint("All the pending requests:\n");
            while ((line = bufferedReader.readLine()) != null) {
                ServerCommandLineInterface.serverPrint(line);
                userFile.add(line);
            }
            UsersFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from Users to delete.";
            //asks the Admin for which Users to reject
            ServerCommandLineInterface.serverPrint("\n" + "Please enter the UserName of the User you wish to delete: (separated by spaces)");

            System.out.print("Server$ ");
            Scanner scanner = new Scanner(System.in);
            String userToDelete = scanner.nextLine();
            scanner.close();
            String[] usersToDelete = {};
            if (userToDelete.contains(" ")) {
                usersToDelete = userToDelete.split(" ");
            } else {
                usersToDelete = new String[]{userToDelete};
            }

            List<String> usersThatHaveNotBeenDeleted = new ArrayList<>();

            state = "Updating the users in local memory.";
            for (String userDetail : userFile) {
                boolean toAccept = false;
                for (String userName : usersToDelete) {
                    if (userDetail.startsWith(userName)) {
                        System.out.println(userName);
                        toAccept = true;
                    }
                }
                if (!toAccept) {
                    System.out.println(userDetail);
                    usersThatHaveNotBeenDeleted.add(userDetail);
                }
            }

            state = "Checking new for new Users.";
            // check that there are no new Users
            UsersFile = new FileReader("./Requests.txt");
            bufferedReader = new BufferedReader(UsersFile);

            line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (!(userFile.contains(line))) {
                    usersThatHaveNotBeenDeleted.add(line);
                }
            }
            UsersFile.close();
            bufferedReader.close();


            state = "ReWriting the updated Users file.";
            // removes the "lines" from the userFile variable
            FileWriter fileWriter = new FileWriter("./Requests.txt");
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenDeleted) {
                System.out.println(User);
                fileWriter.append(User + "\n");
            }

            ServerCommandLineInterface.serverPrint("Refused the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
        } catch (Exception e) {
            ServerCommandLineInterface.serverPrint("ERROR while " + state);
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
        //TODO delete the files associated with the account.
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader("./Users.txt");
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> userFile = new ArrayList<>();

            String line = "";
            ServerCommandLineInterface.serverPrint("All the current users:\n");
            while ((line = bufferedReader.readLine()) != null) {
                ServerCommandLineInterface.serverPrint(line);
                userFile.add(line);
            }
            UsersFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from Users to delete.";
            //asks the Admin for which Users to reject
            ServerCommandLineInterface.serverPrint("\n" + "Please enter the UserName of the User you wish to delete: (separated by spaces)");

            System.out.print("Server$ ");
            Scanner scanner = new Scanner(System.in);
            String userToAccept = scanner.nextLine();
            scanner.close();
            String[] usersToDelete = {};
            if (userToAccept.contains(" ")){
                usersToDelete = userToAccept.split(" ");
            }
            else {
                usersToDelete = new String[]{userToAccept};
            }

            List<String> usersThatHaveNotBeenDeleted = new ArrayList<>();

            state = "Updating the users in local memory.";
            for (String userDetail : userFile){
                boolean toDelete = false;
                for(String userName : usersToDelete) {
                    if (userDetail.startsWith(userName)){
                        toDelete = true;
                    }
                }
                if (!toDelete){
                    usersThatHaveNotBeenDeleted.add(userDetail);
                }
            }

            state = "Checking new for new Users.";
            // check that there are no new Users
            UsersFile = new FileReader("./Users.txt");
            bufferedReader = new BufferedReader(UsersFile);

            line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (!(userFile.contains(line))) {
                    usersThatHaveNotBeenDeleted.add(line);
                }
            }
            UsersFile.close();
            bufferedReader.close();


            state = "ReWriting the updated Users file.";
            // removes the "lines" from the userFile variable
            FileWriter fileWriter = new FileWriter("./Users.txt");
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenDeleted) {
                System.out.println(User);
                fileWriter.append(User + "\n");
            }

            ServerCommandLineInterface.serverPrint("Removed the users specified to the User file!");

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
    static void serverPrint(String toPrint){for (String l : toPrint.split("\n")) System.out.println("....... " + l);}


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
