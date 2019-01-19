package devFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class devCreateFolder {

    static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
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


    public static void main(String args[]) throws NoSuchAlgorithmException, IOException {
        String userName = "JeanJacques";
        String hashedUserName = sha256(userName);
        System.out.println(hashedUserName);

        Path userPath = Paths.get("./"+hashedUserName);
        File folder = new File("./"+hashedUserName);

        if (Files.exists(userPath)){
            System.out.println("This user name already exists");
        }
        else {
            folder.mkdir();
        }

        FileWriter fileWriter = new FileWriter("./"+hashedUserName+"/filesInfo.txt");
        fileWriter.write("PersonalFiles:\n"+"SharedFiles:\n");
        fileWriter.close();

        deleteDirectoryRecursion(userPath);

    }
}
