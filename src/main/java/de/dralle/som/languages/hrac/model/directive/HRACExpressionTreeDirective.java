package de.dralle.som.languages.hrac.model.directive;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
/**
 * Also used for int.
 */
public class HRACExpressionTreeDirective extends AbstractDirective<HRACAbstractExpressionNode>{

	@Override
	public HRACExpressionTreeDirective clone() {
		// TODO Auto-generated method stub
		HRACExpressionTreeDirective clone = (HRACExpressionTreeDirective) super.clone();
		if(getValue()!=null) {
			clone.setValue(getValue().clone());
		}
		return clone;
	}

	public HRACExpressionTreeDirective(boolean global, String name, HRACAbstractExpressionNode value) {
		super(global, name, value);
		// TODO Auto-generated constructor stub
	}

}
