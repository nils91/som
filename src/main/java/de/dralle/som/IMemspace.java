/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public interface IMemspace extends Cloneable{
	void setBit(int address, boolean bitValue);
	boolean getBit(int address);
	/**
	 * Returns the size of this memspace in bits.
	 * @return
	 */
	int getSize();
	void resize(int newSize,boolean copyContent);
	IMemspace clone();
	void copy(IMemspace from);
	boolean equalContent(IMemspace mem);
}
