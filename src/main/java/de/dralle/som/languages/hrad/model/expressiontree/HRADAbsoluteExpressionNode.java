package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hras.model.HRASAbsoluteExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbsoluteExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;

public class HRADAbsoluteExpressionNode extends HRADSingleChildExpressionNode implements Cloneable {

	public HRADAbsoluteExpressionNode(HRADAbstractDirectiveExpressionTreeNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSAbsoluteExpressionNode(getChild().compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADAbsoluteExpressionNode) {
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
		return "| " + getChild().toString() + " |";
	}

}
