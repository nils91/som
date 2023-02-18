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
	private List<AbstractUnconditionalDebugPoint> dps=new ArrayList<>();
	public void addDebugPoint(AbstractUnconditionalDebugPoint dp) {
		dps.add(dp);
	}
	private WriteHookManager writeHookManager = new WriteHookManager();

	public WriteHookManager getWriteHookManager() {
		return writeHookManager;
	}

	public void setWriteHookManager(WriteHookManager writeHookManager) {
		this.writeHookManager = writeHookManager;
	}

	public ISomMemspace getMemspace() {
		return memspace;
	}

	public void setMemspace(ISomMemspace memspace) {
		this.memspace = memspace;
	}

	private ISomMemspace initMemspaceFromAddressSizeAndStartAddress(int addressSizeBits, int startAddress) {
		int bitCnt = (int) Math.pow(2, addressSizeBits);
		ISomMemspace memSpace = new ByteArrayMemspace(bitCnt);
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
		int addressSizeBits = memSpace.getN();
		int startAddress = memSpace.getNextAddress();
		ISomMemspace origMemSpace = memSpace.clone();
		memSpace = initMemspaceFromAddressSizeAndStartAddress(addressSizeBits, startAddress);
		memSpace.copy(origMemSpace);
		return memSpace;
	}

	public boolean execute() {
		int programCounter = 0;
		boolean accumulator = false;
		int addressSize = 0;
		do {			
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
			//update debug points
			for (AbstractUnconditionalDebugPoint dp : dps) {
				dp.update(programCounter, opCode?Opcode.NAW:Opcode.NAR, tgtAddressValue, memspace);
			}
			accumulator = memspace.getAccumulatorValue();
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
						IWriteHook currentlySelectedWriteHook = writeHookManager.getSelectedWriteHook();
						if (currentlySelectedWriteHook != null) {
							currentlySelectedWriteHook.write(whCom, this);
						}
					} else {
						// read
						IWriteHook currentlySelectedWriteHook = writeHookManager.getSelectedWriteHook();
						if (currentlySelectedWriteHook != null) {
							boolean hasNew = currentlySelectedWriteHook.hasDataAvailable();
							if (hasNew) {
								boolean readBit = currentlySelectedWriteHook.read(this);
								memspace.setWriteHookCommunicationBit(readBit);
							}
							memspace.setWriteHookDirectionBit(!hasNew);
						} else {
							memspace.setWriteHookDirectionBit(true);
						}
					}
				} else {
					// wh switch mode
					if (whDir) {
						// switch selected write hook
						boolean comValue = memspace.getWriteHookCommunicationBit();
						if (comValue) {
							writeHookManager.switchToNextWriteHook();
						} else {
							writeHookManager.switchToPreviousWriteHook();
						}
					} else {
						// last switching success
						memspace.setWriteHookCommunicationBit(writeHookManager.isLastSwitchSuccess());
						memspace.setWriteHookDirectionBit(false);
					}
				}
			}

			programCounter += commandSize;
		} while (programCounter < Math.pow(2, addressSize));
		return memspace.getAccumulatorValue();
	}
}
