package devFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class devLoadFile {


    public static void main(String[] args) throws IOException {

        Path path = Paths.get("./fileToLoadExample.txt");

        //Files.write(path, "You could put anything you want here and it would write it in the fileToLoadExample.txt file ".getBytes());

        byte[] fileBytes = Files.readAllBytes(path);

        String byteString = fileBytes.toString();

        System.out.println(byteString);


    }
}
