package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACNegationExpressionNode;

public class HRBSNegationExpressionNode extends HRBSSingleChildExpressionNode implements Cloneable {

	public HRBSNegationExpressionNode(HRBSAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HRACAbstractExpressionNode compileToHRAC() {
		return new HRACNegationExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSNegationExpressionNode) {
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
