package RSA;
import java.math.BigInteger;
import java.util.*;
import java.util.Random;

public class RSA {
    /**
     * Bit length of each prime number.
     */
    int primeSize;
    /**
     * Two distinct large prime numbers p and q.
     */
    BigInteger p, q;

    /**
     * Modulus N.
     */
    BigInteger N;

    /**
     * r = ( p – 1 ) * ( q – 1 )
     */
    BigInteger r;

    /**
     * Public exponent E and Private exponent D
     */
    BigInteger E, D;

    String nt, dt, et;
    /**
     * Public key (E,N)
     */
    PubKey publickey;
    /**
     * Private Key (D,N)
     */
    PrivKey privatekey;

    /**
     * Constructor.
     *
     * @param primeSize Bit length of each prime number.
     */
    public RSA(int primeSize) {

        this.primeSize = primeSize;

        // Generate two distinct large prime numbers p and q.
        generatePrimeNumbers();

        // Generate Public and Private Keys.
        generatePublicPrivateKeys();

    }

    /**
     * Generate two distinct large prime numbers p and q.
     */
    public void generatePrimeNumbers() {
        p = new BigInteger(primeSize, 10, new Random());

        do {
            q = new BigInteger(primeSize, 10, new Random());
        } while (q.compareTo(p) == 0);
    }

    /**
     * Generate Public and Private Keys.
     */
    public void generatePublicPrivateKeys() {
        // N = p * q
        N = p.multiply(q);

        // r = ( p – 1 ) * ( q – 1 )
        r = p.subtract(BigInteger.valueOf(1));
        r = r.multiply(q.subtract(BigInteger.valueOf(1))); // (p-1)(q-1)

        // Choose E, coprime to and less than r
        do {
            E = new BigInteger(2 * primeSize, new Random());
        } while ((E.compareTo(r) != -1) || (E.gcd(r).compareTo(BigInteger.valueOf(1)) != 0));

        // Compute D, the inverse of E mod r
        D = E.modInverse(r);
        /**
         * Private Key
         */
        this.privatekey = new PrivKey(D, N);
        /**
         * Public key
         */
        this.publickey = new PubKey(E, N);

    }

    /**
     * 
     * @return Private Key (D,N)
     */
    public PrivKey getPrivateKey() {
        return this.privatekey;
    }

    /**
     * 
     * @return Public Key (E,N)
     */
    public PubKey getPublicKey() {
        return this.publickey;
    }

    public static BigInteger encrypte(PubKey publicKey, BigInteger m) {
        return m.modPow(publicKey.getPart1(), publicKey.getPart2());
    }

    public static BigInteger decrypte(PrivKey privatekey, BigInteger m) {

        return m.modPow(privatekey.getPart1(), privatekey.getPart2());
    }
}
