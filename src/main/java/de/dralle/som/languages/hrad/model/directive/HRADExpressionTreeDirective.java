package de.dralle.som.languages.hrad.model.directive;

import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
/**
 * Also used for int.
 */
public class HRADExpressionTreeDirective extends HRADAbstractDirective<HRADAbstractDirectiveExpressionTreeNode>{

	@Override
	public HRADExpressionTreeDirective clone() {
		// TODO Auto-generated method stub
		HRADExpressionTreeDirective clone = (HRADExpressionTreeDirective) super.clone();
		if(getValue()!=null) {
			clone.setValue(getValue().clone());
		}
		return clone;
	}

	public HRADExpressionTreeDirective(boolean global, String name, HRADAbstractDirectiveExpressionTreeNode value) {
		super(global, name, value);
		// TODO Auto-generated constructor stub
	}

}
