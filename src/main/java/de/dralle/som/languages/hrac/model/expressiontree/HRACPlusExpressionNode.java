package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.PlusExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSPlusExpressionNode;

public class HRACPlusExpressionNode extends HRACCommutativeDualChildExpressionNode implements Cloneable {

	public HRACPlusExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRACPlusExpressionNode(HRACAbstractDirectiveExpressionTreeNode child1, HRACAbstractDirectiveExpressionTreeNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}
	@Override
	public PlusExpressionNode compileToHRAS(HRACModel parent) {

		return new PlusExpressionNode(getChilds()[0].compileToHRAS(parent), getChilds()[1].compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {

		return new HRBSPlusExpressionNode(getChilds()[0].compileToHRBS(), getChilds()[1].compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACPlusExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + getChilds()[0].hashCode() + getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " + getChilds()[0].toString() + " + " + getChilds()[1].toString() + " )";
	}

}
