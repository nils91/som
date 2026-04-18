package de.dralle.som.languages.hrad.model;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public class HRADComplexNamedDirectiveStatement extends HRADAbstractDirectiveStatement<HRADAbstractDirectiveExpressionTreeNode> {
	
	public HRADComplexNamedDirectiveStatement( HRADAbstractDirectiveExpressionTreeNode name,
			HRADAbstractDirectiveExpressionTreeNode value,HRADSourceLocation sourceLocation) {
		super(name,value,sourceLocation);
	}
}
