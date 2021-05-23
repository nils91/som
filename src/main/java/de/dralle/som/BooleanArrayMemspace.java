/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public class BooleanArrayMemspace extends AbstractSomMemspace {

	private boolean[] memory;
	
	/**
	 * Creates a new memory space.
	 * @param size Size of the new memory space in bits.
	 */
	public BooleanArrayMemspace(int size) {
		memory=new boolean[size];
	}
	
	@Override
	public void setBit(int address, boolean bitValue) {
		memory[address]=bitValue;
	}

	@Override
	public boolean getBit(int address) {
		return memory[address];
	}
}
