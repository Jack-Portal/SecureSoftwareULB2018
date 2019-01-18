import RSA.PrivKey;
import RSA.PubKey;

import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.net.InetAddress;

/**
 * The Server Command line Interface si launched to manage the server's user.
 * The following actions can be performed:
 *  accept : accepting users that have sent a request to join the network.
 *  refuse : refuse *same as above*
 *  delete : deletes a user and its files
 *  deactivate : moves the user to the group of frozen users (Does not delete files)
 *  activate : moves a frozen user back into the normal user file.
 */
public class ServerCommandLineInterface {

    //Server related attributes
    public static String ServerAddress;
    public static String UserFileLocation;
    public static String RequestFileLocation;
    public static String FrozenAccountsFileLocation;
    public static String ServerKeysFileLocation;
    public static String AdminUserName;
    public static String AdminHashedPassword;
    public static String Salt;


    /**
     * this function displays the pending requests and lets the admin accept them.
     */
    private static String[] accept(){
        //TODO check admin
        //TODO check user input and
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader(RequestFileLocation);
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
                    if (userDetail.startsWith(userName + " ")) {
                        BufferedWriter writer =
                                Files.newBufferedWriter(Paths.get(UserFileLocation),
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
            UsersFile = new FileReader(RequestFileLocation);
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
            FileWriter fileWriter = new FileWriter(RequestFileLocation);
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenAccepted) {
                fileWriter.append(User + "\n");
            }

            ServerCommandLineInterface.serverPrint("Accepted the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
            return usersToAccept;
        } catch (Exception e) {
            ServerCommandLineInterface.serverPrint("ERROR while " + state);
            return new String[] {};
        }
    }

    /**
     * This function displays the pending requests and lets the admin refuse them.
     */
    private static void refuse(){
        //TODO check admin
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader(RequestFileLocation);
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
                    if (userDetail.startsWith(userName + " ")) {
                        toAccept = true;
                    }
                }
                if (!toAccept) {
                    usersThatHaveNotBeenDeleted.add(userDetail);
                }
            }

            state = "Checking new for new Users.";
            // check that there are no new Users
            UsersFile = new FileReader(RequestFileLocation);
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
            FileWriter fileWriter = new FileWriter(RequestFileLocation);
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenDeleted) {
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
    private static String[] deleteUser(){
        //TODO check admin
        //TODO delete the files associated with the account.
        //TODO add space after startswith to make sure that only the right users are accepted / removed.........
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader(UserFileLocation);
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
                    if (userDetail.startsWith(userName + " ")){
                        toDelete = true;
                    }
                }
                if (!toDelete){
                    usersThatHaveNotBeenDeleted.add(userDetail);
                }
            }

            state = "Checking new for new Users.";
            // check that there are no new Users
            UsersFile = new FileReader(UserFileLocation);
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
            FileWriter fileWriter = new FileWriter(UserFileLocation);
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenDeleted) {
                fileWriter.append(User + "\n");
            }
            ServerCommandLineInterface.serverPrint("Removed the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
            return usersToDelete;
        }
        catch (Exception e){
            ServerCommandLineInterface.serverPrint("ERROR while "+ state);
            return new String[] {};
        }
    }

    /**
     * This function displays all the users and lets the admin deactivate them.
     */
    private static void deactivateUser(){
        //TODO check admin
        //TODO delete the files associated with the account.
        //TODO add space after startswith to make sure that only the right users are accepted / removed.........
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader(UserFileLocation);
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> userFile = new ArrayList<>();

            String line = "";
            ServerCommandLineInterface.serverPrint("All the active users:\n");
            while ((line = bufferedReader.readLine()) != null) {
                ServerCommandLineInterface.serverPrint(line);
                userFile.add(line);
            }
            UsersFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from Users to delete.";
            //asks the Admin for which Users to reject
            ServerCommandLineInterface.serverPrint("\n" + "Please enter the UserName of the User you wish to deactivate: (separated by spaces)");

            System.out.print("Server$ ");
            Scanner scanner = new Scanner(System.in);
            String userToAccept = scanner.nextLine();

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
                    if (userDetail.startsWith(userName + " ")) {
                        BufferedWriter writer =
                                Files.newBufferedWriter(Paths.get(FrozenAccountsFileLocation),
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
            UsersFile = new FileReader(UserFileLocation);
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
            FileWriter fileWriter = new FileWriter(UserFileLocation);
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenAccepted) {
                fileWriter.append(User + "\n");
            }

            ServerCommandLineInterface.serverPrint("Deactivated the users specified from the User file!");

            fileWriter.close();
            state = "DONE!";
        } catch (Exception e) {
            ServerCommandLineInterface.serverPrint("ERROR while " + state);
        }
    }

    /**
     * This function displays all the users and lets the admin deactivate them.
     */
    private static void activateUser(){
        //TODO check admin
        //TODO delete the files associated with the account.
        //TODO add space after startswith to make sure that only the right users are accepted / removed.........
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader(FrozenAccountsFileLocation);
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> userFile = new ArrayList<>();

            String line = "";
            ServerCommandLineInterface.serverPrint("All the deactivated users:\n");
            while ((line = bufferedReader.readLine()) != null) {
                ServerCommandLineInterface.serverPrint(line);
                userFile.add(line);
            }
            UsersFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from Users to delete.";
            //asks the Admin for which Users to reject
            ServerCommandLineInterface.serverPrint("\n" + "Please enter the UserName of the User you wish to activate: (separated by spaces)");

            System.out.print("Server$ ");
            Scanner scanner = new Scanner(System.in);
            String userToAccept = scanner.nextLine();

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
                    if (userDetail.startsWith(userName + " ")) {
                        BufferedWriter writer =
                                Files.newBufferedWriter(Paths.get(UserFileLocation),
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
            UsersFile = new FileReader(FrozenAccountsFileLocation);
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
            FileWriter fileWriter = new FileWriter(FrozenAccountsFileLocation);
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenAccepted) {
                fileWriter.append(User + "\n");
            }

            ServerCommandLineInterface.serverPrint("Activated the users specified from the User file!");

            fileWriter.close();
            state = "DONE!";
        } catch (Exception e) {
            ServerCommandLineInterface.serverPrint("ERROR while " + state);
        }
    }

    private static void createFolders(String[] userNames) throws IOException, NoSuchAlgorithmException {
        for (String userName : userNames){

            String hashedUserName = Encryption.sha256(userName);

            Path userPath = Paths.get("./"+hashedUserName);
            File folder = new File("./"+hashedUserName);

            if (Files.exists(userPath)){
                // This should never happen as the server should check that no duplicated user are accepted (before accepting the request
                serverPrint("This user name already exists!! "+ userName);
                serverPrint("This action might have corrupted the filesystem! ");
            }
            else {
                folder.mkdir();
            }

            FileWriter fileWriter = new FileWriter("./"+hashedUserName+"/filesInfo.txt");
            fileWriter.write("PersonalFiles:\n"+"SharedFiles:\n");
            fileWriter.close();
        }
    }

    /**
     * https://softwarecave.org/2018/03/24/delete-directory-with-contents-in-java/
     * @param path
     * @throws IOException
     */
    static void deleteDirectoryRecursion(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            }
        }
        Files.delete(path);
    }

    static void deleteDirectories(String[] userNames) throws NoSuchAlgorithmException, IOException {
        for (String userName : userNames){
            deleteDirectoryRecursion(Paths.get(Encryption.sha256(userName)));
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
        try {
            while (true) {
                String[] command = new String[]{};
                while (command.length < 1) {
                    System.out.print("Server$ ");
                    Scanner scanner = new Scanner(System.in);
                    command = scanner.nextLine().split(" ");
                }
                try {
                    switch (command[0]) {
                        case "accept":
                            String[] usersToAccept = accept();
                            createFolders(usersToAccept);
                            break;
                        case "refuse":
                            refuse();
                            break;
                        case "delete":
                            String[] usersToDelete = deleteUser();
                            deleteDirectories(usersToDelete);
                            break;
                        case "deactivate":
                            deactivateUser();
                            break;
                        case "activate":
                            activateUser();
                            break;
                            
                        // other cases:
                        case "help":
                            serverPrint("Help");
                            break;
                        default:
                            serverPrint("Wrong use of the commands.");
                    }
                } catch ( Exception e ) {
                    serverPrint("\nError Occurred in switch...");
                    continue;
                }
            }
        }
        catch ( Exception e ){
            serverPrint("\nError occurred outside the switch.");
        }
    }

    public static boolean verifyAdminCredentials() throws NoSuchAlgorithmException {
        System.out.println("Checking Admin credentials:");
        boolean goodUserName = false;
        boolean goodPassword = false;
        System.out.println("Please enter Admin User Name:");
        Scanner scanner = new Scanner(System.in);
        String UN = scanner.nextLine();
        System.out.println("Please enter Admin password:");
        scanner = new Scanner(System.in);
        String HP = Encryption.sha256(scanner.nextLine()+Salt);
        if (UN.equals(AdminUserName)){
            goodUserName = true;
        }
        if (HP.equals(AdminHashedPassword)){
            goodPassword = true;
        }

        return goodUserName && goodPassword;
    }

    /**
     * The main function of the server, is used to launch the server.
     * @param args default arguments of the main function passed from the command prompt.
     */
    public static void main(String[] args) throws Exception {
//https://unix.stackexchange.com/questions/455013/how-to-create-a-file-that-only-sudo-can-read

        String configFileReaderLocation = "./ServerConfig";
        File previousServerConfig = new File(configFileReaderLocation);
        FileReader configFileReader = new FileReader(configFileReaderLocation);
        BufferedReader bufferedReader = new BufferedReader(configFileReader);

        // loads all the Server Information
        ServerAddress = bufferedReader.readLine();
        UserFileLocation = bufferedReader.readLine();
        RequestFileLocation = bufferedReader.readLine();
        FrozenAccountsFileLocation = bufferedReader.readLine();
        ServerKeysFileLocation = bufferedReader.readLine();
        AdminUserName = bufferedReader.readLine();
        AdminHashedPassword = bufferedReader.readLine();
        Salt = bufferedReader.readLine();

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

        // ask for admin login
        if (verifyAdminCredentials()){
            System.out.println("Credentials correct");

            // launch admin interface where an admin (log in required) can accept / refuse new user and delete users.
            handleCommands();
        }
        else {
            System.out.println("Wrong credentials.");
        }


    }
}
