package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACPowerExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;
import de.dralle.som.languages.hras.model.HRASPowerExpressionNode;

public class HRBSPowerExpressionNode extends HRBSDualChildExpressionNode implements Cloneable {

	public HRBSPowerExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSPowerExpressionNode(HRBSAbstractExpressionNode child1, HRBSAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSPowerExpressionNode) {
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
		return "( "+getChilds()[0].toString()+" ^ "+getChilds()[1].toString()+" )";
	}

	@Override
	public HRACPowerExpressionNode compileToHRAC( ) {

		return new HRACPowerExpressionNode(getChilds()[0].compileToHRAC(), getChilds()[1].compileToHRAC());
	}

}
