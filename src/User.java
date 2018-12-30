import java.lang.Thread;

/**
 * The User class
 */
public class User extends Thread{
    //TODO never send the user name, the server has to verify the user!

    // Information (without an 's' because it is already plural) about the user.
    private String userName;
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
     *
     * @param userName The user name of the User
     * @param password The password of the User
     */
    public User(String userName, String password){
        //TODO add an initialisation function
        this.userName = userName;
        this.password = password;

        //TODO login
        //TODO get one time password
        this.oneTimePassword = "";

        //TODO fetch file names and directories
        this.personalDirectory = null;
        this.sharedDirectories = null;
        this.personalFiles = null;
        this.sharedfiles = null;
    }


}
