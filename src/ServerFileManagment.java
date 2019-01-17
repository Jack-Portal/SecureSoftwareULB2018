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

    public void storeFile(String userName, String fileName, byte[] byteFile) throws NoSuchAlgorithmException {
        Path userFolder = getHashPath(userName, fileName);
    }

    public byte[] loadFile(String userName, String fileName) throws NoSuchAlgorithmException {
        Path userFolder = getHashPath(userName, fileName);
        return null;
    }



}
