

public class userInterruptHook extends Thread{

    private ClientConnectionHandler user;

    public userInterruptHook(ClientConnectionHandler user){
        this.user = user;
    }

    @Override
    public void run(){
        ClientCommandLineInterface.logout(this.user);
        ClientCommandLineInterface.clientPrint("\n EXIT STATUS: The User has been logged out.", "");
    }
}
