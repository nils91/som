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
	private AbstractHRASMemoryAddress address;

	public HRASCommand() {
		super();
	}

	public String asHRASCode() {
		return String.format("%s %s", op, address.asHRASCode());
	}

	public AbstractHRASMemoryAddress getAddress() {
		return address;
	}

	public Opcode getOp() {
		return op;
	}

	public void setAddress(AbstractHRASMemoryAddress abstractHRASMemoryAddress) {
		this.address = abstractHRASMemoryAddress;
	}

	public void setOp(Opcode op) {
		this.op = op;
	}

	@Override
	public String toString() {
		return asHRASCode();
	}
}
