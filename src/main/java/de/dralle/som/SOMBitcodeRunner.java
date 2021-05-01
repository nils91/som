/**
 * 
 */
package de.dralle.som;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.writehooks.IWriteHook;

/**
 * @author Nils Dralle
 *
 */
public class SOMBitcodeRunner {
	private static final int ACC_ADDRESS = 0;
	private static final int START_ADDRESS_START = 6;
	private static final int ADDRESS_SIZE_END = 5;
	private static final int ADDRESS_SIZE_START = 1;
	private static final int ADDRESS_SIZE_OFFSET = 4;
	private byte[] memSpace;
	private List<IWriteHook> writeHooks;
	private int selectedWriteHook = 0;

	public void addWriteHook(IWriteHook writeHook) {
		if (writeHooks == null) {
			writeHooks = new ArrayList<>();
		}
		writeHooks.add(writeHook);
	}

	private IWriteHook getSelectedWriteHook() {
		if (writeHooks.size() > 1 && selectedWriteHook > -1) {
			return writeHooks.get(selectedWriteHook);
		}
		return null;
	}

	public byte[] getMemSpace() {
		return memSpace;
	}

	public static boolean isWriteHookEnabled(byte[] memSpace) {
		int addressSizeBits = getN(memSpace);
		int startAddress = getStartAddress(memSpace);
		return (START_ADDRESS_START + addressSizeBits) < startAddress;
	}

	public boolean isWriteHookEnabled() {
		return isWriteHookEnabled(memSpace);
	}

	public static int getWriteHookTriggerAddress(byte[] memSpace) {
		int addressSizeBits = getN(memSpace);
		return START_ADDRESS_START + addressSizeBits;
	}

	public static int getWriteHookSelectAddress(byte[] memSpace) {
		return getWriteHookTriggerAddress(memSpace) + 1;
	}

	public static boolean isWriteHookReadmode(byte[] memSpace) {
		return !getBit(getWriteHookTriggerAddress(memSpace), memSpace);
	}

	public static boolean isWriteHookWritemode(byte[] memSpace) {
		return getBit(getWriteHookTriggerAddress(memSpace), memSpace);

	}

	public static boolean isWriteHookSelected(byte[] memSpace) {
		return getBit(getWriteHookSelectAddress(memSpace), memSpace);

	}

	public static boolean isWriteHookSwitchmodeSelected(byte[] memSpace) {
		return !getBit(getWriteHookSelectAddress(memSpace), memSpace);
	}

	public int getWriteHookTriggerAddress() {
		return getWriteHookTriggerAddress(memSpace);
	}

	public int getWriteHookSelectAddress() {
		return getWriteHookSelectAddress(memSpace);
	}

	public boolean isWriteHookReadmode() {
		return isWriteHookReadmode(memSpace);
	}

	public boolean isWriteHookWritemode() {
		return isWriteHookWritemode(memSpace);
	}

	public boolean isWriteHookSelected() {
		return isWriteHookSelected(memSpace);

	}

	public boolean isWriteHookSwitchmodeSelected() {
		return isWriteHookSwitchmodeSelected(memSpace);
	}

	public static int getN(byte[] memSpace) {
		int addressSizeBits = getBitsUnsignedBounds(ADDRESS_SIZE_START, ADDRESS_SIZE_END + 1, memSpace)
				+ ADDRESS_SIZE_OFFSET;
		return addressSizeBits;
	}

	public int getN() {
		return getN(memSpace);
	}

	public static int getStartAddress(byte[] memSpace) {
		int addressSizeBits = getN(memSpace);
		int startAddress = getBitsUnsignedBounds(START_ADDRESS_START, START_ADDRESS_START + addressSizeBits, memSpace);
		return startAddress;
	}

	public int getStartAddress() {
		return getStartAddress(memSpace);
	}

	public SOMBitcodeRunner(int n, int startAddress) {
		byte[] memSpace = initMemspaceFromAddressSizeAndStartAddress(n, startAddress);
		this.memSpace = memSpace;
	}

	private byte[] initMemspaceFromAddressSizeAndStartAddress(int addressSizeBits, int startAddress) {
		long bitCnt = (long) Math.pow(2, addressSizeBits);
		long byteCnt = bitCnt / 8;
		byte[] memSpace = new byte[(int) byteCnt];
		memSpace = initEmptyMemspaceFromAddressSizeAndStartAddress(addressSizeBits, startAddress, memSpace);
		return memSpace;
	}

	private byte[] initEmptyMemspaceFromAddressSizeAndStartAddress(int addressSizeBits, int startAddress,
			byte[] memSpace) {
		memSpace = setBitsUnsignedBounds(ADDRESS_SIZE_START, ADDRESS_SIZE_END + 1,
				addressSizeBits - ADDRESS_SIZE_OFFSET, memSpace);
		memSpace = setBitsUnsignedBounds(START_ADDRESS_START, START_ADDRESS_START + addressSizeBits, startAddress,
				memSpace);
		return memSpace;
	}

	public SOMBitcodeRunner(byte[] memSpace) {
		memSpace = initFromExistingPartialMemspace(memSpace);
		this.memSpace = memSpace;
	}

	private byte[] initFromExistingPartialMemspace(byte[] memSpace) {
		int addressSizeBits = getN(memSpace);
		int startAddress = getStartAddress(memSpace);
		byte[] origgMemSpace = memSpace;
		memSpace = initMemspaceFromAddressSizeAndStartAddress(addressSizeBits, startAddress);
		for (int i = 0; i < origgMemSpace.length; i++) {
			memSpace[i] = origgMemSpace[i];
		}
		return memSpace;
	}

	public SOMBitcodeRunner(boolean[] bits) {
		byte[] memSpace = initFromBooleanArray(bits);
		this.memSpace = memSpace;
	}

	private byte[] initFromBooleanArray(boolean[] bits) {
		int bitCnt = bits.length;
		int byteCnt = bitCnt / 8;
		if (bitCnt % 8 != 0) {
			byteCnt++;
		}
		byte[] memSpace = new byte[(int) byteCnt];
		for (int i = 0; i < bits.length; i++) {
			boolean b = bits[i];
			memSpace = setBit(i, b, memSpace);
		}
		memSpace = initFromExistingPartialMemspace(memSpace);
		return memSpace;
	}

	private byte[] initFromBooleanArray(Boolean[] bits) {
		boolean[] realBooleanArray = new boolean[bits.length];
		for (int i = 0; i < realBooleanArray.length; i++) {
			realBooleanArray[i] = bits[i];
		}
		return initFromBooleanArray(realBooleanArray);
	}

	public SOMBitcodeRunner(List<Boolean> bits) {
		byte[] memSpace = initFromBooleanArray(bits.toArray(new Boolean[bits.size()]));
		this.memSpace = memSpace;
	}

	public SOMBitcodeRunner(String bits) {
		bits = bits.replaceAll("[^0-1]", "");
		boolean[] boolArr = new boolean[bits.length()];
		for (int i = 0; i < bits.length(); i++) {
			boolean b = bits.charAt(i) != '0';
			boolArr[i] = b;
		}
		this.memSpace = initFromBooleanArray(boolArr);
	}

	public static byte[] setBitsUnsignedBounds(int lowerBound, int upperBound, int value, byte[] memSpace) {
		int bits = upperBound - lowerBound;
		for (int i = 0; i < bits; i++) {
			boolean bitValue = value - Math.pow(2, bits - i - 1) >= 0 ? true : false;
			if (bitValue) {
				value -= Math.pow(2, bits - i - 1);
			}
			memSpace = setBit(lowerBound + i, bitValue, memSpace);
		}
		return memSpace;
	}

	public static byte[] setBitsUnsigned(int lowerBound, int n, int value, byte[] memSpace) {
		return setBitsUnsignedBounds(lowerBound, lowerBound + n, value, memSpace);
	}

	public static byte[] setBit(int address, boolean bitValue, byte[] memSpace) {
		int byteAddress = address / 8;
		int offset = 7 - address % 8;
		byte bite = memSpace[byteAddress];
		byte bitmask = (byte) (1 << offset);
		if (!bitValue) {
			bitmask = (byte) ~bitmask;
			bite = (byte) (bite & bitmask);
		} else {
			bite = (byte) (bite | bitmask);
		}
		memSpace[byteAddress] = bite;
		return memSpace;
	}

	public static boolean getBit(int address, byte[] memSpace) {
		int byteAddress = address / 8;
		int offset = 7 - address % 8;
		byte bite = memSpace[byteAddress];
		byte bitmask = (byte) (1 << offset);
		boolean bitValue = (bite & bitmask) != 0;
		return bitValue;
	}

	public static int getBitsUnsignedBounds(int lowerBound, int upperBound, byte[] memSpace) {
		int bits = upperBound - lowerBound;
		int returnValue = 0;
		for (int i = 0; i < bits; i++) {
			if (getBit(lowerBound + i, memSpace)) {
				int bitmask = 1 << (bits - i - 1);
				returnValue = returnValue | bitmask;
			}
		}
		return returnValue;
	}

	public static int getBitsUnsigned(int lowerBound, int n, byte[] memSpace) {
		return getBitsUnsignedBounds(lowerBound, lowerBound + n, memSpace);
	}

	public byte[] setBit(int address, boolean bitValue, boolean checkWriteHook) {
		memSpace = setBit(address, bitValue);
		if (checkWriteHook) {
			if (address == getWriteHookTriggerAddress()) {
				boolean writeHookTrigger = getBit(getWriteHookTriggerAddress());
				boolean writeHookSelectBit = getBit(getWriteHookSelectAddress());
				if (writeHookSelectBit) {
					IWriteHook currentlySelectedWriteHook = getSelectedWriteHook();
					if (currentlySelectedWriteHook != null) {
						boolean readOrWrite = getBit(getWriteHookTriggerAddress());
						if (readOrWrite) {
							boolean accumulatorValue = getBit(ACC_ADDRESS);
							currentlySelectedWriteHook.write(accumulatorValue,this);
						} else {
							boolean hasData=currentlySelectedWriteHook.hasDataAvailable();
							boolean newData = currentlySelectedWriteHook.read(this);
							if(hasData) {
								setBit(ACC_ADDRESS, newData);
								setBit(getWriteHookTriggerAddress(), hasData);
							}
						}
					}
				} else {
					if (writeHookTrigger) {
						// perform write hook wsitch and check
						// switch currently selected writehook
						if (selectedWriteHook > 0 && selectedWriteHook < writeHooks.size() - 1) {
							boolean accumulatorValue = getBit(ACC_ADDRESS);
							if (accumulatorValue) {
								selectedWriteHook++;
							} else {
								selectedWriteHook--;
							}
							memSpace = setBit(ACC_ADDRESS, true);
						} else {
							memSpace = setBit(ACC_ADDRESS, false);
						}
					}
				}
			}
		}
		return memSpace;
	}

	public byte[] setBit(int address, boolean bitValue) {
		return setBit(address, bitValue, memSpace);
	}

	public boolean getBit(int address) {
		return getBit(address, memSpace);
	}

	public int getBitsUnsignedBounds(int lowerBound, int upperBound) {
		return getBitsUnsignedBounds(lowerBound, upperBound, memSpace);
	}

	public int getBitsUnsigned(int lowerBound, int n) {
		return getBitsUnsigned(lowerBound, n, memSpace);
	}
	
	public boolean execute() {
		int addressSize=getN();
		int startAddress=getStartAddress();
		int opcodeSize=2;
		int commandSize=addressSize+opcodeSize;
		int programCounter=startAddress;
		while(programCounter<Math.pow(2, addressSize)) {
			//get next command
			boolean[] nextCommand=new boolean[commandSize];
			for (int i = 0; i < nextCommand.length; i++) {
				nextCommand[i]=getBit(programCounter+i);
			}
			boolean[] opCode=new boolean[2];
			for (int i = 0; i < opCode.length; i++) {
				opCode[i]=nextCommand[i];
			}
			boolean[] tgtAddress=new boolean[addressSize];
			for (int i = 0; i < tgtAddress.length; i++) {
				tgtAddress[i]=nextCommand[i+opcodeSize];
			}
			programCounter+=commandSize;
			Opcode op=null;
			if (!opCode[0] && !opCode[1]) {// 00
				op = Opcode.READ;
			} else if (!opCode[0] && opCode[1]) {// 01
				op = Opcode.WRITE;
			} else if (opCode[0] && !opCode[1]) {// 10
				op = Opcode.NAND;
			} else if (opCode[0] && opCode[1]) {// 11
				op = Opcode.CJMP;
			}
			int tgtAddressValue = Util.getAsUnsignedInt(tgtAddress);
			boolean accumulatorValue = getBit(ACC_ADDRESS);
			boolean tgtBitValue=getBit(tgtAddressValue);
			switch (op) {
			case CJMP:
				if(accumulatorValue) {
					programCounter=tgtAddressValue;
				}
				break;
			case NAND:
				setBit(ACC_ADDRESS, !(accumulatorValue&&tgtBitValue));
				break;
			case READ:
				setBit(ACC_ADDRESS, tgtBitValue);
				break;
			case WRITE:
				setBit(tgtAddressValue,accumulatorValue);
				break;
			default:
				break;
			}
		}
		return getBit(ACC_ADDRESS);
		
	}
}
