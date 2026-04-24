package de.dralle.som.languages.hrad.model;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public class HRADComplexNamedDirectiveStatement extends HRADAbstractDirectiveStatement<HRADAbstractDirectiveExpressionTreeNode> {
	
	@Override
	public HRADComplexNamedDirectiveStatement clone() {
		HRADComplexNamedDirectiveStatement clone = (HRADComplexNamedDirectiveStatement) super.clone();
		if(getName()!=null) clone.setName(getName().clone());
		return clone;
	}

	public HRADComplexNamedDirectiveStatement( HRADAbstractDirectiveExpressionTreeNode name,
			HRADAbstractDirectiveExpressionTreeNode value,HRADSourceLocation sourceLocation) {
		super(name,value,sourceLocation);
	}
}
