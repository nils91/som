/**
 * 
 */
package de.dralle.som.languages.hra.model;

import de.dralle.som.Opcode;

/**
 * @author Nils
 *
 */
public class Command {
	private Opcode op;
	public Opcode getOp() {
		return op;
	}
	public int getTgtAddress() {
		return tgtAddress;
	}
	private int tgtAddress;
	public Command(Opcode op, int tgtAddress) {
		super();
		this.op = op;
		this.tgtAddress = tgtAddress;
	}
}
