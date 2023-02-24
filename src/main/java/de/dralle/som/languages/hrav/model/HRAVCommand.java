/**
 * 
 */
package de.dralle.som.languages.hrav.model;

import de.dralle.som.Opcode;

/**
 * @author Nils
 *
 */
public class HRAVCommand {
	private Opcode op;
	private int address;
	
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public Opcode getOp() {
		return op;
	}
	public void setOp(Opcode op) {
		this.op = op;
	}
public String asHRAVCode() {
	return String.format("%s %d", op,address);
}
	
	@Override
public String toString() {
	return asHRAVCode();
}
	public HRAVCommand() {
		super();
	}
}
