/**
 * 
 */
package de.dralle.som.languages.hra.model;

/**
 * @author Nils
 *
 */
public class MemoryAddress {
private String symbol;
public String getSymbol() {
	return symbol;
}
public void setSymbol(String symbol) {
	this.symbol = symbol;
}
public int getAddressOffset() {
	return addressOffset;
}
public void setAddressOffset(int addressOffset) {
	this.addressOffset = addressOffset;
}
private int addressOffset;
public int resolve(HRAModel model) {
	int address=model.resolveSymbolToAddress(symbol);
	return address+addressOffset;
}
}
