package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMinusExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;
import de.dralle.som.languages.hras.model.HRASMinusExpressionNode;
import de.dralle.som.languages.hrbs.model.HRBSModel;

public class HRBSMinusExpressionNode extends HRBSDualChildExpressionNode implements Cloneable {

	public HRBSMinusExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSMinusExpressionNode(HRBSAbstractExpressionNode child1, HRBSAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSMinusExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()-getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" - "+getChilds()[1].toString()+" )";
	}

	@Override
	public HRACMinusExpressionNode compileToHRAC(HRBSModel parent) {

		return new HRACMinusExpressionNode(getChilds()[0].compileToHRAS(parent), getChilds()[1].compileToHRAS(parent));
}

}
