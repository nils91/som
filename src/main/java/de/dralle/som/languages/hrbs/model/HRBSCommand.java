/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.Opcode;

/**
 * @author Nils
 *
 */
public class HRBSCommand implements Cloneable {
	@Override
	public HRBSCommand clone(){
		HRBSCommand clone = new HRBSCommand();
		clone.setCmd(command);
		clone.label=label;
		clone.labelType=labelType;
		clone.cllInstId=cllInstId;
		if(targets!=null) {
			clone.targets=new ArrayList<>();
			for (HRBSMemoryAddress hrbsMemoryAddress : targets) {
				clone.targets.add(hrbsMemoryAddress.clone());
			}
		}
		return clone;
	}

	private String label;
	private String cllInstId;
	public String getCllInstId() {
		return cllInstId;
	}

	public void setCllInstId(String cllInstId) {
		this.cllInstId = cllInstId;
	}

	private HRBSSymbolType labelType;
	public HRBSSymbolType getLabelType() {
		return labelType;
	}

	private String command;
	private List<HRBSMemoryAddress> targets;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<HRBSMemoryAddress> getTarget() {
		return targets;
	}

	public void setTarget(List<HRBSMemoryAddress> target) {
		this.targets = target;
	}

	public void addTarget(HRBSMemoryAddress target) {
		if (targets == null) {
			targets = new ArrayList<>();
		}
		targets.add(target);
	}

	public String getCmd() {
		return command;
	}

	public void setCmd(String op) {
		this.command = op;
	}

	public String asCode() {
		String code = "";
		if (label != null) {
			if(labelType!=null) {
				code+=labelType+" ";
			}
			code += label + ":\n ";
		}
		code += command;
		if(targets!=null) {
			code+=" ";
			for (HRBSMemoryAddress hrbsMemoryAddress : targets) {
				code+=hrbsMemoryAddress.asHRBSCode()+",";
			}
			code=code.substring(0, code.length()-1);
		}		
		return code;
	}

	@Override
	public String toString() {
		return asCode();
	}

	public HRBSCommand() {
		super();
	}

	public void setLabelType(HRBSSymbolType labelType) {
		this.labelType=labelType;
	}
}
