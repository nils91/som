package de.dralle.som.languages.hrad.model.directive;
import java.util.List;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public abstract class HRADAbstractCustomDirectiveFunction  {
	
	public abstract HRADAbstractDirectiveExpressionTreeNode getValue(List<HRADAbstractDirectiveExpressionTreeNode> nodeParamValues);

	
}
