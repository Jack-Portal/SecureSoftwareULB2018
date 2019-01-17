package RSA;

import java.math.BigInteger;

public abstract class Key {

	private BigInteger part1;
	private BigInteger part2;

	/**
	 * RSA Key consisting of two separate parts
	 * @param part1
	 * @param part2
	 */
	public Key(BigInteger part1, BigInteger part2) {
		this.part1 = part1;
		this.part2 = part2;
	}
	

	public BigInteger getPart1() {
		return part1;
	}
	public BigInteger getPart2() {
		return part2;
	}
	
	@Override
	/**
	 * String representation of this RSA key. Can be used to store in a file.
	 */
	public String toString() {
		return "(" + part1 + "," + part2 + ")";
	}
}
