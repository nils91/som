/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import de.dralle.som.Opcode;

/**
 * @author Nils
 *
 */
public class Command {
	private Opcode op;
	private MemoryAddress address;
	
	public MemoryAddress getAddress() {
		return address;
	}
	public void setAddress(MemoryAddress address) {
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
	public Command() {
		super();
	}
}