package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACNegationExpressionNode;

public class HRADNegationExpressionNode extends HRADSingleChildExpressionNode implements Cloneable {

	public HRADNegationExpressionNode(HRADAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode compileToHRAC() {
		return new HRACNegationExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADNegationExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "- " + getChild().toString();
	}

}
