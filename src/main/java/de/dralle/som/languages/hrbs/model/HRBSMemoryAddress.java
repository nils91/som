/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

/**
 * @author Nils
 *
 */
public class HRBSMemoryAddress implements Cloneable{
	private boolean isDeref=false;
	private HRBSSymbol symbol;
	private String tgtCmd;
	public String getTgtCmd() {
		return tgtCmd;
	}

	public void setTgtCmd(String tgtCmd) {
		this.tgtCmd = tgtCmd;
	}

	private Integer offset;
	private Integer derefOffset;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public HRBSMemoryAddress(HRBSSymbol symbol) {
		this.symbol = symbol;
	}

	public HRBSMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public HRBSSymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(HRBSSymbol symbol) {
		this.symbol = symbol;
	}


	@Override
	public int hashCode() {
		int hashc = symbol.hashCode();
		if(tgtCmd!=null) {
			hashc+=tgtCmd.hashCode();
		}
		if(offset!=null) {
			hashc+=offset.hashCode();
		}
		if(derefOffset!=null) {
			hashc+=derefOffset.hashCode();
		}
		if(isDeref) {
			hashc*=1337;
		}
		return hashc;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null&&obj instanceof HRBSMemoryAddress) {
			HRBSMemoryAddress other = (HRBSMemoryAddress)obj;
			boolean equal = symbol.equals(other.symbol);
			equal=equal&&(isDeref==other.isDeref);
			if(equal&&offset!=null) {
				equal= offset.equals(other.offset);
			}
			if(equal&&derefOffset!=null) {
				equal= derefOffset.equals(other.derefOffset);
			}
			if(equal&&tgtCmd!=null) {
				equal=tgtCmd.equals(other.tgtCmd);
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
	public HRBSMemoryAddress clone() {
		HRBSMemoryAddress copy = new HRBSMemoryAddress();
		if(symbol!=null) {
			copy.symbol=symbol.clone();
		}		
		if(offset!=null) { 
			copy.offset=offset.intValue();
		}		
		if(derefOffset!=null) { 
			copy.derefOffset=derefOffset.intValue();
		}		
		copy.isDeref=isDeref;
		copy.tgtCmd=tgtCmd;
		return copy;
	}

	public String asHRBSCode() {
		String s = "";
		if(isDeref) {
			s+="&";
		}
		if(tgtCmd!=null) {
			s+=tgtCmd+".";
		}
		s+=symbol.getName();
		if(offset!=null) {
			s+="["+offset+"]";
		}if(derefOffset!=null) {
			s+="["+derefOffset+"]";
		}
		return s;
	}

	public boolean isDeref() {
		return isDeref;
	}

	public void setDeref(boolean isDeref) {
		this.isDeref = isDeref;
	}

	public Integer getDerefOffset() {
		return derefOffset;
	}

	public void setDerefOffset(Integer derefOffset) {
		this.derefOffset = derefOffset;
	}
}
