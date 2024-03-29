/**
 * 
 */
package de.dralle.som.test.util;

import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.writehooks.AbstractWriteHook;

/**
 * @author Nils Dralle
 *
 */
public class TestWriteHook extends AbstractWriteHook {
	private int writeTrgCnt = 0;
	private int readTrgCnt = 0;

	private boolean[] writtenBits = new boolean[0];
	private boolean[] bitsProvidedForRead = new boolean[0];

	public boolean[] getBitsProvidedForRead() {
		return bitsProvidedForRead;
	}

	public void setBitsProvidedForRead(boolean[] bitsProvidedForRead) {
		this.bitsProvidedForRead = bitsProvidedForRead;
	}

	public boolean[] getWrittenBits() {
		return writtenBits;
	}

	

	public int getWriteTrgCnt() {
		return writeTrgCnt;
	}

	public int getReadTrgCnt() {
		return readTrgCnt;
	}

	@Override
	public boolean read(SOMBitcodeRunner runner) {
		readTrgCnt++;
		boolean bitToReturn = false;
		boolean[] bitsProvidedForReadNew = bitsProvidedForRead;
		if (bitsProvidedForRead.length > 0) {
			bitsProvidedForReadNew = new boolean[bitsProvidedForRead.length - 1];
			for (int i = 0; i < bitsProvidedForReadNew.length; i++) {
				bitsProvidedForReadNew[i] = bitsProvidedForRead[i + 1];
			}
			bitToReturn = bitsProvidedForRead[0];
		} 
		bitsProvidedForRead = bitsProvidedForReadNew;
		return bitToReturn;
	}

	@Override
	public boolean hasDataAvailable() {
		return bitsProvidedForRead.length>0;
	}

	@Override
	public boolean write(boolean accumulatorValue, SOMBitcodeRunner runner) {
		writeTrgCnt++;
		boolean[] writtenBitsNew = new boolean[writtenBits.length + 1];
		for (int i = 0; i < writtenBits.length; i++) {
			writtenBitsNew[i] = writtenBits[i];
		}
		writtenBitsNew[writtenBitsNew.length - 1] = accumulatorValue;
		writtenBits = writtenBitsNew;
		return false;
	}

}
