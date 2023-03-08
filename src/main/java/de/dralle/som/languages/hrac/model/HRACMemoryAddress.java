/**
 * 
 */
package de.dralle.som.languages.hrac.model;

/**
 * @author Nils
 *
 */
public class HRACMemoryAddress implements Cloneable {
	private HRACSymbol symbol;
	private Integer offset;
	private boolean offsetSpecial;
	private String offsetSpecialnName;

	public boolean isOffsetSpecial() {
		return offsetSpecial;
	}

	public void setOffsetSpecial(boolean offsetSpecial) {
		this.offsetSpecial = offsetSpecial;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public HRACMemoryAddress(HRACSymbol symbol) {
		this.symbol = symbol;
	}

	public HRACMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public HRACSymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(HRACSymbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public int hashCode() {
		int hashc = symbol.hashCode();
		if (offset != null) {
			hashc += offset.hashCode();
		}
		return hashc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof HRACMemoryAddress) {
			HRACMemoryAddress other = (HRACMemoryAddress) obj;
			boolean equal = symbol.equals(((HRACMemoryAddress) obj).symbol);
			if (equal && isOffsetSpecial()) {
				return offsetSpecialnName.equals(other.offsetSpecialnName);
			}
			if (equal && offset != null) {
				return offset.equals(((HRACMemoryAddress) obj).offset);
			}
			return equal;
		}
		return false;
	}

	@Override
	public String toString() {
		return asHRACCode();
	}

	@Override
	public HRACMemoryAddress clone() {
		HRACMemoryAddress copy = new HRACMemoryAddress();
		copy.symbol = symbol;
		copy.offsetSpecial = offsetSpecial;
		if (offset != null) {
			copy.offset = offset.intValue();
		}
		copy.offsetSpecialnName = offsetSpecialnName;
		return copy;
	}

	public String asHRACCode() {
		if (offsetSpecial) {
			return String.format("%s[$%s]", symbol.getName(), offsetSpecialnName);
		}
		if (offset != null) {
			return String.format("%s[%d]", symbol.getName(), offset);
		} else {
			return symbol.getName();
		}
	}

	@Deprecated
	public boolean getOffsetSpecial() {
		// TODO Auto-generated method stub
		return isOffsetSpecial();
	}

	public String getOffsetSpecialnName() {
		return offsetSpecialnName;
	}

	public void setOffsetSpecialnName(String offsetSpecialnName) {
		this.offsetSpecialnName = offsetSpecialnName;
	}
}
