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
	private AbstractHRBSRange range; // if this command is to be executed multiple times

	private String label;
	private String cllInstId;
	/**
	 * If true, cllInstId is interpreted as directive name which contains the
	 * instance id.
	 */
	private boolean instIdDirective;

	private HRBSSymbolType labelType;

	private String command;

	private List<AbstractHRBSMemoryAddress> targets;

	public HRBSCommand() {
		super();
	}

	public void addTarget(AbstractHRBSMemoryAddress target) {
		if (targets == null) {
			targets = new ArrayList<>();
		}
		targets.add(target);
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
		if (cllInstId != null) {
			code += "[";
			if (instIdDirective) {
				code += "$";
			}
			code += cllInstId + "]";
		}
		if (targets != null) {
			code += " ";
			for (AbstractHRBSMemoryAddress hrbsMemoryAddress : targets) {
				code += hrbsMemoryAddress.asHRBSCode() + ",";
			}
			code = code.substring(0, code.length() - 1);
		}
		return code;
	}

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
			for (AbstractHRBSMemoryAddress hrbsMemoryAddress : targets) {
				clone.targets.add(hrbsMemoryAddress.clone());
			}
		}
		if (range != null) {
			clone.range = range.clone();
		}
		return clone;
	}

	public String getCllInstId() {
		return cllInstId;
	}

	public String getCmd() {
		return command;
	}

	public String getLabel() {
		return label;
	}

	public HRBSSymbolType getLabelType() {
		return labelType;
	}

	public AbstractHRBSRange getRange() {
		return range;
	}

	public List<AbstractHRBSMemoryAddress> getTarget() {
		return targets;
	}

	public boolean isInstIdDirective() {
		return instIdDirective;
	}

	/**
	 * Returns true if cmd is one of the standard commands
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean isStandardCommand() {
		for (Opcode op : Opcode.values()) {
			if (op.name().equals(getCmd())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Recursively count NAR/NAW child command.
	 * 
	 * @param parent Parent model to this command
	 * @return
	 */
	public int recursiveCountAtomicCommands(HRBSModel parent) {
		if (isStandardCommand()) {
			return 1;
		} else {
			HRBSModel model = parent.getChilds().get(command);
			if (model == null) {
				return 0;
			} else {
				return model.recursiveCountAtomicCommands();
			}
		}
	}

	public void setCllInstId(String cllInstId) {
		this.cllInstId = cllInstId;
	}

	public void setCmd(String op) {
		this.command = op;
	}

	public void setInstIdDirective(boolean instIdDirective) {
		this.instIdDirective = instIdDirective;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLabelType(HRBSSymbolType labelType) {
		this.labelType = labelType;
	}

	public void setRange(AbstractHRBSRange range) {
		this.range = range;
	}

	public void setTarget(List<AbstractHRBSMemoryAddress> target) {
		this.targets = target;
	}

	@Override
	public String toString() {
		return asCode();
	}
}
