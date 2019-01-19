package devFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class devDelUser {

    public static void main(String args[]) throws IOException {
//        File theFile = new File("./"+fileName);
//
        String state = "";
        try {
            state = "Opening Users file to print pending Users.";
            //opens the file
            FileReader UsersFile = new FileReader("./Users.txt");
            BufferedReader bufferedReader = new BufferedReader(UsersFile);

            List<String> userFile = new ArrayList<>();

            String line = "";
            System.out.println("All the current users:\n");
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

            System.out.println("Removed the users specified to the User file!");

            fileWriter.close();
            state = "DONE!";
        }
        catch (Exception e){
            System.out.println("ERROR while "+ state);
        }

//        boolean exists = theFile.exists();
//
//        boolean wantToOverwrite = false;
//
//        if (exists){
//            System.out.println("The file already exists, type 'y' to overwrite the file ?");
//            Scanner scanner = new Scanner(System.in);
//            wantToOverwrite = scanner.next().contains("y");
//        }
//
//        if (wantToOverwrite||!exists) {
//
//            Files.write(theFile.toPath(), byteFile);
//
//        }
//        else{
//            while (theFile.exists()){
//                System.out.println("What name do you want to give to this file? (type cancel to cancel)");
//                Scanner scanner = new Scanner(System.in);
//                String userInput = scanner.nextLine();
//                if (userInput.equals("cancel")){
//                    break;
//                }
//                theFile = new File("./" + userInput);
//            }
//            Files.write(theFile.toPath(), byteFile);
    }
}
