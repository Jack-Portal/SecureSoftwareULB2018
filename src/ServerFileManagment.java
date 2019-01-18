import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerFileManagment {

    public static Path getHashPath(String userName, String fileName) throws NoSuchAlgorithmException {
        return Paths.get(Encryption.sha256(userName) + "/" + Encryption.sha256(fileName));
    }

    public void storeFile(String userName, String fileName, byte[] byteFile) throws NoSuchAlgorithmException, IOException {
        //TODO The username should preferably not be given by the user when, instead it should
        // be deducted by the server making sure that no one can pretend to be someone they are not.
        Path userFolder = getHashPath(userName, fileName);
        Files.write(new File(userFolder.toString()).toPath(), byteFile);
    }

    public byte[] loadFile(String userName, String fileName) throws NoSuchAlgorithmException {
        //TODO The username should preferably not be given by the user when, instead it should
        // be deducted by the server making sure that no one can pretend to be someone they are not.
        Path userFolder = getHashPath(userName, fileName);
        return null;
    }



}
