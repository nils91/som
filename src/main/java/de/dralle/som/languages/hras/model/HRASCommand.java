/**
 * 
 */
package de.dralle.som.languages.hras.model;

import de.dralle.som.Opcode;

/**
 * @author Nils
 *
 */
public class HRASCommand {
	private Opcode op;
	private HRASMemoryAddress address;
	
	public HRASMemoryAddress getAddress() {
		return address;
	}
	public void setAddress(HRASMemoryAddress address) {
		this.address = address;
	}
	public Opcode getOp() {
		return op;
	}
	public void setOp(Opcode op) {
		this.op = op;
	}
public String asHRASCode() {
	return String.format("%s %s", op,address.asHRASCode());
}
	
	@Override
public String toString() {
	return asHRASCode();
}
	public HRASCommand() {
		super();
	}
}
