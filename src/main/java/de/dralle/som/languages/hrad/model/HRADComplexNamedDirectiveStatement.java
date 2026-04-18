package de.dralle.som.languages.hrad.model;

import java.util.List;
import java.util.Objects;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirectiveName;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public class HRADComplexNamedDirectiveStatement extends HRADAbstractDirectiveStatement<HRADAbstractDirectiveExpressionTreeNode> {
	
	public HRADComplexNamedDirectiveStatement( HRADAbstractDirectiveExpressionTreeNode name,
			HRADAbstractDirectiveExpressionTreeNode value,HRADSourceLocation sourceLocation) {
		super(name,value,sourceLocation);
	}
}
