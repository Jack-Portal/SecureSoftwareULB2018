package devFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class devServerAccept {

    public static void main(String args[]) throws IOException {
//        File theFile = new File("./"+fileName);
//
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader("./Requests");
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> userFile = new ArrayList<>();

            String line = "";
            System.out.println("All the pending requests:\n");
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                userFile.add(line);
            }
            UsersFile.close();
            bufferedReader.close();

            state = "Asking the Admin for user names from Users to delete.";
            //asks the Admin for which Users to reject
            System.out.println("\n" + "Please enter the UserName of the User you wish to delete: (separated by spaces)");

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
            UsersFile = new FileReader("./Requests");
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
            FileWriter fileWriter = new FileWriter("./Requests");
            //writes the new Users list into the Users file
            fileWriter.write("");
            for (String User : usersThatHaveNotBeenAccepted) {
                System.out.println(User);
                fileWriter.append(User + "\n");
            }

            System.out.println("Accepted the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
        } catch (Exception e) {
            System.out.println("ERROR while " + state);
        }
    }
}
