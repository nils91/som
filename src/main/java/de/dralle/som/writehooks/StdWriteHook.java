/**
 * 
 */
package de.dralle.som.writehooks;

import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.writehooks.AbstractWriteHook;

/**
 * @author Nils Dralle
 *
 */
public class StdWriteHook extends AbstractWriteHook {
	@Override
	public boolean read(SOMBitcodeRunner runner) {
		
		return true;
	}

	@Override
	public boolean hasDataAvailable() {
		return true;
	}

	@Override
	public boolean write(boolean accumulatorValue, SOMBitcodeRunner runner) {		
		return true;
	}

}
