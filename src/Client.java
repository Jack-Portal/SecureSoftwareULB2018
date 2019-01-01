/**
 * The client class, command line interface allowing users to log in, download, upload and delete files stored
 * on a server.
 */
public class Client {

    public static String ServerAddress;

    private String userName;
    private String password;

    /**
     * Initialises the clients
     */
    private Client(){
        //TODO ask for server address
        this.userName = "";
        this.password = "";
    }

    /**
     * Logs a user inside the server
     */
    private void login(){
        //TODO login
    }

    /**
     * Logs a user out of the server
     */
    private void logout(){
        //TODO logout
    }

    /**
     * Loads a file into the client to give it to the user as a byte array.
     * @param fileLocation The location of the file in question.
     * @return returns the file as a byte array.
     */
    private byte[] loadFile(String fileLocation){
        //TODO load the file
        byte[] file = {};
        //TODO handle exceptions
        return file;
    }

    /**
     * saves a byte array as a file on the system.
     * @param fileName name of the file where the byte array will be saved.
     */
    private void saveFile(String fileName){
        //TODO save the file
    }

    /**
     * The main function of the client, is used to launch the client.
     * @param args default arguments of the main function passed from the command prompt.
     */
    public static void main(String[] args){
        //TODO ask for server address

        //TODO enter login
        //TODO enter pwd

        //TODO try create user
        //TODO catch bad login exception

        //TODO while not quit / ^C

        //TODO different commands:

        //TODO list personal directory
        //TODO Add files
        //TODO Download files
        //TODO Delete files

        //TODO list shared directories
        //TODO cooperate with other users
        //TODO list shared directories
        //TODO Add file
        //TODO Delete File
        //TODO Download file
    }
}
