/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class HRACCommand implements Cloneable{
	private HRACSymbol label;
	private Opcode op;
	private AbstractHRACMemoryAddress target;

	public HRACSymbol getLabel() {
		return label;
	}

	@Override
	protected HRACCommand clone(){
		HRACCommand copy = null;
		try {
			copy = (HRACCommand) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(label!=null) {
			copy.label=label.clone();
		}
		if(target!=null) {
			copy.target=target.clone();
		}
		return copy;
	}

	public void setLabel(HRACSymbol label) {
		this.label = label;
	}

	public AbstractHRACMemoryAddress getTarget() {
		return target;
	}

	public void setTarget(AbstractHRACMemoryAddress target) {
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
		if (label != null) {
			code += label.getName() + ": ";
		}
		code += op + " " + target.asHRACCode();
		return code;
	}

	@Override
	public String toString() {
		return asCode();
	}

	public HRACCommand() {
		super();
	}

	public HRACCommand(HRASCommand val) {
		op=val.getOp();
		AbstractHRASMemoryAddress hrascaddress = val.getAddress();
		if(hrascaddress instanceof SymbolHRASMemoryAddress) {
			target=new NamedHRACMemoryAddress(((SymbolHRASMemoryAddress)val.getAddress()).getSymbol());
		}else {
			target=new NamedHRACMemoryAddress((val.getAddress()).toString());
		}
		
	}
}
