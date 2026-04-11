package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACFactorialExpressionNode;

public class HRBSFactorialExpressionNode extends HRBSSingleChildExpressionNode implements Cloneable {

	public HRBSFactorialExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSFactorialExpressionNode(HRBSAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HRACFactorialExpressionNode compileToHRAC() {
		return new HRACFactorialExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSFactorialExpressionNode) {
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
