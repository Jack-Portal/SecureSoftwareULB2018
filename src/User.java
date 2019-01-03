import java.lang.Thread;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The User class
 */
public class User extends Thread{
    //TODO never send the user name, the server has to verify the user!

    // Information (without an 's' because it is already plural) about the user.
    public String userName;
    private String password;
    private String oneTimePassword;

    // Different Directories the User has access to.
    private String personalDirectory;
    private String[] sharedDirectories;

    // Files names of the files either fully or partially owned by the user.
    private String[] personalFiles;
    private String[] sharedfiles;

    /**
     * This is the initialisation function for a user.
     * It can be used by both the server and the client for authentication of users and file management.
     */
    public User(){
        //TODO add an initialisation function
        // prompt for their age
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: " );
        String userName = scanner.next();

        System.out.print("User Password: " );
        String password = scanner.next();

        System.out.print("OTP: ");
        String otp = scanner.next();

        this.userName = userName;
        this.password = password;
        //TODO deal with OPT


        //TODO login
        //TODO get one time password
        this.oneTimePassword = "";

        //TODO fetch file names and directories
        this.personalDirectory = null;
        this.sharedDirectories = null;
        this.personalFiles = null;
        this.sharedfiles = null;
    }

    /**
     * This function returns true if the user has the access rights to a directory.
     * @return boolean
     */
    private boolean checkAccessRights(){
        //TODO check access rights
        return false;
    }

    /**
     * This function is used to add a file to the server
     * @param file The file in byte array form
     */
    private void addFile(byte[] file){
        //TODO check access rights
        //TODO add the file to the NAS
    }

    /**
     * This function is used to download a file from the server and give it to the client.
     * @param fileName      The name of the file that needs to be downloaded.
     * @param fileLocation  The location of the file that needs to be downloaded.
     * @return The file as a byte array.
     */
    private byte[] downloadFile(String fileName, String fileLocation){
        byte[] file = {};
        //TODO check access rights
        //TODO download the file from the NAS
        return file;
    }

    /**
     * This function calls the download file function and provides a default file location in case it is needed
     * @param fileName The name of the file that has to be downloaded by the user.
     */
    private byte[] downloadFile(String fileName){
        //TODO check if this is needed.
        return downloadFile(fileName, "./");
    }

    /**
     * This function deletes a file at the user's request stored on the server.
     * @param fileName The file that needs to be deleted.
     */
    private void deleteFile(String fileName){
        //TODO check access rights
        //TODO delete the file in question
    }

    /**
     * This function gets a list of all the first layer files the user has and returns it for the client to return them.
     * @return the list of files the user has and their file type.
     */
    private HashMap<String, String> ls(){
        //TODO check access rights
        //TODO print a list of all the files and dictionaries
        return null;
    }

}
