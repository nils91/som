/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

/**
 * @author Nils
 *
 */
public enum HRBSSymbolType implements IHBRSSymbolType{
	global,shared,local;

	@Override
	public HRBSSymbolType getType() {
		return this;
	}
}
