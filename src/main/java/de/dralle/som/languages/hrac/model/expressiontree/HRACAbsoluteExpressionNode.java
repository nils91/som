package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbsoluteExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbsoluteExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;

public class HRACAbsoluteExpressionNode extends HRACSingleChildExpressionNode implements Cloneable {

	public HRACAbsoluteExpressionNode(HRACAbstractDirectiveExpressionTreeNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASAbsoluteExpressionNode(getChild().compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSAbsoluteExpressionNode(getChild().compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACAbsoluteExpressionNode) {
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
