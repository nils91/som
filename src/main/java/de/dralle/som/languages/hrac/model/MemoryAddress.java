/**
 * 
 */
package de.dralle.som.languages.hrac.model;

/**
 * @author Nils
 *
 */
public class MemoryAddress implements Cloneable{
	private String symbol;

	public MemoryAddress(int accAddress) {
		this.symbol = accAddress + "";
	}

	public MemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getAddressOffset() {
		return addressOffset;
	}

	public void setAddressOffset(Integer addressOffset) {
		this.addressOffset = addressOffset;
	}

	private Integer addressOffset;

	public int resolve(HRASModel model) {
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
		if(obj!=null&&obj instanceof MemoryAddress) {
			boolean equal = symbol.equals(((MemoryAddress)obj).symbol);
			if(equal&&addressOffset!=null) {
				return addressOffset.equals(((MemoryAddress)obj).addressOffset);
			}
			return equal;
		}
		return false;
	}

	@Override
	public String toString() {
		return asHRASCode();
	}

	@Override
	public MemoryAddress clone() {
		MemoryAddress copy = new MemoryAddress();
		copy.symbol=symbol;
		if(addressOffset!=null) { 
			copy.addressOffset=addressOffset.intValue();
		}		
		return copy;
	}

	public String asHRASCode() {
		if(addressOffset!=null) {
			return String.format("%s[%d]", symbol,addressOffset);
		}else {
			return symbol;
		}
	}
}
