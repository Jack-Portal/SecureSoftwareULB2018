package RSA;

import java.io.*;
import java.math.BigInteger;

public class test {
    public static void main(String[] args) throws IOException {

        try {

            RSA akg = new RSA(1024);
            PubKey publicKey = akg.getPublicKey();
            PrivKey privateKey = akg.getPrivateKey();

            BigInteger enc = null;
            BigInteger denc = null;

            String m = "hello";
            BigInteger tt = new BigInteger(m.getBytes());
            System.out.println("Message " + m);

            // enc
            enc = akg.encrypte(publicKey, tt);
            System.out.println("enc " + enc);
           
            String message = enc.toString();
            System.out.println(message.length());
            
            // dec
            denc = akg.decrypte(privateKey, new BigInteger(message));
            System.out.println("denc " + denc);
            System.out.println("Message " + new String(denc.toByteArray()));

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}