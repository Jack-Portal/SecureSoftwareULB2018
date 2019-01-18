import RSA.PrivKey;
import RSA.PubKey;
import jdk.internal.util.xml.impl.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.Cipher;

public class Encryption {


    static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static PrivKey loadPrivateKey(String KeyFileLocation) throws IOException {
        // load the server public key from stored file
        FileReader fileReader = new FileReader(KeyFileLocation);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] publicKey = bufferedReader.readLine().split("@");
        String[] privateKey = bufferedReader.readLine().split("@");
        bufferedReader.close();
        fileReader.close();

        // convert string obtained above to big int
        //This is how they are stored:
        //String publickey = publicKey.e.toString() + "@" + publicKey.n.toString();
        //String privatekey = privateKey.d.toString() + "@" + privateKey.n.toString();
        //PubKey pbk  = new PubKey(new BigInteger(publicKey[0]), new BigInteger(publicKey[1]));
        PrivKey pvk = new PrivKey(new BigInteger((privateKey[0])), new BigInteger(privateKey[1]));
        return pvk;
    }

    //https://www.programcreek.com/java-api-examples/javax.crypto.Cipher



}
