/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

/**
 * @author Nils
 *
 */
public abstract class AbstractHRBSMemoryAddress implements Cloneable {
	private boolean isDeref = false;
	private String tgtCmd;
	private String tgtCmdInst;
	private boolean tgtCmdInstIsDirective;

	private HRBSAbstractExpressionNode offset;

	private HRBSAbstractExpressionNode derefOffset;

	public AbstractHRBSMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public String asHRBSCode() {
		return getFirstPartHRBSCode() + getSecondPartHRBSCode();
	}

	@Override
	public AbstractHRBSMemoryAddress clone() {
		AbstractHRBSMemoryAddress copy = null;
		try {
			copy = (AbstractHRBSMemoryAddress) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (offset != null) {
			copy.offset = offset.clone();
		}
		if (derefOffset != null) {
			copy.derefOffset = derefOffset.clone();
		}
		copy.isDeref = isDeref;
		copy.tgtCmd = tgtCmd;
		copy.tgtCmdInst = tgtCmdInst;
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof AbstractHRBSMemoryAddress) {
			AbstractHRBSMemoryAddress other = (AbstractHRBSMemoryAddress) obj;
			boolean equal = (isDeref == other.isDeref);
			if (equal && offset != null) {
				equal = offset.equals(other.offset);
			}
			if (equal && derefOffset != null) {
				equal = derefOffset.equals(other.derefOffset);
			}
			if (equal && tgtCmd != null) {
				equal = tgtCmd.equals(other.tgtCmd);
			}
			return equal;
		}
		return super.equals(obj);
	}

	public HRBSAbstractExpressionNode getDerefOffset() {
		return derefOffset;
	}

	protected String getFirstPartHRBSCode() {
		String s = "";
		if (isDeref) {
			s += "&";
		}
		if (tgtCmd != null) {
			s += tgtCmd;
			if (tgtCmdInst != null) {
				s += String.format("[%s%s]", tgtCmdInstIsDirective ? "$" : "", tgtCmdInst);
			}
			s += ".";
		}
		return s;
	}

	public HRBSAbstractExpressionNode getOffset() {
		return offset;
	}

	protected String getSecondPartHRBSCode() {
		String s = "";
		if (offset != null) {
			s += "[" + offset + "]";
		}
		if (derefOffset != null) {
			s += "[" + derefOffset + "]";
		}
		s += ";";
		return s;
	}

	public String getTgtCmd() {
		return tgtCmd;
	}

	public String getTgtCmdInst() {
		return tgtCmdInst;
	}

	@Override
	public int hashCode() {
		int hashc = 0;
		if (tgtCmd != null) {
			hashc += tgtCmd.hashCode();
		}
		if (offset != null) {
			hashc += offset.hashCode();
		}
		if (derefOffset != null) {
			hashc += derefOffset.hashCode();
		}
		if (isDeref) {
			hashc *= 1337;
		}
		return hashc;
	}

	public boolean isDeref() {
		return isDeref;
	}

	public boolean isTgtCmdInstIsDirective() {
		return tgtCmdInstIsDirective;
	}

	public void setDeref(boolean isDeref) {
		this.isDeref = isDeref;
	}

	public void setDerefOffset(HRBSAbstractExpressionNode derefOffset) {
		this.derefOffset = derefOffset;
	}

	public void setDerefOffset(int derefOffset) {
		this.derefOffset = new HRBSIntegerNode(derefOffset);
	}

	public void setOffset(HRBSAbstractExpressionNode offset) {
		this.offset = offset;
	}

	public void setOffset(int offset) {
		this.offset = new HRBSIntegerNode(offset);
	}

	public void setTgtCmd(String tgtCmd) {
		this.tgtCmd = tgtCmd;
	}

	public void setTgtCmdInst(String tgtCmdInst) {
		this.tgtCmdInst = tgtCmdInst;
	}

	public void setTgtCmdInstIsDirective(boolean tgtCmdInstIsDirective) {
		this.tgtCmdInstIsDirective = tgtCmdInstIsDirective;
	}

	@Override
	public String toString() {
		return asHRBSCode();
	}

}
