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
	private String tgtSymbol;
	public Command(Opcode op, String tgtSymbol) {
		super();
		this.op = op;
		this.tgtSymbol = tgtSymbol;
	}
}
