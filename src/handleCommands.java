import java.util.Scanner;



public class handleCommands {

    public static void clientPrint(String toPrint){for (String l : toPrint.split("\n")) System.out.println(".. " + l);}

    public static void main(String args[]){
        while (true){
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("\n>> ");
//            String serverAddress = scanner.nextLine();
//            clientPrint(serverAddress);


            Scanner scanner = new Scanner(System.in);
            String[] command = {};
            System.out.println(command.length);
            while(command.length < 2) {
                System.out.print(">> ");
                command = scanner.nextLine().split(" ");
            }
            switch (command[0]) {
                //commands for private files:
                case "lsp":
                    //TODO deal with ls
                    break;
                case "-ap":
                    //TODO Add files
                    break;
                case "-dp":
                    //TODO Download files
                    break;
                case "-rp":
                    //TODO Delete files
                    break;

                // commands for shared files:
                case "lss":
                    //TODO list shared directories
                    break;
                case "-as":
                    //TODO Add file
                    break;
                case "-ds":
                    //TODO Download file
                    break;
                case "-rs":
                    //TODO Delete File
                    break;

                // other cases:
                case "help":
                    clientPrint("Help");
                default:
                    clientPrint("Wrong use of the commands.");
            }
        }
    }
}
