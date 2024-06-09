/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class AbstractHRACMemoryAddress implements Cloneable {
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
	protected AbstractHRACMemoryAddress() {
		
	}

	@Override
	public int hashCode() {
		int hashc=0;
		if (offset != null) {
			hashc += offset.hashCode();
		}
		return hashc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof AbstractHRACMemoryAddress) {
			AbstractHRACMemoryAddress other = (AbstractHRACMemoryAddress) obj;
			boolean equal = true;
			if (equal && isOffsetSpecial()) {
				return offsetSpecialnName.equals(other.offsetSpecialnName);
			}
			if (equal && offset != null) {
				return offset.equals(((AbstractHRACMemoryAddress) obj).offset);
			}
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public String toString() {
		return asHRACCode();
	}

	@Override
	public AbstractHRACMemoryAddress clone() {
		AbstractHRACMemoryAddress copy = new AbstractHRACMemoryAddress();
		try {
			copy=(AbstractHRACMemoryAddress) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		copy.offsetSpecial = offsetSpecial;
		if (offset != null) {
			copy.offset = offset.intValue();
		}
		copy.offsetSpecialnName = offsetSpecialnName;
		return copy;
	}

	public String asHRACCode() {
		if (offsetSpecial) {
			return String.format("[$%s]",  offsetSpecialnName);
		}
		if (offset != null) {
			return String.format("[%d]", offset);
		} else {
			return "";
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

	public void setOffsetSpecialName(String offsetSpecialnName) {
		this.offsetSpecialnName = offsetSpecialnName;
	}
}
