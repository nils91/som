package de.dralle.som.languages.hras.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMinusExpressionNode;

public class HRASMinusExpressionNode extends HRASDualChildExpressionNode implements Cloneable {

	public HRASMinusExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRASMinusExpressionNode(HRASAbstractExpressionNode child1, HRASAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRASMinusExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()-getChilds()[1].hashCode();
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChilds()[0].calculateNumericalValue()-getChilds()[1].calculateNumericalValue();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" - "+getChilds()[1].toString()+" )";
	}

	@Override
	public HRACMinusExpressionNode compileToHRAC() {
		return new HRACMinusExpressionNode(getChilds()[0].compileToHRAC(), getChilds()[1].compileToHRAC());
	}

}
