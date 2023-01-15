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
	private int addressOffset;
	public int getAddressOffset() {
		return addressOffset;
	}
	public Opcode getOp() {
		return op;
	}
	private String tgtSymbol;
	public void setOp(Opcode op) {
		this.op = op;
	}
	public void setAddressOffset(int addressOffset) {
		this.addressOffset = addressOffset;
	}
	public void setTgtSymbol(String tgtSymbol) {
		this.tgtSymbol = tgtSymbol;
	}
	public String getTgtSymbol() {
		return tgtSymbol;
	}
	public Command(Opcode op, String tgtSymbol) {
		super();
		this.op = op;
		this.tgtSymbol = tgtSymbol;
	}
	public Command(Opcode op, String tgtSymbol,int offset) {
		super();
		this.op = op;
		this.tgtSymbol = tgtSymbol;
		this.addressOffset= offset;
	}
	public Command() {
		super();
	}
}
