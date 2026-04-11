package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACPlusExpressionNode;

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
	public HRACPlusExpressionNode compileToHRAC() {

		return new HRACPlusExpressionNode(getChilds()[0].compileToHRAC(), getChilds()[1].compileToHRAC());
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
		return super.hashCode() + getChilds()[0].hashCode() + getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " + getChilds()[0].toString() + " + " + getChilds()[1].toString() + " )";
	}

}
