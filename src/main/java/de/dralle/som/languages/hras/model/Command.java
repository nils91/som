/**
 * 
 */
package de.dralle.som.languages.hras.model;

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

	
	public Command() {
		super();
	}
}
