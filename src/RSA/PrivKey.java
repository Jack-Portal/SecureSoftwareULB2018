package RSA;
import java.math.BigInteger;

public class PrivKey extends Key {

	public BigInteger d = getPart1();
	public BigInteger n = getPart2();

	public PrivKey(BigInteger part1, BigInteger part2) {
		super(part1, part2);
	}

}
