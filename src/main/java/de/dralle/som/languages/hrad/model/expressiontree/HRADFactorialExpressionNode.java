package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACFactorialExpressionNode;

public class HRADFactorialExpressionNode extends HRADSingleChildExpressionNode implements Cloneable {

	public HRADFactorialExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADFactorialExpressionNode(HRADAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HRACFactorialExpressionNode compileToHRAC() {
		return new HRACFactorialExpressionNode(getChild().compileToHRAC());
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
