/**
 * 
 */
package de.dralle.som.languages.hrad.model;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrad.HRADSourceLocation;

/**
 * @author Nils
 *
 */
public class HRADCommand extends AbstractHRADCommand{
	private Opcode op;
	private int address;

	public HRADCommand(HRADSourceLocation sourceLocation) {
		super(sourceLocation);
	}

	public String asHRADCode() {
		return String.format("%s %d", op, address);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADCommand) {
			HRADCommand other = (HRADCommand) obj;
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
		return asHRADCode();
	}
}
