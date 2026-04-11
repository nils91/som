package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hras.model.PlusExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSPlusExpressionNode;

public class HRADPlusExpressionNode extends HRADCommutativeDualChildExpressionNode implements Cloneable {

	public HRADPlusExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADPlusExpressionNode(HRADAbstractDirectiveExpressionTreeNode child1, HRADAbstractDirectiveExpressionTreeNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}
	@Override
	public PlusExpressionNode compileToHRAS(HRADModel parent) {

		return new PlusExpressionNode(getChilds()[0].compileToHRAS(parent), getChilds()[1].compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {

		return new HRBSPlusExpressionNode(getChilds()[0].compileToHRBS(), getChilds()[1].compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADPlusExpressionNode) {
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
