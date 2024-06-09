/**
 * 
 */
package de.dralle.som.languages.hras.model;

/**
 * @author Nils
 *
 */
public class SymbolHRASMemoryAddress extends AbstractHRASMemoryAddress implements Cloneable{
	private String symbol;

	public SymbolHRASMemoryAddress(int accAddress) {
		this.symbol = accAddress + "";
	}

	public SymbolHRASMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public SymbolHRASMemoryAddress(String mirrorSymbol) {
		this.symbol=mirrorSymbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int resolve(HRASModel model) {
		int address = model.resolveSymbolToAddress(symbol);
		
		return address + super.resolve(model);
	}
	@Override
	public int hashCode() {
		int hashc = symbol.hashCode();
		return hashc+super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null&&obj instanceof SymbolHRASMemoryAddress) {
			boolean equal = symbol.equals(((SymbolHRASMemoryAddress)obj).symbol)&&super.equals(obj);			
			return equal;
		}
		return false;
	}

	@Override
	public String toString() {
		return asHRASCode();
	}

	@Override
	public SymbolHRASMemoryAddress clone() {
		SymbolHRASMemoryAddress copy = (SymbolHRASMemoryAddress) super.clone();
		copy.symbol=symbol;	
		return copy;
	}

	public String asHRASCode() {
					return symbol+super.asHRASCode();
		
	}

	public void setSymbol(int address) {
		setSymbol(address+"");		
	}
}
