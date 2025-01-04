package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;
import de.dralle.som.languages.hras.model.HRASPowerExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSPowerExpressionNode;

public class HRACPowerExpressionNode extends HRACDualChildExpressionNode implements Cloneable {

	public HRACPowerExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRACPowerExpressionNode(HRACAbstractExpressionNode child1, HRACAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACPowerExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()*getChilds()[1].hashCode();
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return (int) Math.pow(getChilds()[0].calculateNumericalValue(),getChilds()[1].calculateNumericalValue());
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" ^ "+getChilds()[1].toString()+" )";
	}

	@Override
	public HRASPowerExpressionNode compileToHRAS(HRACModel parent) {

		return new HRASPowerExpressionNode(getChilds()[0].compileToHRAS(parent), getChilds()[1].compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSPowerExpressionNode(getChilds()[0].compileToHRBS(), getChilds()[1].compileToHRBS());

	}

}
