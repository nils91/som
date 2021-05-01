/**
 * 
 */
package de.dralle.som.writehooks;

import de.dralle.som.SOMBitcodeRunner;

/**
 * @author Nils Dralle
 *
 */
public interface IWriteHook {
	public boolean read(SOMBitcodeRunner runner);
	public boolean write(boolean accumulatorValue,SOMBitcodeRunner runner);
}
