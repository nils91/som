package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMultiplicationExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;
import de.dralle.som.languages.hras.model.HRASMultiplicationExpression;

public class HRBSMultiplicationExpressionNode extends HRBSCommutativeDualChildExpressionNode implements Cloneable {

	public HRBSMultiplicationExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSMultiplicationExpressionNode(HRBSAbstractExpressionNode child1, HRBSAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSMultiplicationExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()*getChilds()[1].hashCode();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" * "+getChilds()[1].toString()+" )";
	}

	@Override
	public HRACMultiplicationExpressionNode compileToHRAC() {

		return new HRACMultiplicationExpressionNode(getChilds()[0].compileToHRAC(), getChilds()[1].compileToHRAC());
}

}
