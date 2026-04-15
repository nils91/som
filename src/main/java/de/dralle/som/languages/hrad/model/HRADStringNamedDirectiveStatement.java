package de.dralle.som.languages.hrad.model;

import java.util.List;
import java.util.Objects;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirective;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public class HRADStringNamedDirectiveStatement extends HRADAbstractDirectiveStatement<String> {
	
	public HRADStringNamedDirectiveStatement( String name,
			HRADAbstractDirectiveExpressionTreeNode value,HRADSourceLocation sourceLocation) {
		super(name,value,sourceLocation);
	}
}
