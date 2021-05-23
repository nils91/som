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
	 * Creates a new memory space. Note that will not initialize the memspace and for example set the bits for N in the memspace.
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
