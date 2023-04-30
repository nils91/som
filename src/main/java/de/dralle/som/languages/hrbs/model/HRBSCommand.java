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
	public HRBSCommand clone() {
		HRBSCommand clone = null;
		try {
			clone = (HRBSCommand) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clone.setCmd(command);
		clone.label = label;
		clone.labelType = labelType;
		clone.cllInstId = cllInstId;
		clone.instIdDirective = instIdDirective;
		if (targets != null) {
			clone.targets = new ArrayList<>();
			for (HRBSMemoryAddress hrbsMemoryAddress : targets) {
				clone.targets.add(hrbsMemoryAddress.clone());
			}
		}
		if (range != null) {
			clone.range = range.clone();
		}
		return clone;
	}

	private HRBSRange range; // if this command is to be executed multiple times
	private String label;
	private String cllInstId;

	public String getCllInstId() {
		return cllInstId;
	}

	public void setCllInstId(String cllInstId) {
		this.cllInstId = cllInstId;
	}

	/**
	 * If true, cllInstId is interpreted as directive name which contains the
	 * instance id.
	 */
	private boolean instIdDirective;
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
			if (labelType != null) {
				code += labelType + " ";
			}
			code += label + ": ";
		}
		if (range != null) {
			code += range + " ";
		}
		code += command;
		if(cllInstId!=null) {
			code+="[";
			if(instIdDirective) {
				code+="$";
			}
			code+=cllInstId+"]";
		}
		if (targets != null) {
			code += " ";
			for (HRBSMemoryAddress hrbsMemoryAddress : targets) {
				code += hrbsMemoryAddress.asHRBSCode() + ",";
			}
			code = code.substring(0, code.length() - 1);
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
		this.labelType = labelType;
	}

	public HRBSRange getRange() {
		return range;
	}

	public void setRange(HRBSRange range) {
		this.range = range;
	}

	public boolean isInstIdDirective() {
		return instIdDirective;
	}

	public void setInstIdDirective(boolean instIdDirective) {
		this.instIdDirective = instIdDirective;
	}
}
