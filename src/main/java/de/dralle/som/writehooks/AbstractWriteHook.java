/**
 * 
 */
package de.dralle.som.writehooks;

import de.dralle.som.SOMBitcodeRunner;

/**
 * @author Nils Dralle
 *
 */
public abstract class AbstractWriteHook implements IWriteHook{

	@Override
	public boolean read(SOMBitcodeRunner runner) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasDataAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean write(boolean accumutlatorValue,SOMBitcodeRunner runner) {
		// TODO Auto-generated method stub
		return false;
	}

}
