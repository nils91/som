/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import de.dralle.som.Opcode;

/**
 * @author Nils
 *
 */
public class HRACCommand {
	private HRACSymbol label;
	private Opcode op;
	private HRACMemoryAddress target;
	

	public Opcode getOp() {
		return op;
	}
	public void setOp(Opcode op) {
		this.op = op;
	}
public String asHRASCode() {
	return String.format("%s %s", op,address.asHRACCode());
}
	
	@Override
public String toString() {
	return asHRASCode();
}
	public HRACCommand() {
		super();
	}
}
