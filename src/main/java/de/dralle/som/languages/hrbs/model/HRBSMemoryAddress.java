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
	private Integer offset;

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
		if(offset!=null) {
			hashc+=offset.hashCode();
		}
		return hashc;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null&&obj instanceof HRBSMemoryAddress) {
			HRBSMemoryAddress other = (HRBSMemoryAddress)obj;
			boolean equal = symbol.equals(((HRBSMemoryAddress)obj).symbol);
			if(equal&&offset!=null) {
				return offset.equals(((HRBSMemoryAddress)obj).offset);
			}
			return equal;
		}
		return false;
	}

	@Override
	public String toString() {
		return asHRBSCode();
	}

	@Override
	public HRBSMemoryAddress clone() {
		HRBSMemoryAddress copy = new HRBSMemoryAddress();
		copy.symbol=symbol;
		if(offset!=null) { 
			copy.offset=offset.intValue();
		}		
		return copy;
	}

	public String asHRBSCode() {
		if(offset!=null) {
			return String.format("%s[%d]", symbol.getName(),offset);
		}else {
			return symbol.getName();
		}
	}

	public boolean isDeref() {
		return isDeref;
	}

	public void setDeref(boolean isDeref) {
		this.isDeref = isDeref;
	}
}
