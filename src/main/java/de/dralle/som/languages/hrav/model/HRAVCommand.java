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

	public HRAVCommand() {
		super();
	}

	public String asHRAVCode() {
		return String.format("%s %d", op, address);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRAVCommand) {
			HRAVCommand other = (HRAVCommand) obj;
			boolean equal = op.equals(other.op);
			equal = equal && address == other.address;
			return equal;
		}
		return super.equals(obj);
	}

	public int getAddress() {
		return address;
	}

	public Opcode getOp() {
		return op;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public void setOp(Opcode op) {
		this.op = op;
	}

	@Override
	public String toString() {
		return asHRAVCode();
	}
}
