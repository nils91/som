/**
 * 
 */
package de.dralle.som.languages.hras.model;

/**
 * @author Nils
 *
 */
public class HRASMemoryAddress implements Cloneable{
	private String symbol;

	public HRASMemoryAddress(int accAddress) {
		this.symbol = accAddress + "";
	}

	public HRASMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public HRASMemoryAddress(String mirrorSymbol) {
		this.symbol=mirrorSymbol;
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
		if(obj!=null&&obj instanceof HRASMemoryAddress) {
			boolean equal = symbol.equals(((HRASMemoryAddress)obj).symbol);
			if(equal&&addressOffset!=null) {
				return addressOffset.equals(((HRASMemoryAddress)obj).addressOffset);
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
	public HRASMemoryAddress clone() {
		HRASMemoryAddress copy = new HRASMemoryAddress();
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

	public void setSymbol(int address) {
		setSymbol(address+"");		
	}
}
