/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public interface IMemspace {
	void setBit(int address, boolean bitValue);
	boolean getBit(int address);
}
