/**
 * 
 */
package de.dralle.som.languages.hrac.model;

/**
 * @author Nils
 *
 */
public class HRACMemoryAddress implements Cloneable{
	private HRACSymbol symbol;
	private Integer offset;

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

	public Integer getAddressOffset() {
		return addressOffset;
	}

	public void setAddressOffset(Integer addressOffset) {
		this.addressOffset = addressOffset;
	}

	private Integer addressOffset;

	public int resolve(HRACModel model) {
		int address = model.resolveSymbolToAddress(symbol);
		if(addressOffset==null) {
			return address;
		}
		return address + addressOffset;
	}
	@Override
	public int hashCode() {
		int hashc = symbol.hashCode();
		if(addressOffset!=null) {
			hashc+=addressOffset.hashCode();
		}
		return hashc;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null&&obj instanceof HRACMemoryAddress) {
			boolean equal = symbol.equals(((HRACMemoryAddress)obj).symbol);
			if(equal&&addressOffset!=null) {
				return addressOffset.equals(((HRACMemoryAddress)obj).addressOffset);
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
		copy.symbol=symbol;
		if(addressOffset!=null) { 
			copy.addressOffset=addressOffset.intValue();
		}		
		return copy;
	}

	public String asHRACCode() {
		if(offset!=null) {
			return String.format("%s[%d]", symbol.getName(),offset);
		}else {
			return symbol.getName();
		}
	}
}
