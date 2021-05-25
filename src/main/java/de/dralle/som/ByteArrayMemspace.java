/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public class ByteArrayMemspace extends AbstractSomMemspace {

	private boolean[] memory;
	
	/**
	 * Creates a new memory space. Note that will not initialize the memspace and for example set the bits for N in the memspace.
	 * @param size Size of the new memory space in bits.
	 */
	public ByteArrayMemspace(int size) {
		memory=new boolean[size];
	}
	
	/**
	 * Creates a new memory space. Note that will not initialize the memspace and for example set the bits for N in the memspace.
	 * @param size Size of the new memory space in bits.
	 */
	public ByteArrayMemspace() {
		memory=new boolean[0];
	}
	
	@Override
	public void setBit(int address, boolean bitValue) {
		memory[address]=bitValue;
	}

	@Override
	public boolean getBit(int address) {
		return memory[address];
	}

	@Override
	public int getSize() {
		return memory.length;
	}

	@Override
	public void resize(int newSize, boolean copyContent) {
		ByteArrayMemspace newmemSpace = new ByteArrayMemspace(newSize);
		if(copyContent) {
			newmemSpace.copy(this);
		}
		memory=newmemSpace.memory;
	}

	@Override
	public ByteArrayMemspace clone() {
		ByteArrayMemspace newmemSpace = new ByteArrayMemspace(getSize());
		newmemSpace.copy(this);
		return newmemSpace;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteArrayMemspace) {
			ByteArrayMemspace cTo = (ByteArrayMemspace)obj;
			return equalContent(cTo);
		}
		return false;
	}
}
