package AES;

import java.io.*;

public class test {
    public static void main(String[] args) throws IOException {

        // Generate Key
        String key = AES.GenerateKey();
        System.out.println("key :" + key);

        // enc
        String m = "fichier1@fichier2@fichier3";
        byte cipher [] = m.getBytes();

        System.out.println("encrypt...");
        cipher = AES.encrypt(cipher, key.getBytes());
        System.out.println(new String(cipher));
        
        System.out.println("decrypt...");
        byte[] res = AES.decrypt(cipher, key.getBytes());
        System.out.println(new String(res));

        
        //File file = new File("onion.pdf"); // FIle name to Test

       /* try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileByte = fis.readAllBytes();

            System.out.println("encrypt...");
            cipher = AES.encrypt(fileByte, key.getBytes());

        } catch (Exception e) {
            System.out.println("file to Test not found !!");
        }

        //////////////////////////

        FileOutputStream fileOuputStream = null;
        try {

            System.out.println("decrypt...");
            byte[] res = AES.decrypt(cipher, key.getBytes());

            fileOuputStream = new FileOutputStream("test.pdf"); // cree new file
            fileOuputStream.write(res);
        } catch (IOException e) {

        } finally {
            fileOuputStream.close();
        }
	*/
        
    }
}
