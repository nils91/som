/**
 * 
 */
package de.dralle.som;

import java.util.List;

/**
 * @author Nils Dralle
 *
 */
public class SOMBitcodeRunner {
	private byte[] memSpace;

	public byte[] getMemSpace() {
		return memSpace;
	}

	public SOMBitcodeRunner(int n,int startAddress) {
		long bitCnt=(long) Math.pow(2, n);
		long byteCnt=bitCnt/8;
		byte[] memSpace = new byte[(int) byteCnt];
		memSpace=setBitsUnsignedBounds(1,6,n-4,memSpace);
		memSpace=setBitsUnsignedBounds(6, 6+n, startAddress, memSpace);
		this.memSpace=memSpace;
	}
	public SOMBitcodeRunner(byte[] memSpace) {
		this.memSpace=memSpace;
	}
	public SOMBitcodeRunner(boolean[] bits) {
		int bitCnt = bits.length;
		int byteCnt = bitCnt/8;
		if(bitCnt%8!=0) {
			byteCnt++;
		}
		byte[] memSpace = new byte[(int) byteCnt];
		for (int i = 0; i < bits.length; i++) {
			boolean b = bits[i];
			memSpace=setBit(i, b, memSpace);
		}
		this.memSpace=memSpace;
	}
	public SOMBitcodeRunner(List<Boolean> bits) {
		int bitCnt = bits.size();
		int byteCnt = bitCnt/8;
		if(bitCnt%8!=0) {
			byteCnt++;
		}
		byte[] memSpace = new byte[(int) byteCnt];
		for (int i = 0; i < bits.size(); i++) {
			boolean b = bits.get(i);
			memSpace=setBit(i, b, memSpace);
		}
		this.memSpace=memSpace;
	}
	public SOMBitcodeRunner(String bits) {
		bits=bits.replaceAll("[^0-1]", "");
		int bitCnt = bits.length();
		int byteCnt = bitCnt/8;
		if(bitCnt%8!=0) {
			byteCnt++;
		}
		byte[] memSpace = new byte[(int) byteCnt];
		for (int i = 0; i < bits.length(); i++) {
			boolean b = bits.charAt(i)!='0';
			memSpace=setBit(i, b, memSpace);
		}
		this.memSpace=memSpace;
	}
	public static byte[] setBitsUnsignedBounds(int lowerBound, int upperBound, int value, byte[] memSpace) {
		int bits=upperBound-lowerBound;
		for (int i = 0; i < bits; i++) {
			boolean bitValue=value-Math.pow(2, bits-i-1)>=0?true:false;
			if(bitValue) {
				value-=Math.pow(2, bits-i-1);
			}
			memSpace=setBit(lowerBound+i,bitValue,memSpace);
		}
		return memSpace;
	}
	
	public static byte[] setBitsUnsigned(int lowerBound, int n, int value, byte[] memSpace) {
		return setBitsUnsignedBounds(lowerBound, lowerBound+n, value, memSpace);
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
	public static boolean getBit(int address, byte[] memSpace) {
		int byteAddress=address/8;
		int offset=7-address%8;
		byte bite=memSpace[byteAddress];
		byte bitmask=(byte) (1<<offset);
		boolean bitValue=(bite&bitmask)!=0;		
		return bitValue;
	}
	public static int getBitsUnsignedBounds(int lowerBound, int upperBound, byte[] memSpace) {
		int bits=upperBound-lowerBound;
		int returnValue=0;
		for (int i = 0; i < bits; i++) {
			if(getBit(lowerBound+i, memSpace)) {
				int bitmask=1<<(lowerBound+i);
				returnValue=returnValue|bitmask;
			}
		}
		return returnValue;
	}
	
	public static int getBitsUnsigned(int lowerBound, int n, byte[] memSpace) {
		return getBitsUnsignedBounds(lowerBound, lowerBound+n, memSpace);
	}
}
