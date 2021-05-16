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
	private boolean lastWriteHookSwitchSuccess;

	public void addWriteHook(IWriteHook writeHook) {
		if (writeHooks == null) {
			writeHooks = new ArrayList<>();
		}
		writeHooks.add(writeHook);
	}

	private IWriteHook getSelectedWriteHook() {
		if (writeHooks!=null&& writeHooks.size() > 0 && selectedWriteHook > -1) {
			return writeHooks.get(selectedWriteHook);
		}
		return null;
	}

	public byte[] getMemSpace() {
		return memSpace;
	}

	public static boolean isWriteHookEnabled(byte[] memSpace) {
		int addressSizeBits = getN(memSpace);
		int startAddress = getAddressBits(memSpace);
		return (START_ADDRESS_START + addressSizeBits) < startAddress;
	}

	public boolean isWriteHookEnabled() {
		return isWriteHookEnabled(memSpace);
	}

	public static int getWriteHookEnabledAddress(byte[] memSpace) {
		int addressEvalAddress = getAddressEvaluationBitAddress(memSpace);
		return addressEvalAddress + 1;
	}
	
	public static int getWriteHookDirectionAddress(byte[] memSpace) {
		return getWriteHookEnabledAddress(memSpace) + 1;
	}

	public static int getWriteHookCommunicationAddress(byte[] memSpace) {
		return getWriteHookEnabledAddress(memSpace) + 2;
	}

	public static int getWriteHookSelectAddress(byte[] memSpace) {
		return getWriteHookEnabledAddress(memSpace) + 3;
	}

	public static boolean isWriteHookReadmode(byte[] memSpace) {
		return !getBit(getWriteHookEnabledAddress(memSpace), memSpace);
	}

	public static boolean isWriteHookWritemode(byte[] memSpace) {
		return getBit(getWriteHookEnabledAddress(memSpace), memSpace);

	}

	public static boolean isWriteHookSelected(byte[] memSpace) {
		return getBit(getWriteHookSelectAddress(memSpace), memSpace);

	}

	public static boolean isWriteHookSwitchmodeSelected(byte[] memSpace) {
		return !getBit(getWriteHookSelectAddress(memSpace), memSpace);
	}

	public int getWriteHookEnabledAddress() {
		return getWriteHookEnabledAddress(memSpace);
	}
	
	public int getWriteHookDirectionAddress() {
		return getWriteHookDirectionAddress(memSpace);
	}

	public int getWriteHookCommunicationAddress() {
		return getWriteHookCommunicationAddress(memSpace);
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

	public static int getAddressBits(byte[] memSpace) {
		int addressSizeBits = getN(memSpace);
		int startAddress = getBitsUnsignedBounds(START_ADDRESS_START, START_ADDRESS_START + addressSizeBits, memSpace);
		return startAddress;
	}

	public int getAddressBits() {
		return getAddressBits(memSpace);
	}

	public static int getAddressEvaluationBitAddress(byte[] memSpace) {
		int addressSizeBits = getN(memSpace);
		return START_ADDRESS_START + addressSizeBits;
	}

	public int getAddressEvaluationBitAddress() {
		return getAddressEvaluationBitAddress(memSpace);
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
		int startAddress = getAddressBits(memSpace);
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
		int programCounter = 0;
		boolean accumulator = false;
		int addressSize = 0;
		do {
			accumulator = getBit(ACC_ADDRESS);
			addressSize = getN();
			int startAddress = getAddressBits();
			boolean addressEval = getBit(getAddressEvaluationBitAddress());
			int opcodeSize = 1;
			int commandSize = addressSize + opcodeSize;
			if (addressEval) {
				programCounter = startAddress;
			}
			// get next command
			boolean[] nextCommand = new boolean[commandSize];
			for (int i = 0; i < nextCommand.length; i++) {
				nextCommand[i] = getBit(programCounter + i);
			}
			boolean opCode = nextCommand[0];
			boolean[] tgtAddress = new boolean[addressSize];
			for (int i = 0; i < tgtAddress.length; i++) {
				tgtAddress[i] = nextCommand[i + opcodeSize];
			}
			int tgtAddressValue = Util.getAsUnsignedInt(tgtAddress);
			boolean tgtBitValue = getBit(tgtAddressValue);
			boolean nand=!(accumulator&&tgtBitValue);
			if(!opCode)//NAR
			{
				setBit(ACC_ADDRESS, nand);
			}else {
				setBit(tgtAddressValue, nand);
			}
			//get write hook bits
			boolean whEn=getBit(getWriteHookEnabledAddress());
			boolean whDir=getBit(getWriteHookDirectionAddress());
			boolean whCom=getBit(getWriteHookCommunicationAddress());
			boolean whSel=getBit(getWriteHookSelectAddress());
			
			if(whEn) {
				//enabled
				if(whSel) {
					//write hook selected
					if(whDir) {
						//write
						IWriteHook currentlySelectedWriteHook = getSelectedWriteHook();
						if (currentlySelectedWriteHook != null) {
							currentlySelectedWriteHook.write(whCom, this);
						}
					}else {
						//read
						IWriteHook currentlySelectedWriteHook = getSelectedWriteHook();
						if (currentlySelectedWriteHook != null) {
							boolean hasNew = currentlySelectedWriteHook.hasDataAvailable();
							boolean readBit = currentlySelectedWriteHook.read(this);
							setBit(getWriteHookCommunicationAddress(), readBit);
							setBit(ACC_ADDRESS, hasNew);
						}
					}
				}else {
					//wh switch mode
					if(whDir) {
						// switch selected write hook
						if (selectedWriteHook > 0 && selectedWriteHook < writeHooks.size() - 1) {
							boolean comValue = getBit(getWriteHookCommunicationAddress());
							if (comValue) {
								selectedWriteHook++;
							} else {
								selectedWriteHook--;
							}
							lastWriteHookSwitchSuccess = true;
						} else {
							lastWriteHookSwitchSuccess = false;
						}
					}else {
						// last switching success
						setBit(getWriteHookCommunicationAddress(), lastWriteHookSwitchSuccess);
						setBit(ACC_ADDRESS, true);
					}
				}
			}
			
			programCounter+=commandSize;
		} while (programCounter < Math.pow(2, addressSize));
		return getBit(ACC_ADDRESS);
	}
}
