package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;

public class HRBSDivisionExpressionNode extends HRBSDualChildExpressionNode implements Cloneable {

	public HRBSDivisionExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSDivisionExpressionNode(HRBSAbstractExpressionNode child1, HRBSAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSDivisionExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()/getChilds()[1].hashCode();
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" / "+getChilds()[1].toString()+" )";
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChilds()[0].calculateNumericalValue()/getChilds()[1].calculateNumericalValue();
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASDivisionExpressionNode(getChilds()[0].compileToHRAS(parent), getChilds()[1].compileToHRAS(parent));
	}

}
