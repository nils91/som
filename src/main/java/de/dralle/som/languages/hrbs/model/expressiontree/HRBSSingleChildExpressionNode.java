package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACSingleChildExpressionNode;

public class HRBSSingleChildExpressionNode extends HRBSAbstractExpressionNode implements Cloneable {
	private HRBSAbstractExpressionNode child;

	public HRBSSingleChildExpressionNode() {
		super();
	}

	public HRBSSingleChildExpressionNode(HRBSAbstractExpressionNode child) {
		super();
		this.child = child;
	}

	@Override
	public HRBSSingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRBSSingleChildExpressionNode cl = (HRBSSingleChildExpressionNode) super.clone();
		cl.child = child.clone();
		return cl;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode compileToHRAC() {
		return new HRACSingleChildExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSSingleChildExpressionNode) {
			HRBSSingleChildExpressionNode oth = (HRBSSingleChildExpressionNode) obj;
			return child.equals(oth.child);
		}
		return false;
	}

	public HRBSAbstractExpressionNode getChild() {
		return child;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}

	public void setChild(HRBSAbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return child.toString();
	}

}
