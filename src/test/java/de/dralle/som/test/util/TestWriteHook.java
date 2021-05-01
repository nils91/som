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

	public int getWriteTrgCnt() {
		return writeTrgCnt;
	}

	public int getReadTrgCnt() {
		return readTrgCnt;
	}

	@Override
	public boolean read(SOMBitcodeRunner runner) {
		readTrgCnt++;
		return false;
	}

	@Override
	public boolean hasDataAvailable() {
		return true;
	}

	@Override
	public boolean write(boolean accumutlatorValue, SOMBitcodeRunner runner) {
		writeTrgCnt++;
		return false;
	}

}
