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
	

	public HRACSymbol getLabel() {
		return label;
	}
	public void setLabel(HRACSymbol label) {
		this.label = label;
	}
	public HRACMemoryAddress getTarget() {
		return target;
	}
	public void setTarget(HRACMemoryAddress target) {
		this.target = target;
	}
	public Opcode getOp() {
		return op;
	}
	public void setOp(Opcode op) {
		this.op = op;
	}
public String asCode() {
	String code = "";
	if(label!=null) {
		code+=label.getName()+": ";
	}
	code+=op+" "+target.asHRACCode();
	return code;
}
	
	@Override
public String toString() {
	return asCode();
}
	public HRACCommand() {
		super();
	}
}
