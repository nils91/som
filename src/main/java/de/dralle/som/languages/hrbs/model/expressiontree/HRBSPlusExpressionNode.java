package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;
import de.dralle.som.languages.hras.model.PlusExpressionNode;

public class HRBSPlusExpressionNode extends HRBSCommutativeDualChildExpressionNode implements Cloneable {

	public HRBSPlusExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSPlusExpressionNode(HRBSAbstractExpressionNode child1, HRBSAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSPlusExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()+getChilds()[1].hashCode();
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChilds()[0].calculateNumericalValue()+getChilds()[1].calculateNumericalValue();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" + "+getChilds()[1].toString()+" )";
	}

	@Override
	public PlusExpressionNode compileToHRAC(HRACModel parent) {

		return new PlusExpressionNode(getChilds()[0].compileToHRAC(parent), getChilds()[1].compileToHRAC(parent));
	}

}
