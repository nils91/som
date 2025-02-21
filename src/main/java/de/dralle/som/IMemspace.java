/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public interface IMemspace extends Cloneable {
	IMemspace clone();

	void copy(IMemspace from);

	boolean equalContent(IMemspace mem);

	boolean getBit(int address);

	/**
	 * Returns the size of this memspace in bits.
	 * 
	 * @return
	 */
	int getSize();

	void resize(int newSize, boolean copyContent);

	void setBit(int address, boolean bitValue);
}
