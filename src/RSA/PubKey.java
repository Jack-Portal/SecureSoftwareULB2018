package RSA;

import java.io.Serializable;
import java.math.BigInteger;

public class PubKey extends Key implements Serializable {

	public BigInteger e = getPart1();
	public BigInteger n = getPart2();

	public PubKey(BigInteger part1, BigInteger part2) {
		super(part1, part2);
	}

}
