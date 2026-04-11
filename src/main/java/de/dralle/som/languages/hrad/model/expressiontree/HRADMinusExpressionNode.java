package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hras.model.HRASMinusExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSMinusExpressionNode;

public class HRADMinusExpressionNode extends HRADDualChildExpressionNode implements Cloneable {

	public HRADMinusExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADMinusExpressionNode(HRADAbstractDirectiveExpressionTreeNode child1, HRADAbstractDirectiveExpressionTreeNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}


	@Override
	public HRASMinusExpressionNode compileToHRAS(HRADModel parent) {

		return new HRASMinusExpressionNode(getChilds()[0].compileToHRAS(parent), getChilds()[1].compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSMinusExpressionNode(getChilds()[0].compileToHRBS(), getChilds()[1].compileToHRBS());

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADMinusExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + getChilds()[0].hashCode() - getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " + getChilds()[0].toString() + " - " + getChilds()[1].toString() + " )";
	}

}
