/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

/**
 * @author Nils
 *
 */
public abstract class AbstractHRBSMemoryAddress implements Cloneable {
	private boolean isDeref = false;
	private String tgtCmd;
	private String tgtCmdInst;
	private boolean tgtCmdInstIsDirective;

	public String getTgtCmdInst() {
		return tgtCmdInst;
	}

	public void setTgtCmdInst(String tgtCmdInst) {
		this.tgtCmdInst = tgtCmdInst;
	}

	public String getTgtCmd() {
		return tgtCmd;
	}

	public void setTgtCmd(String tgtCmd) {
		this.tgtCmd = tgtCmd;
	}

	private HRBSMemoryAddressOffset offset;
	private HRBSMemoryAddressOffset derefOffset;

	public HRBSMemoryAddressOffset getOffset() {
		return offset;
	}

	public void setOffset(HRBSMemoryAddressOffset offset) {
		this.offset = offset;
	}

	public void setOffset(int offset) {
		this.offset = new HRBSMemoryAddressOffset(offset);
	}

	public AbstractHRBSMemoryAddress() {
		// TODO Auto-generated constructor stub
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

	@Override
	public String toString() {
		return asHRBSCode();
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

	public String asHRBSCode() {
		return getFirstPartHRBSCode() + getSecondPartHRBSCode();
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

	public boolean isDeref() {
		return isDeref;
	}

	public void setDeref(boolean isDeref) {
		this.isDeref = isDeref;
	}

	public HRBSMemoryAddressOffset getDerefOffset() {
		return derefOffset;
	}

	public void setDerefOffset(HRBSMemoryAddressOffset derefOffset) {
		this.derefOffset = derefOffset;
	}

	public void setDerefOffset(int derefOffset) {
		this.derefOffset = new HRBSMemoryAddressOffset(derefOffset);
	}

	public boolean isTgtCmdInstIsDirective() {
		return tgtCmdInstIsDirective;
	}

	public void setTgtCmdInstIsDirective(boolean tgtCmdInstIsDirective) {
		this.tgtCmdInstIsDirective = tgtCmdInstIsDirective;
	}

}
