package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDivisionExpressionNode;

public class HRADDivisionExpressionNode extends HRADDualChildExpressionNode implements Cloneable {

	public HRADDivisionExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADDivisionExpressionNode(HRADAbstractDirectiveExpressionTreeNode child1, HRADAbstractDirectiveExpressionTreeNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRADModel parent) {
		return new HRASDivisionExpressionNode(getChilds()[0].compileToHRAS(parent),
				getChilds()[1].compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSDivisionExpressionNode(getChilds()[0].compileToHRBS(), getChilds()[1].compileToHRBS());

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADDivisionExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + getChilds()[0].hashCode() / getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " + getChilds()[0].toString() + " / " + getChilds()[1].toString() + " )";
	}

}
