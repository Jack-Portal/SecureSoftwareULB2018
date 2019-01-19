package devFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class devSaveFile {


    public static void save(byte[] byteFile, String fileName) throws IOException {
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

    public static void main(String[] args) throws IOException {

        save("salut".getBytes(), "salut.txt");
//        byte[] fileBytes = Files.readAllBytes(path);
//
//        String byteString = fileBytes.toString();
//
//        System.out.println(byteString);


    }


}
