package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASMultiplicationExpression;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSMultiplicationExpressionNode;

public class HRACMultiplicationExpressionNode extends CommutativeDualChildExpressionNode implements Cloneable {

	public HRACMultiplicationExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRACMultiplicationExpressionNode(HRACAbstractExpressionNode child1, HRACAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChilds()[0].calculateNumericalValue() * getChilds()[1].calculateNumericalValue();
	}

	@Override
	public HRASMultiplicationExpression compileToHRAS(HRACModel parent) {

		return new HRASMultiplicationExpression(getChilds()[0].compileToHRAS(parent),
				getChilds()[1].compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {

		return new HRBSMultiplicationExpressionNode(getChilds()[0].compileToHRBS(), getChilds()[1].compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACMultiplicationExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + getChilds()[0].hashCode() * getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " + getChilds()[0].toString() + " * " + getChilds()[1].toString() + " )";
	}

}
