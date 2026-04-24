package de.dralle.som.languages.hrad.model;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public class HRADStringNamedDirectiveStatement extends HRADAbstractDirectiveStatement<String> {
	
	@Override
	public HRADStringNamedDirectiveStatement clone() {
		HRADStringNamedDirectiveStatement clone = (HRADStringNamedDirectiveStatement) super.clone();
		return clone;
	}

	public HRADStringNamedDirectiveStatement( String name,
			HRADAbstractDirectiveExpressionTreeNode value,HRADSourceLocation sourceLocation) {
		super(name,value,sourceLocation);
	}
}
