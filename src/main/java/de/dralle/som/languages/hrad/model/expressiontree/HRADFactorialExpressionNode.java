package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hras.model.HRASFactorialExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSFactorialExpressionNode;

public class HRADFactorialExpressionNode extends HRADSingleChildExpressionNode implements Cloneable {

	public HRADFactorialExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADFactorialExpressionNode(HRADAbstractDirectiveExpressionTreeNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}



	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSFactorialExpressionNode(getChild().compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADFactorialExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getChild().toString() + "!";
	}

}
