/**
 * 
 */
package de.dralle.som.languages.hras.model;

/**
 * @author Nils
 *
 */
public class SymbolHRASMemoryAddress extends AbstractHRASMemoryAddress implements Cloneable {
	private String symbol;

	public SymbolHRASMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public SymbolHRASMemoryAddress(HRASAbstractExpressionNode tgtAdr) {
		// TODO Auto-generated constructor stub
	}

	public SymbolHRASMemoryAddress(int accAddress) {
		this.symbol = accAddress + "";
	}

	public SymbolHRASMemoryAddress(String mirrorSymbol) {
		this.symbol = mirrorSymbol;
	}

	public String asHRASCode() {
		return symbol + super.asHRASCode();

	}

	@Override
	public SymbolHRASMemoryAddress clone() {
		SymbolHRASMemoryAddress copy = (SymbolHRASMemoryAddress) super.clone();
		copy.symbol = symbol;
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SymbolHRASMemoryAddress) {
			boolean equal = symbol.equals(((SymbolHRASMemoryAddress) obj).symbol) && super.equals(obj);
			return equal;
		}
		return false;
	}

	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		int hashc = symbol.hashCode();
		return hashc + super.hashCode();
	}

	public int resolve(HRASModel model) {
		int address = model.resolveSymbolToAddress(symbol);

		return address + super.resolve(model);
	}

	public void setSymbol(int address) {
		setSymbol(address + "");
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return asHRASCode();
	}
}
