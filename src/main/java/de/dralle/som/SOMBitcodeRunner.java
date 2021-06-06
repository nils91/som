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
	private ISomMemspace memspace;
	public ISomMemspace getMemspace() {
		return memspace;
	}

	public void setMemspace(ISomMemspace memspace) {
		this.memspace = memspace;
	}
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
		if (writeHooks != null && writeHooks.size() > 0 && selectedWriteHook > -1) {
			return writeHooks.get(selectedWriteHook);
		}
		return null;
	}

	

	public SOMBitcodeRunner(int n, int startAddress) {
		ISomMemspace memSpace = initMemspaceFromAddressSizeAndStartAddress(n, startAddress);
		this.memspace = memSpace;
	}

	private ISomMemspace initMemspaceFromAddressSizeAndStartAddress(int addressSizeBits, int startAddress) {
		int bitCnt = (int) Math.pow(2, addressSizeBits);
		ISomMemspace memSpace = new BooleanArrayMemspace(bitCnt);
		memSpace = initEmptyMemspaceFromAddressSizeAndStartAddress(addressSizeBits, startAddress, memSpace);
		return memSpace;
	}

	private ISomMemspace initEmptyMemspaceFromAddressSizeAndStartAddress(int addressSizeBits, int startAddress,
			ISomMemspace memSpace) {
		memSpace.setN(addressSizeBits);
		memSpace.setNextAddress(startAddress);
		memSpace.setAdrEval();
		return memSpace;
	}

	public SOMBitcodeRunner(ISomMemspace memSpace) {
		memSpace = initFromExistingPartialMemspace(memSpace);
		this.memspace = memSpace;
	}

	private ISomMemspace initFromExistingPartialMemspace(ISomMemspace memSpace) {
		int addressSizeBits =memSpace.getN();
		int startAddress = memSpace.getNextAddress();
		ISomMemspace origgMemSpace = memSpace.clone();
		memSpace = initMemspaceFromAddressSizeAndStartAddress(addressSizeBits, startAddress);
		memSpace.copy(origgMemSpace);
		return memSpace;
	}

	public SOMBitcodeRunner(boolean[] bits) {
		ISomMemspace memSpace = initFromBooleanArray(bits);
		this.memspace = memSpace;
	}

	private ISomMemspace initFromBooleanArray(boolean[] bits) {
		ISomMemspace memSpace = new BooleanArrayMemspace(bits.length);
		for (int i = 0; i < bits.length; i++) {
			boolean b = bits[i];
			memSpace.setBit(i, b);
		}
		memSpace = initFromExistingPartialMemspace(memSpace);
		return memSpace;
	}

	private ISomMemspace initFromBooleanArray(Boolean[] bits) {
		boolean[] realBooleanArray = new boolean[bits.length];
		for (int i = 0; i < realBooleanArray.length; i++) {
			realBooleanArray[i] = bits[i];
		}
		return initFromBooleanArray(realBooleanArray);
	}

	public SOMBitcodeRunner(List<Boolean> bits) {
		ISomMemspace memSpace = initFromBooleanArray(bits.toArray(new Boolean[bits.size()]));
		this.memspace = memSpace;
	}

	public SOMBitcodeRunner(String bits) {
		bits = bits.replaceAll("[^0-1]", "");
		boolean[] boolArr = new boolean[bits.length()];
		for (int i = 0; i < bits.length(); i++) {
			boolean b = bits.charAt(i) != '0';
			boolArr[i] = b;
		}
		this.memspace = initFromBooleanArray(boolArr);
	}
	public boolean execute() {
		int programCounter = 0;
		boolean accumulator = false;
		int addressSize = 0;
		do {
			accumulator = memspace.getAccumulatorValue();
			addressSize = memspace.getN();
			int startAddress = memspace.getNextAddress();
			boolean addressEval = memspace.isAdrEvalSet();
			int opcodeSize = 1;
			int commandSize = addressSize + opcodeSize;
			if (addressEval) {
				programCounter = startAddress;
			}
			// get next command
			boolean[] nextCommand = new boolean[commandSize];
			for (int i = 0; i < nextCommand.length; i++) {
				nextCommand[i] = memspace.getBit(programCounter + i);
			}
			boolean opCode = nextCommand[0];
			boolean[] tgtAddress = new boolean[addressSize];
			for (int i = 0; i < tgtAddress.length; i++) {
				tgtAddress[i] = nextCommand[i + opcodeSize];
			}
			int tgtAddressValue = Util.getAsUnsignedInt(tgtAddress);
			boolean tgtBitValue = memspace.getBit(tgtAddressValue);
			boolean nand = !(accumulator && tgtBitValue);
			if (!opCode)// NAR
			{
				memspace.setAccumulatorValue(nand);
			} else {
				memspace.setBit(tgtAddressValue, nand);
			}
			// get write hook bits
			boolean whEn = memspace.isWriteHookEnabled();
			boolean whDir = memspace.isWriteHookWritemode();
			boolean whCom = memspace.getWriteHookCommunicationBit();
			boolean whSel = memspace.isWriteHookSelected();

			if (whEn) {
				// enabled
				if (whSel) {
					// write hook selected
					if (whDir) {
						// write
						IWriteHook currentlySelectedWriteHook = getSelectedWriteHook();
						if (currentlySelectedWriteHook != null) {
							currentlySelectedWriteHook.write(whCom, this);
						}
					} else {
						// read
						IWriteHook currentlySelectedWriteHook = getSelectedWriteHook();
						if (currentlySelectedWriteHook != null) {
							boolean hasNew = currentlySelectedWriteHook.hasDataAvailable();
							boolean readBit = currentlySelectedWriteHook.read(this);
							memspace.setWriteHookCommunicationBit(readBit);
							memspace.setWriteHookDirectionBit(!hasNew);
						}else {
							memspace.setWriteHookDirectionBit(true);
						}
					}
				} else {
					// wh switch mode
					if (whDir) {
						// switch selected write hook
						if (selectedWriteHook > 0 && selectedWriteHook < writeHooks.size() - 1) {
							boolean comValue = memspace.getWriteHookCommunicationBit();
							if (comValue) {
								selectedWriteHook++;
							} else {
								selectedWriteHook--;
							}
							lastWriteHookSwitchSuccess = true;
						} else {
							lastWriteHookSwitchSuccess = false;
						}
					} else {
						// last switching success
						memspace.setWriteHookCommunicationBit(lastWriteHookSwitchSuccess);
						memspace.setWriteHookDirectionBit(false);
					}
				}
			}

			programCounter += commandSize;
		} while (programCounter < Math.pow(2, addressSize));
		return memspace.getAccumulatorValue();
	}
}
