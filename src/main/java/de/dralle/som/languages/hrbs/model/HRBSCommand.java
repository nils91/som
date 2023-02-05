/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

import de.dralle.som.Opcode;

/**
 * @author Nils
 *
 */
public class HRBSCommand {
	private HRBSSymbol label;
	private Opcode op;
	private HRBSMemoryAddress target;
	

	public HRBSSymbol getLabel() {
		return label;
	}
	public void setLabel(HRBSSymbol label) {
		this.label = label;
	}
	public HRBSMemoryAddress getTarget() {
		return target;
	}
	public void setTarget(HRBSMemoryAddress target) {
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
		code+=label.getName()+" ";
	}
	code+=op+" "+target.asHrbsCode();
	return code;
}
	
	@Override
public String toString() {
	return asCode();
}
	public HRBSCommand() {
		super();
	}
}
