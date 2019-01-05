

public class userInterruptHook extends Thread{

    private User user;

    public userInterruptHook(User user){
        this.user = user;
    }

    @Override
    public void run(){
        Client.logout(this.user);
        Client.clientPrint("\n EXIT STATUS: The User has been logged out.", "");
    }
}
