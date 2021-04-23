/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public class SOMBitcodeRunner {
	public SOMBitcodeRunner(int n) {
		long bitCnt=(long) Math.pow(2, n);
		long byteCnt=bitCnt/8;
		byte[] memSpace = new byte[(int) byteCnt];
		setBitsUnsigned(1,6,n,memSpace);
	}

	public static byte[] setBitsUnsigned(int lowerBound, int upperBound, int value, byte[] memSpace) {
		int bits=upperBound-lowerBound;
		for (int i = 0; i < bits; i++) {
			boolean bitValue=value%(Math.pow(2, bits-i-1))==0?false:true;
			memSpace=setBit(lowerBound+i,bitValue,memSpace);
		}
		return memSpace;
	}

	public static byte[] setBit(int address, boolean bitValue, byte[] memSpace) {
		int byteAddress=address/8;
		int offset=7-address%8;
		byte bite=memSpace[byteAddress];
		byte bitmask=(byte) (1<<offset);
		if(!bitValue) {
			bitmask=(byte) ~bitmask;
			bite=(byte) (bite&bitmask);
		}else {
			bite=(byte) (bite|bitmask);
		}
		memSpace[byteAddress]=bite;
		return memSpace;
	}
}
