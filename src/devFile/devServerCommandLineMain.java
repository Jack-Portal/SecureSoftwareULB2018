package devFile;

import java.io.File;

public class devServerCommandLineMain {
    public static void main(String args[]){
        //https://unix.stackexchange.com/questions/455013/how-to-create-a-file-that-only-sudo-can-read

        //TODO try to launch config file
        if (new File("./ServerConfig").exists()){
            //TODO load all the file information
        }else{
            //TODO create a server / files and permissions
        }
        //TODO ask for admin login

        //TODO launch the server
        //using all the config variables

        //TODO launch threads that can handle user connections / interaction
        //ServerConnectionHandler

        //TODO while:
        //TODO launch admin interface where an admin (log in required) can accept / refuse new user and delete users.

    }
}
